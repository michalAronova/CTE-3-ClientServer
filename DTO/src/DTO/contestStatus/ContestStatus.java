package DTO.contestStatus;


public class ContestStatus {
    private final boolean isCompetitionOn;
    private final String winnerName;

    public ContestStatus(boolean isCompetitionOn, String winnerName) {
        this.isCompetitionOn = isCompetitionOn;
        this.winnerName = winnerName;
    }

    public boolean isCompetitionOn() {
        return isCompetitionOn;
    }

    public String getWinnerName() {
        return winnerName;
    }
}
