package DTO.agent;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.Serializable;

public class ActiveAgentDTO implements Serializable {
    private final StringProperty name;
    private final IntegerProperty totalMissions;
    private final IntegerProperty pendingMissions;
    private final IntegerProperty candidatesProduced;

    public ActiveAgentDTO(String name, int totalMissions,
                          int pendingMissions, int candidatesProduced) {
        this.name = new SimpleStringProperty(name);
        this.totalMissions = new SimpleIntegerProperty(totalMissions);
        this.pendingMissions = new SimpleIntegerProperty(pendingMissions);
        this.candidatesProduced = new SimpleIntegerProperty(candidatesProduced);
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public int getTotalMissions() {
        return totalMissions.get();
    }

    public IntegerProperty totalMissionsProperty() {
        return totalMissions;
    }

    public int getPendingMissions() {
        return pendingMissions.get();
    }

    public IntegerProperty pendingMissionsProperty() {
        return pendingMissions;
    }

    public int getCandidatesProduced() {
        return candidatesProduced.get();
    }

    public IntegerProperty candidatesProducedProperty() {
        return candidatesProduced;
    }
}
