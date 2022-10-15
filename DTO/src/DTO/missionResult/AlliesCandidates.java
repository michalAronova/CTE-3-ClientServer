package DTO.missionResult;

import DTO.codeObj.CodeObj;
import javafx.util.Pair;

import java.util.List;

public class AlliesCandidates{
    private final List<Pair<String, CodeObj>> candidates;
    private String entityName;

    public AlliesCandidates(List<Pair<String, CodeObj>> candidates, String allyName) {
        this.candidates = candidates;
        this.entityName = allyName;
    }
    public List<Pair<String, CodeObj>> getCandidates() {
        return candidates;
    }

    public String getAllyName() {
        return entityName;
    }

    public void setAllyName(String allyName) {
        this.entityName = allyName;
    }
}
