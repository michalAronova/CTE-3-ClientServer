package DTO.missionResult;

import DTO.codeObj.CodeObj;
import javafx.util.Pair;

import java.util.LinkedList;
import java.util.List;

public class Candidate {
    private final String allyName;
    private final String candidate;
    private final CodeObj code;

    public Candidate(String allyName, String candidate, CodeObj code) {
        this.allyName = allyName;
        this.candidate = candidate;
        this.code = code;
    }

    public static List<Candidate> getListFromAlliesCandidates(AlliesCandidates alliesCandidates) {
        List<Candidate> candidates = new LinkedList<>();
        for(Pair<String,CodeObj> candidate: alliesCandidates.getCandidates()){
            candidates.add(new Candidate
                    (alliesCandidates.getAllyName(), candidate.getKey(), candidate.getValue()));
        }
        return candidates;
    }

    public static List<Candidate> getListFromMissionResult(MissionResult missionResult, boolean isForAllyDisplay) {
        List<Candidate> candidates = new LinkedList<>();
        for(Pair<String,CodeObj> candidate: missionResult.getCandidates()){
            //if for ally display, column should show agent name. otherwise:
            //1. It's for uboat display meaning should show ally name in column
            //2. It's for agent display, and the ally column will be hidden.
            String entityName = isForAllyDisplay ? missionResult.getAgentName() : missionResult.getAllyName();
            candidates.add(new Candidate(entityName, candidate.getKey(), candidate.getValue()));
        }
        return candidates;
    }

    public String getAllyName() {
        return allyName;
    }

    public String getCandidate() {
        return candidate;
    }

    public CodeObj getCode() {
        return code;
    }
}
