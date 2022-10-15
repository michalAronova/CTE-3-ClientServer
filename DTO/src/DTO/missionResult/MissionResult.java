package DTO.missionResult;

import DTO.codeObj.CodeObj;
import javafx.util.Pair;

import java.io.Serializable;
import java.util.List;

public class MissionResult implements Serializable {
    private final List<Pair<String,CodeObj>> candidates;
    private final String allyName;
    private final String agentName;


    public MissionResult(List<Pair<String, CodeObj>> candidates, String agentName, String allyName) {
        this.candidates = candidates;
        this.agentName = agentName;
        this.allyName = allyName;
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

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(System.lineSeparator());
        sb.append(candidates);
        sb.append(System.lineSeparator());
        sb.append("ally name ").append(allyName);
        sb.append(System.lineSeparator());
        sb.append("agent name ").append(agentName);
        return sb.toString();
    }
}
