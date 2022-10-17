package DTO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MachineDetails implements Serializable {
    private final int totalRotors;
    private final int rotorsInUse;
    private final int reflectorsCount;
    private final List<String> dictionary;
    private final List<Character> keys;

    public MachineDetails(int totalRotors, int rotorsInUse, int reflectorsCount,
                          Set<String> dictionary, List<Character> keys) {
        this.totalRotors = totalRotors;
        this.rotorsInUse = rotorsInUse;
        this.reflectorsCount = reflectorsCount;
        this.dictionary = new ArrayList<>(dictionary);
        this.keys = keys;
    }

    public int getTotalRotors() {
        return totalRotors;
    }

    public int getRotorsInUse() {
        return rotorsInUse;
    }

    public int getReflectorsCount() {
        return reflectorsCount;
    }

    public List<String> getDictionary() {
        return dictionary;
    }

    public List<Character> getKeys() {
        return keys;
    }
}
