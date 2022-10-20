package DTO.dmInfo;

import DTO.contest.Contest;

import java.util.List;
import java.util.Set;

public class DMInfo {
    private final boolean isCompetitionOn;
    private final Contest contestDetails;
    private final List<Character> keys;
    private final Set<String> dictionary;

    public DMInfo(boolean isCompetitionOn, Contest contestDetails,
                  List<Character> keys, Set<String> dictionary) {
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

    public Set<String> getDictionary() {
        return dictionary;
    }
}
