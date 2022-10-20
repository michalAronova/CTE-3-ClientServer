package DTO.dmInfo;

import DTO.contest.Contest;

import java.util.List;

public class DMInfo {
    private final boolean isCompetitionOn;
    private final Contest contestDetails;
    private final List<Character> keys;
    private final List<String> dictionary;

    public DMInfo(boolean isCompetitionOn, Contest contestDetails,
                  List<Character> keys, List<String> dictionary) {
        this.isCompetitionOn = isCompetitionOn;
        this.contestDetails = contestDetails;
        this.keys = keys;
        this.dictionary = dictionary;
    }

    public boolean isCompetitionOn() {
        return isCompetitionOn;
    }

    public Contest getContestDetails() {
        return contestDetails;
    }

    public List<Character> getKeys() {
        return keys;
    }

    public List<String> getDictionary() {
        return dictionary;
    }
}
