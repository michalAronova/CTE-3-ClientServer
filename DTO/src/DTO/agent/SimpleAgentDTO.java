package DTO.agent;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class SimpleAgentDTO {
    private final StringProperty name;
    private final IntegerProperty threadCount;
    private final IntegerProperty missionPull;

    public SimpleAgentDTO(String name, int threadCount, int missionPull) {
        this.name = new SimpleStringProperty(name);
        this.threadCount = new SimpleIntegerProperty(threadCount);
        this.missionPull = new SimpleIntegerProperty(missionPull);
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
}