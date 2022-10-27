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

    @Override
    public String toString() {
        String winner = winnerName == null ? "NO WINNER" : winnerName;
        return "in contest: "+isCompetitionOn+" | winner: "+winner;
    }
}
