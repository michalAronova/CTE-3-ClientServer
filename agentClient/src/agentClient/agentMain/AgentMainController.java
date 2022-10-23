package agentClient.agentMain;

import agentClient.agentApp.AgentAppController;
import agentClient.agentLogic.Agent;
import agentClient.refreshers.AllyApprovedRefresher;
import agentClient.refreshers.CheckForContestRefresher;
import agentClient.refreshers.CheckForFinishRefresher;
import clientUtils.MainAppController;
import clientUtils.candidatesComponent.CandidatesComponentController;
import clientUtils.contestDetails.ContestDetailsController;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.io.Closeable;
import java.util.Timer;

import static util.Constants.REFRESH_RATE;

public class AgentMainController implements MainAppController, Closeable {
    @FXML private Label usernameLabel;
    @FXML private Label alliesNameLabel;
    @FXML private Label totalCandidatesLabel;
    @FXML private Label inQueueLabel;
    @FXML private Label pulledLabel;
    @FXML private Label CompletedLabel;
    @FXML private ContestDetailsController contestDetailsController;
    @FXML private CandidatesComponentController candidatesComponentController;

    private AgentAppController agentAppController;

    //properties
    private final StringProperty alliesName;
    private final IntegerProperty totalCandidates;
    private final IntegerProperty inQueue;
    private final IntegerProperty pulled;
    private final IntegerProperty completed;
    private final BooleanProperty finished;
    private final BooleanProperty inContest;
    private final BooleanProperty waitForAllyApproval;
    //refreshers and timers
    CheckForContestRefresher checkForContestRefresher;
    CheckForFinishRefresher checkForFinishRefresher;
    AllyApprovedRefresher allyApprovedRefresher;
    private Timer checkForContestTimer;
    private Timer checkForFinishTimer;
    private Timer allyApprovedTimer;

    //Agent object
    private Agent agent;

    public AgentMainController(){
        alliesName = new SimpleStringProperty("");
        totalCandidates = new SimpleIntegerProperty(0);
        inQueue = new SimpleIntegerProperty(0);
        pulled = new SimpleIntegerProperty(0);
        completed = new SimpleIntegerProperty(0);
        finished = new SimpleBooleanProperty(false);
        inContest = new SimpleBooleanProperty(false);
        waitForAllyApproval = new SimpleBooleanProperty(false);
    }

    public void createAgent(String username, String myAllies, int threadCount, int missionAmountPull){
        agent = new Agent(username, myAllies, threadCount, missionAmountPull);
    }

    public void setMainController(AgentAppController agentAppController) {
        this.agentAppController = agentAppController;
        this.usernameLabel.textProperty().bind(agentAppController.usernameProperty());
    }

    @FXML
    public void initialize() {
        if(contestDetailsController != null && candidatesComponentController != null){
            contestDetailsController.setMainApplicationController(this);
            candidatesComponentController.setMainApplicationController(this);
            candidatesComponentController.hideAllyColumn();
        }
        alliesNameLabel.textProperty().bind(alliesName);
        totalCandidatesLabel.textProperty().bind(Bindings.format("%d", totalCandidates));
        inQueueLabel.textProperty().bind(Bindings.format("%d", inQueue));
        pulledLabel.textProperty().bind(Bindings.format("%d", pulled));
        CompletedLabel.textProperty().bind(Bindings.format("%d", completed));

        startRefreshers();
        //do something with finished and in contest properties ?
    }

    private void startRefreshers() {
        startCheckForContestRefresher();
        startCheckForFinishRefresher();
        startAllyApprovedRefresher();
    }

    public void startCheckForContestRefresher(){
//        checkForContestRefresher = new CheckForContestRefresher(inContest, ((contest) -> contestDetailsController.update(contest)),
//                this::updateDMInfo);
        checkForContestRefresher = new CheckForContestRefresher(inContest, ((contest) -> contestDetailsController.update(contest)),
                ((dmInfo) -> {
                    agent.updateByContest(dmInfo.getKeys(), dmInfo.getDictionary());
                }));
        checkForContestTimer = new Timer();
        checkForContestTimer.schedule(checkForContestRefresher, REFRESH_RATE, REFRESH_RATE);
    }

    private void startCheckForFinishRefresher() {
        checkForFinishRefresher = new CheckForFinishRefresher(inContest, waitForAllyApproval);
        checkForFinishTimer = new Timer();
        checkForFinishTimer.schedule(checkForFinishRefresher, REFRESH_RATE, REFRESH_RATE);
    }

    private void startAllyApprovedRefresher(){
        allyApprovedRefresher = new AllyApprovedRefresher(waitForAllyApproval, this::handleAllyOKClicked);
        allyApprovedTimer = new Timer();
        allyApprovedTimer.schedule(allyApprovedRefresher, REFRESH_RATE, REFRESH_RATE);
    }

    private void handleAllyOKClicked() {
        //TODO
        //intiate UI
    }

    public String getAlliesName() {
        return alliesName.get();
    }

    public StringProperty alliesNameProperty() {
        return alliesName;
    }

    public int getTotalCandidates() {
        return totalCandidates.get();
    }

    public IntegerProperty totalCandidatesProperty() {
        return totalCandidates;
    }

    public int getInQueue() {
        return inQueue.get();
    }

    public IntegerProperty inQueueProperty() {
        return inQueue;
    }

    public int getPulled() {
        return pulled.get();
    }

    public IntegerProperty pulledProperty() {
        return pulled;
    }

    public int getCompleted() {
        return completed.get();
    }

    public IntegerProperty completedProperty() {
        return completed;
    }

    public boolean isInContest() {
        return inContest.get();
    }

    public BooleanProperty inContestProperty() {
        return inContest;
    }

    public boolean isFinished() {
        return finished.get();
    }

    public BooleanProperty finishedProperty() {
        return finished;
    }

    @Override
    public void close() {
        //stop all refreshers here
    }


}
