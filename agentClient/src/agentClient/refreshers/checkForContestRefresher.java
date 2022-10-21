package agentClient.refreshers;

import DTO.contest.Contest;
import javafx.beans.property.BooleanProperty;

import java.util.TimerTask;
import java.util.function.Consumer;

public class checkForContestRefresher extends TimerTask {

    private final BooleanProperty inContest;
    private final Consumer<Contest> updateContestDetails;

    public checkForContestRefresher(BooleanProperty inContest, Consumer<Contest> updateContestDetails) {
        this.inContest = inContest;
        this.updateContestDetails = updateContestDetails;
    }

    @Override
    public void run() {

    }
}
