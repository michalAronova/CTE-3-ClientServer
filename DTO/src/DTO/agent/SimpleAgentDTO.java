package DTO.agent;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.Serializable;

public class SimpleAgentDTO implements Serializable {
    private final StringProperty name;
    private final IntegerProperty threadCount;
    private final IntegerProperty missionPull;
    private WorkStatusDTO workStatus;

    public SimpleAgentDTO(String name, int threadCount, int missionPull) {
        this.name = new SimpleStringProperty(name);
        this.threadCount = new SimpleIntegerProperty(threadCount);
        this.missionPull = new SimpleIntegerProperty(missionPull);
        this.workStatus = new WorkStatusDTO();
    }

    public SimpleAgentDTO(SimpleAgentDTO dto) {
        this.name = new SimpleStringProperty(dto.getName());
        this.threadCount = new SimpleIntegerProperty(dto.getThreadCount());
        this.missionPull = new SimpleIntegerProperty(dto.getMissionPull());
    }

    public void resetWorkStatus(){
        workStatus.reset();
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public int getThreadCount() {
        return threadCount.get();
    }

    public IntegerProperty threadCountProperty() {
        return threadCount;
    }

    public int getMissionPull() {
        return missionPull.get();
    }

    public IntegerProperty missionPullProperty() {
        return missionPull;
    }
    public void setWorkStatus(WorkStatusDTO workStatus){
        this.workStatus = workStatus;
    }
    public WorkStatusDTO getWorkStatus(){
        return workStatus;
    }
}
