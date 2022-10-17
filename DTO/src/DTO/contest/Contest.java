package DTO.contest;

import javafx.beans.property.*;

import java.io.Serializable;

public class Contest implements Serializable {
    private final String battleFieldName;
    private final String uBoatName;
    private final Boolean isActive;
    private final String difficulty;
    private final Integer totalRequiredTeams;
    private final Integer teamsInContest;

    public Contest(String battleFieldName, String uBoatName, Boolean isActive,
                   String difficulty, Integer totalRequiredTeams, Integer teamsInContest) {
        this.battleFieldName = battleFieldName;
        this.uBoatName = uBoatName;
        this.isActive = isActive;
        this.difficulty = difficulty;
        this.totalRequiredTeams = totalRequiredTeams;
        this.teamsInContest = teamsInContest;
    }

    public String getBattleFieldName() {
        return battleFieldName;
    }

    public String getuBoatName() {
        return uBoatName;
    }

    public Boolean getActive() {
        return isActive;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public Integer getTotalRequiredTeams() {
        return totalRequiredTeams;
    }

    public Integer getTeamsInContest() {
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
