package DTO.missionResult;

import DTO.codeObj.CodeObj;
import javafx.util.Pair;

import java.io.Serializable;
import java.util.List;

public class MissionResult implements Serializable {
    private final List<Pair<String,CodeObj>> candidates;
    private String allyName;

    private String agentName;
    private final long time;


    public MissionResult(List<Pair<String,CodeObj>> candidates, String allyName, long time) {
        this.candidates = candidates;
        this.allyName = allyName;
        //this.agentName = agentName;
        this.time = time;
    }

    public String getAllyName() {
        return allyName;
    }

    public String getAgentName() {
        return agentName;
    }

    public List<Pair<String,CodeObj>> getCandidates() {
        return candidates;
    }

    public long getTime() {
        return time;
    }

    public void setEntityName(String entityName) {
        this.allyName = entityName;
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
