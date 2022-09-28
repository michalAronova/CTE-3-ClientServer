package enigmaMachine;

import DTO.codeObj.CodeObj;
import DTO.machineAnatomy.MachineAnatomy;
import DTO.machineAnatomy.componentAnatomy.ComponentAnatomy;
import DTO.machineAnatomy.componentAnatomy.ComponentType;
import enigmaMachine.keyBoard.KeyBoard;
import enigmaMachine.plugBoard.Plugs;
import enigmaMachine.reflector.Reflecting;
import enigmaMachine.rotor.Rotor;
import enigmaMachine.secret.Secret;
import javafx.util.Pair;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.*;

public class Machine implements Serializable, Cloneable {
    private final KeyBoard keyboard;
    private final int rotorCount;
    private List<Rotor> rotors;
    private Reflecting reflector;
    private Plugs plugBoard;

    public Machine(KeyBoard keyboard, int rotorCount){
        this.keyboard = keyboard;
        this.rotorCount = rotorCount;
    }

    public Machine(KeyBoard keyboard, List<Rotor> rotors, Reflecting reflector, Plugs plugBoard, int rotorCount) {
        this.keyboard = keyboard;
        this.rotors = rotors;
        setRotors();
        this.reflector = reflector;
        this.plugBoard = plugBoard;
        this.rotorCount = rotorCount;
    }

    public Machine(Secret settings, KeyBoard keyboard, int rotorCount) {
        this.keyboard = keyboard;
        this.rotorCount = rotorCount;
        updateBySecret(settings);
    }

    public void updateBySecret(@NotNull Secret settings) {
        this.rotors = settings.getRotors();
        setRotors();
        this.reflector = settings.getReflector();
        this.plugBoard = settings.getPlugBoard();
    }

    private void setRotors() {
        for(int i = 0; i < rotors.size() - 1 ; i++){
            rotors.get(i).setNextRotor(rotors.get(i + 1));
        }
    }

    public void resetRotorsToInitial(){
        rotors.forEach(Rotor::reset);
    }

    private int passThroughRotorsR2L(int index) {
        int passedIndex = index;
        for(Rotor r : rotors) {
            passedIndex = r.rightToLeftReturnIndex(passedIndex);
        }
        return passedIndex;
    }

    private int passThroughRotorsL2R(int index) {
        int passedIndex = index;
        Collections.reverse(rotors);
        for(Rotor r : rotors) {
            passedIndex = r.leftToRightReturnIndex(passedIndex);
        }
        Collections.reverse(rotors);
        return passedIndex;
    }

    public Character process(Character input) {
        input = Character.toUpperCase(input);
        rotors.get(0).advance();
        Character pluggedOutput = plugBoard.passThroughPlugBoard(input);
        int rotorsInput = keyboard.indexOf(pluggedOutput);
        int reflectorInput = passThroughRotorsR2L(rotorsInput);
        rotorsInput = reflector.passThroughReflector(reflectorInput);
        int pluggedInputIndex = passThroughRotorsL2R(rotorsInput);
        Character pluggedInput = keyboard.charAt(pluggedInputIndex);
        pluggedOutput = plugBoard.passThroughPlugBoard(pluggedInput);
        return Character.toUpperCase(pluggedOutput);
    }
    public List<Rotor> getRotors() {
        return rotors;
    }

    @Override
    public String toString() {
        return "Machine{" +
                "keyboard=" + keyboard +
                ", rotorCount=" + rotorCount +
                ", rotors=" + rotors +
                ", reflector=" + reflector +
                ", plugBoard=" + plugBoard +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Machine machine = (Machine) o;
        return rotorCount == machine.rotorCount && Objects.equals(keyboard, machine.keyboard) && Objects.equals(rotors, machine.rotors) && Objects.equals(reflector, machine.reflector) && Objects.equals(plugBoard, machine.plugBoard);
    }

    @Override
    public int hashCode() {
        return Objects.hash(keyboard, rotorCount, rotors, reflector, plugBoard);
    }

    @Override
    public Machine clone(){
        return new Machine(this.keyboard.clone(), this.rotorCount);
    }

    public void updateByPositionsList(List<Character> startRotorsPositions) {
        for (int i = 0; i < rotors.size(); i++) {
            rotors.get(i)
                    .setInitialPosition(rotors.get(i)
                            .getRightPermutation()
                            .indexOf(startRotorsPositions.get(i)));
        }
    }

    public String processWord(String toDecrypt) {
        StringBuilder sb = new StringBuilder();
        for (Character c:toDecrypt.toCharArray()) {
            sb.append(process(c));
        }
        return sb.toString();
    }

    public int getRotorCount() {
        return rotorCount;
    }

    public CodeObj getMachineCode(){
        List<Pair<Integer, Character>> ID2PositionList = new LinkedList<>();
        Map<Integer, Integer> notchRelativeLocation = new HashMap<>();
        String reflectorID = reflector.getID();
        List<Pair<Character, Character>> plugs = plugBoard.getPlugsForCode();
        rotors.forEach(rotor -> {
            ID2PositionList.add(new Pair<>(rotor.getID(), rotor.getCurrentPositionChar()));
            notchRelativeLocation.put(rotor.getID(), rotor.getRelativeNotch());
        });

        return new CodeObj(ID2PositionList, reflectorID, plugs, notchRelativeLocation);
    }

    public MachineAnatomy getMachineAnatomy(){
        List<ComponentAnatomy> rotorsAnatomy = new LinkedList<>();
        rotors.forEach(rotor -> {
            rotorsAnatomy.add(new ComponentAnatomy(ComponentType.ROTOR,
                                                    rotor.getLeftPermutation(),
                                                    rotor.getRightPermutation(),
                                                    String.valueOf(rotor.getID())));
        });
        ComponentAnatomy reflectorAnatomy = new ComponentAnatomy(ComponentType.REFLECTOR,
                                                    reflector.getLeftPermutation(),
                                                    reflector.getRightPermutation(),
                                                    reflector.getID());

        return new MachineAnatomy(reflectorAnatomy, rotorsAnatomy);
    }

    public void setReflector(Reflecting reflector) {
        this.reflector = reflector;
    }

    public void setRotors(List<Rotor> newPermutation) {
        this.rotors = newPermutation;
        setRotors();
    }
}
