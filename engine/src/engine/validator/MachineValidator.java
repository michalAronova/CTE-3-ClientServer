package engine.validator;

import enigmaMachine.reflector.ReflectorID;
import exceptions.XMLException.InvalidXMLException;
import exceptions.XMLException.XMLExceptionMsg;
import schema.generated.*;
import java.util.*;

public class MachineValidator implements Validator{
    private final CTEMachine machine;
    private final String processedABC;
    private final int minimumRotorsCount;
    private final int maximumRotorsCount;

    public MachineValidator(CTEMachine machine, int minimumRotorsCount, int maximumRotorsCount){
        this.machine = machine;
        this.processedABC = machine.getABC().trim().toUpperCase();
        this.minimumRotorsCount = minimumRotorsCount;
        this.maximumRotorsCount = maximumRotorsCount;
    }

    @Override
    public boolean validate() {
        validateABC();
        validateRotorsAndCount(); // continues to this method only if abc is valid
        validateReflectors(); // continues to this method only if abc is valid
        return true;
    }

    private void validateABC(){
        if(processedABC.length()%2 == 1) {
            throw new InvalidXMLException(XMLExceptionMsg.INVALIDABC, "odd abc");
        }
        if(!checkUniqueString(processedABC)){
            throw new InvalidXMLException(XMLExceptionMsg.INVALIDABC, "abc not unique");
        }
    }

    //must call validateABC before this method
    private void validateRotorsAndCount(){
        List<CTERotor> rotors = machine.getCTERotors().getCTERotor();
        int rotorsCount = machine.getRotorsCount();
        if(rotorsCount > rotors.size() || rotorsCount > maximumRotorsCount || rotorsCount < minimumRotorsCount) {
            throw new InvalidXMLException(XMLExceptionMsg.INVALIDROTOR,
                    String.format("invalid rotor count, range is %d - %d",
                            minimumRotorsCount, Math.min(rotors.size(), maximumRotorsCount)));
        }
        boolean[] rotorsCheckers = new boolean[rotors.size() + 1];
        for(CTERotor r : rotors){
            int id = r.getId();
            if(id > rotors.size() || id <= 0) {
                throw new InvalidXMLException(XMLExceptionMsg.INVALIDROTOR,
                        String.format("id out of range (%d - %d)", 0, rotors.size()));
            }
            if(rotorsCheckers[id]) {
                throw new InvalidXMLException(XMLExceptionMsg.INVALIDROTOR, "more than one rotor with same ID");
            }
            rotorsCheckers[id] = true;
            if(r.getNotch() > processedABC.length() || r.getNotch() <= 0) {
                throw new InvalidXMLException(XMLExceptionMsg.INVALIDROTOR,
                        String.format("notch location out of range (%d - %d)", 1, processedABC.length()));
            }
            checkPositioning(r); //throws exception if not valid positioning
        }
    }

    //must call validateABC before this method
    private void validateReflectors(){
        List<CTEReflector> reflectors = machine.getCTEReflectors().getCTEReflector();
        Map<String, Integer> ID2Cnt = new HashMap<>();
        for(CTEReflector r : reflectors){
            if(ID2Cnt.getOrDefault(r.getId(), 0) == 0 && checkValidReflectorID(r.getId())) {
                ID2Cnt.put(r.getId(), 1);
            }
            else{
                throw new InvalidXMLException(XMLExceptionMsg.INVALIDREFLECTOR, "invalid reflector ID");
            }
            List<CTEReflect> reflector = r.getCTEReflect();
            for(CTEReflect reflection : reflector) {
                if(reflection.getInput() == reflection.getOutput()) {
                    throw new InvalidXMLException(XMLExceptionMsg.INVALIDREFLECTOR, "self reflecting");
                }
                int abcLen = processedABC.length();
                if(reflection.getInput() > abcLen || reflection.getOutput() > abcLen) {
                    throw new InvalidXMLException(XMLExceptionMsg.INVALIDREFLECTOR, "reflection value out of range");
                }
            }
        }
    }

    private boolean checkUniqueString(String s) {
        Map<Character, Integer> char2cnt = new HashMap<>();
        for(Character c : s.toCharArray()){
            if(char2cnt.getOrDefault(c,0) == 0) {
                char2cnt.put(c, 1);
            }
            else {
                return false;
            }
        }
        return true;
    }

    private void checkPositioning(CTERotor r){
        Map<String, Integer> char2CntLEFT = new HashMap<>();
        Map<String, Integer> char2CntRIGHT = new HashMap<>();
        List<CTEPositioning> rotorPermutations = r.getCTEPositioning();
        for(CTEPositioning p : rotorPermutations) {
            String left = p.getLeft().toUpperCase();
            String right = p.getRight().toUpperCase();
            if(left.length() != 1 || right.length() != 1){
                throw new InvalidXMLException(XMLExceptionMsg.INVALIDROTOR, "not a single character in positioning");
            }
            if(!processedABC.contains(left) || !processedABC.contains(right)){
                throw new InvalidXMLException(XMLExceptionMsg.INVALIDROTOR, "positioning value not in abc");
            }
            if(char2CntLEFT.getOrDefault(left,0) == 0) {
                char2CntLEFT.put(left, 1);
            }
            else {
                throw new InvalidXMLException(XMLExceptionMsg.INVALIDROTOR, "double mapping");
            }
            if(char2CntRIGHT.getOrDefault(right,0) == 0) {
                char2CntRIGHT.put(right, 1);
            }
            else {
                throw new InvalidXMLException(XMLExceptionMsg.INVALIDROTOR, "double mapping");
            }
        }
    }

    private boolean checkValidReflectorID(String ID) {
        for(int i=0 ; i < machine.getCTEReflectors().getCTEReflector().size() ; i++){
            ReflectorID r = ReflectorID.values()[i];
            if (r.name().equals(ID)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "MachineValidator{" +
                "machine=" + machine +
                ", ABC='" + processedABC + '\'' +
                ", minimumRotorsCount=" + minimumRotorsCount +
                ", maximumRotorsCount=" + maximumRotorsCount +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MachineValidator that = (MachineValidator) o;
        return minimumRotorsCount == that.minimumRotorsCount && maximumRotorsCount == that.maximumRotorsCount && Objects.equals(machine, that.machine) && Objects.equals(processedABC, that.processedABC);
    }

    @Override
    public int hashCode() {
        return Objects.hash(machine, processedABC, minimumRotorsCount, maximumRotorsCount);
    }
}
