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
    private final String battleField;
    private final int requiredTeams;
    private final String difficulty;

    public MachineDetails(int totalRotors, int rotorsInUse, int reflectorsCount,
                          Set<String> dictionary, List<Character> keys, String battleField,
                          int requiredTeams, String difficulty) {
        this.totalRotors = totalRotors;
        this.rotorsInUse = rotorsInUse;
        this.reflectorsCount = reflectorsCount;
        this.dictionary = new ArrayList<>(dictionary);
        this.keys = keys;
        this.battleField = battleField;
        this.requiredTeams = requiredTeams;
        this.difficulty = difficulty;
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

    public String getBattleField() {
        return battleField;
    }

    public int getRequiredTeams() {
        return requiredTeams;
    }

    public String getDifficulty() {
        return difficulty;
    }
}
