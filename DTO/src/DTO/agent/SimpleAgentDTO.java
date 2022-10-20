package DTO.agent;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.Serializable;

public class SimpleAgentDTO implements Serializable {
    private final String name;
    private final int threadCount;
    private final int missionPull;
    private WorkStatusDTO workStatus;

    public SimpleAgentDTO(String name, int threadCount, int missionPull) {
        this.name = name;
        this.threadCount = threadCount;
        this.missionPull = missionPull;
        this.workStatus = new WorkStatusDTO();
    }

    public SimpleAgentDTO(SimpleAgentDTO dto) {
        this.name = dto.getName();
        this.threadCount = dto.getThreadCount();
        this.missionPull = dto.getMissionPull();
    }

    public void resetWorkStatus(){
        workStatus.reset();
    }

    public String getName() {
        return name;
    }

    public int getThreadCount() {
        return threadCount;
    }

    public int getMissionPull() {
        return missionPull;
    }

    public void setWorkStatus(WorkStatusDTO workStatus){
        this.workStatus = workStatus;
    }
    public WorkStatusDTO getWorkStatus(){
        return workStatus;
    }
}
