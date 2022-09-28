package engine.stock;

import enigmaMachine.keyBoard.KeyBoard;
import enigmaMachine.reflector.Reflecting;
import enigmaMachine.reflector.Reflector;
import enigmaMachine.rotor.Rotor;
import enigmaMachine.rotor.TheRotor;
import schema.generated.CTEPositioning;
import schema.generated.CTEReflect;
import schema.generated.CTEReflector;
import schema.generated.CTERotor;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Stock implements Serializable, Cloneable {
    private final Map<Integer,Rotor> rotorMap;
    private final Map<String, Reflecting> reflectorMap;
    private final KeyBoard keyBoard;
    private final int rotorsCount;

    public Stock(List<CTERotor> rotors, List<CTEReflector> reflectors, KeyBoard keyBoard, int rotorsCount) {
        rotorMap = new HashMap<>();
        reflectorMap = new HashMap<>();
        this.keyBoard = keyBoard;
        this.rotorsCount = rotorsCount;
        fillRotorsMap(rotors);
        fillReflectorsMap(reflectors);
    }

    public Stock(Map<Integer, Rotor> rotorMap, Map<String, Reflecting> reflectorMap, KeyBoard keyBoard, int rotorsCount){
        this.rotorMap = rotorMap;
        this.reflectorMap = reflectorMap;
        this.keyBoard = keyBoard;
        this.rotorsCount = rotorsCount;
    }
    private void fillRotorsMap(List<CTERotor> CTERotors) {
        CTERotors.forEach(r -> rotorMap.put(r.getId(), new TheRotor(r.getId(),
                r.getNotch() - 1, getPermutation(r.getCTEPositioning()))));
    }

    private String[] getPermutation(List<CTEPositioning> positionings) {
        String[] permutations = new String[2];
        String permutationL = "";
        String permutationR = "";
        for(CTEPositioning pos : positionings) {
            permutationL = permutationL.concat(pos.getLeft());
            permutationR = permutationR.concat(pos.getRight());
        }
        permutations[0] = permutationL;
        permutations[1] = permutationR;
        return permutations;
    }

    private void fillReflectorsMap(List<CTEReflector> CTEReflectors) {
        CTEReflectors.forEach(r -> reflectorMap.put(r.getId(), new Reflector(r.getId(),
                                                reflectionListToMap(r.getCTEReflect()))));
    }

    //getting pairs in base 1 (1 -> n) and return map in base 0 (0 -> n-1)
    private Map<Integer, Integer> reflectionListToMap(List<CTEReflect> reflectionList) {
        Map<Integer, Integer> int2int = new HashMap<>();
        reflectionList.forEach(r -> { int2int.put((r.getInput()-1), (r.getOutput()-1));
                                      int2int.put((r.getOutput()-1), (r.getInput()-1));
                                });
        return int2int;
    }

    public Map<Integer, Integer> getNotches() {
        Map<Integer, Integer> id2notch = new HashMap<>();
        rotorMap.forEach((id, r) -> id2notch.put(id, r.getRelativeNotch()));
        return id2notch;
    }

    public Map<Integer, Rotor> getRotorMap() {
        return rotorMap;
    }

    public Map<String, Reflecting> getReflectorMap() {
        return reflectorMap;
    }

    public KeyBoard getKeyBoard() {
        return keyBoard;
    }

    public int getRotorsCount() {
        return rotorsCount;
    }

    public Reflecting getReflector(String ID){
        return reflectorMap.get(ID);
    }

    @Override
    public String toString() {
        return "Stock{" +
                "rotors=" + rotorMap +
                ", reflectors=" + reflectorMap +
                ", keyBoard=" + keyBoard +
                ", rotorsCount=" + rotorsCount +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Stock stock = (Stock) o;
        return rotorsCount == stock.rotorsCount && Objects.equals(rotorMap, stock.rotorMap) && Objects.equals(reflectorMap, stock.reflectorMap) && Objects.equals(keyBoard, stock.keyBoard);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rotorMap, reflectorMap, keyBoard, rotorsCount);
    }

    @Override
    protected Stock clone() {
        Map<Integer, Rotor> rotorMap = new HashMap<>();
        Map<String, Reflecting> reflectorMap = new HashMap<>();
        this.rotorMap.forEach((id, rotor) ->
                rotorMap.put(id, ((TheRotor)rotor).clone()));
        this.reflectorMap.forEach((id, reflector) ->
                reflectorMap.put(id, ((Reflector)reflector).clone()));
        KeyBoard keyboard = this.keyBoard.clone();
        return new Stock(rotorMap, reflectorMap, keyboard, this.rotorsCount);
    }
}
