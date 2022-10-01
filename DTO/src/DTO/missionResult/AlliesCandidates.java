package DTO.missionResult;

import DTO.codeObj.CodeObj;
import javafx.util.Pair;

import java.util.List;

public class AlliesCandidates {
    private List<Pair<String, CodeObj>> candidates;
    private String allyName;

    public AlliesCandidates(List<Pair<String, CodeObj>> candidates, String allyName) {
        this.candidates = candidates;
        this.allyName = allyName;
    }
    public List<Pair<String, CodeObj>> getCandidates() {
        return candidates;
    }

    public String getAllyName() {
        return allyName;
    }
}
