package DTO.missionResult;

import DTO.codeObj.CodeObj;
import javafx.util.Pair;

import java.util.List;

public class MissionResult {
    private List<Pair<String,CodeObj>> candidates;
    private String agentID;
    private long time;


    public MissionResult(List<Pair<String,CodeObj>> candidates, String agentID, long time) {
        this.candidates = candidates;
        this.agentID = agentID;
        this.time = time;
    }
    public String getAgentID() {
        return agentID;
    }

    public List<Pair<String,CodeObj>> getCandidates() {
        return candidates;
    }

    public long getTime() {
        return time;
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("Time taken: "+ time);
        sb.append(System.lineSeparator());
        sb.append(candidates);
        return sb.toString();
    }
}
