package DTO.contest;

import javafx.beans.property.*;

public class Contest {
    private StringProperty battleFieldName;
    private StringProperty uBoatName;
    private BooleanProperty isActive;
    private StringProperty difficulty;
    private IntegerProperty totalRequiredTeams;
    private IntegerProperty teamsInContest;

    public Contest(String battleFieldName, String uBoatName,
                   boolean isActive, String difficulty,
                   int totalRequiredTeams, int teamsInContest) {
        this.battleFieldName = new SimpleStringProperty(battleFieldName);
        this.uBoatName = new SimpleStringProperty(uBoatName);
        this.isActive = new SimpleBooleanProperty(isActive);
        this.difficulty = new SimpleStringProperty(difficulty);
        this.totalRequiredTeams = new SimpleIntegerProperty(totalRequiredTeams);
        this.teamsInContest = new SimpleIntegerProperty(teamsInContest);
    }

    public String getBattleFieldName() {
        return battleFieldName.get();
    }

    public StringProperty battleFieldNameProperty() {
        return battleFieldName;
    }

    public String getuBoatName() {
        return uBoatName.get();
    }

    public StringProperty uBoatNameProperty() {
        return uBoatName;
    }

    public boolean isIsActive() {
        return isActive.get();
    }

    public BooleanProperty isActiveProperty() {
        return isActive;
    }

    public String getDifficulty() {
        return difficulty.get();
    }

    public StringProperty difficultyProperty() {
        return difficulty;
    }

    public int getTotalRequiredTeams() {
        return totalRequiredTeams.get();
    }

    public IntegerProperty totalRequiredTeamsProperty() {
        return totalRequiredTeams;
    }

    public int getTeamsInContest() {
        return teamsInContest.get();
    }

    public IntegerProperty teamsInContestProperty() {
        return teamsInContest;
    }

    @Override
    public String toString() {
        return "Contest{" +
                "battleFieldName=" + battleFieldName +
                ", uBoatName=" + uBoatName +
                ", isActive=" + isActive +
                ", difficulty=" + difficulty +
                ", totalRequiredTeams=" + totalRequiredTeams +
                ", teamsInContest=" + teamsInContest +
                '}';
    }
}
