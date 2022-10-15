package DTO.agent;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class SimpleAgentDTO {
    private final StringProperty name;
    private final IntegerProperty threadCount;
    private final IntegerProperty missionPull;
    private int missionDone = 0;

    public SimpleAgentDTO(String name, int threadCount, int missionPull) {
        this.name = new SimpleStringProperty(name);
        this.threadCount = new SimpleIntegerProperty(threadCount);
        this.missionPull = new SimpleIntegerProperty(missionPull);
    }

    public SimpleAgentDTO(SimpleAgentDTO dto) {
        this.name = new SimpleStringProperty(dto.getName());
        this.threadCount = new SimpleIntegerProperty(dto.getThreadCount());
        this.missionPull = new SimpleIntegerProperty(dto.getMissionPull());
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

    public int getMissionDone() {
        return missionDone;
    }

    public void setMissionDone(int missionDone) {
        this.missionDone = missionDone;
    }
}
