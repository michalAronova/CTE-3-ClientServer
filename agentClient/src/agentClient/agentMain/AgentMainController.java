package agentClient.agentMain;

import DTO.missionResult.MissionResult;
import agentClient.agentApp.AgentAppController;
import agentClient.agentLogic.Agent;
import agentClient.refreshers.AllyApprovedRefresher;
import agentClient.refreshers.CheckForContestRefresher;
import agentClient.refreshers.CheckForFinishRefresher;
import agentClient.refreshers.StatusUpdater;
import clientUtils.MainAppController;
import clientUtils.candidatesComponent.CandidatesComponentController;
import clientUtils.chat.chatroom.ChatRoomMainController;
import clientUtils.contestDetails.ContestDetailsController;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.layout.AnchorPane;

import java.io.Closeable;
import java.io.IOException;
import java.net.URL;
import java.util.Timer;

import static util.Constants.*;

public class AgentMainController implements MainAppController, Closeable {
    @FXML private Tab chatTab;
    @FXML private AnchorPane chatPanel;
    @FXML private Label usernameLabel;
    @FXML private Label alliesNameLabel;
    @FXML private Label totalCandidatesLabel;
    @FXML private Label inQueueLabel;
    @FXML private Label pulledLabel;
    @FXML private Label CompletedLabel;
    @FXML private ContestDetailsController contestDetailsController;
    @FXML private CandidatesComponentController candidatesComponentController;

    private Parent chatRoomComponent;
    private ChatRoomMainController chatRoomComponentController;
    private AgentAppController agentAppController;

    //properties
    private final StringProperty alliesName;
    private final IntegerProperty totalCandidates;
    private final IntegerProperty inQueue;
    private final IntegerProperty pulled;
    private final IntegerProperty completed;
    private final BooleanProperty finished;
    private final BooleanProperty inContest;
    private final BooleanProperty inWaitingList;
    private final BooleanProperty waitForAllyApproval;
    //refreshers and timers
    private CheckForContestRefresher checkForContestRefresher;
    private CheckForFinishRefresher checkForFinishRefresher;
    private AllyApprovedRefresher allyApprovedRefresher;
    private StatusUpdater statusUpdater;
    private Timer statusUpdateTimer;
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
        inWaitingList = new SimpleBooleanProperty();
    }

    public void createAgent(String username, String myAllies, int threadCount, int missionAmountPull){
        agent = new Agent(username, myAllies, threadCount, missionAmountPull);
        agent.isCompetitionOnProperty().bind(inContest);
        agent.setUpdateUICandidates(this::updateCandidates);
        connectDataToAgent();
    }

    private void connectDataToAgent() {
        inQueue.bind(agent.missionsInQueueProperty());
        pulled.bind(agent.totalMissionsPulledProperty());
        completed.bind(agent.missionsDoneProperty());
    }

    private void updateCandidates(MissionResult missionResult) {
        Platform.runLater(() ->
                candidatesComponentController
                .addMissionResult(missionResult, false));
    }

    public void setMainController(AgentAppController agentAppController) {
        this.agentAppController = agentAppController;
        this.usernameLabel.textProperty().bind(agentAppController.usernameProperty());
        this.inWaitingList.set(agentAppController.getInWaitingList());
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
        loadChatRoomPage();
    }
    private void loadChatRoomPage() {
        URL loginPageUrl = getClass().getResource(CHAT_ROOM_FXML_RESOURCE_LOCATION);
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(loginPageUrl);
            chatRoomComponent = fxmlLoader.load();
            chatRoomComponentController = fxmlLoader.getController();
            chatRoomComponentController.setChatAppMainController(this);
            setChatTab(chatRoomComponent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void setChatTab(Parent pane) {
        chatPanel.getChildren().clear();
        chatPanel.getChildren().add(pane);
        AnchorPane.setBottomAnchor(pane, 1.0);
        AnchorPane.setTopAnchor(pane, 1.0);
        AnchorPane.setLeftAnchor(pane, 1.0);
        AnchorPane.setRightAnchor(pane, 1.0);
    }

    private void startRefreshers() {
        startCheckForContestRefresher();
        startCheckForFinishRefresher();
        startAllyApprovedRefresher();
        startStatusUpdater();
    }

    private void startStatusUpdater() {
        statusUpdater = new StatusUpdater(totalCandidates, inQueue, completed, inWaitingList);
        statusUpdateTimer = new Timer();
        statusUpdateTimer.schedule(statusUpdater, TINY_REFRESH_RATE, TINY_REFRESH_RATE);
    }

    public void startCheckForContestRefresher(){
        checkForContestRefresher = new CheckForContestRefresher(inContest,
                inWaitingList, ((contest) -> contestDetailsController.update(contest)),
                ((dmInfo) -> agent.updateByContest(dmInfo.getKeys(), dmInfo.getDictionary())));

        checkForContestTimer = new Timer();
        checkForContestTimer.schedule(checkForContestRefresher, REFRESH_RATE, REFRESH_RATE);
    }

    private void startCheckForFinishRefresher() {
        checkForFinishRefresher = new CheckForFinishRefresher(inContest, waitForAllyApproval, inWaitingList);
        checkForFinishTimer = new Timer();
        checkForFinishTimer.schedule(checkForFinishRefresher, REFRESH_RATE, REFRESH_RATE);
    }

    private void startAllyApprovedRefresher(){
        allyApprovedRefresher = new AllyApprovedRefresher(waitForAllyApproval,
                                        this::handleAllyOKClicked, inWaitingList);
        allyApprovedTimer = new Timer();
        allyApprovedTimer.schedule(allyApprovedRefresher, REFRESH_RATE, REFRESH_RATE);
    }

    private void handleAllyOKClicked() {
        inWaitingList.set(false);
        candidatesComponentController.clear();
        agent.clearData();
        contestDetailsController.clear();
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
        if (allyApprovedRefresher != null && allyApprovedTimer != null) {
            allyApprovedRefresher.cancel();
            allyApprovedTimer.cancel();
        }
        if (checkForContestRefresher != null && checkForContestTimer != null) {
            checkForContestRefresher.cancel();
            checkForContestTimer.cancel();
        }
        if (checkForFinishRefresher != null && checkForFinishTimer != null) {
            checkForFinishRefresher.cancel();
            checkForFinishTimer.cancel();
        }
        if (statusUpdater != null && statusUpdateTimer != null) {
            statusUpdater.cancel();
            statusUpdateTimer.cancel();
        }

        chatRoomComponentController.close();
    }


    @Override
    public void updateCandidateAmount(int size) {
        totalCandidates.set(size);
    }
}
