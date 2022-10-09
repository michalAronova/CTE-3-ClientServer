package agentClient.agentMain;

import agentClient.agentApp.AgentAppController;
import clientUtils.MainAppController;
import clientUtils.candidatesComponent.CandidatesComponentController;
import clientUtils.contestDetails.ContestDetailsController;
import javafx.beans.property.*;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class AgentMainController implements MainAppController {
    @FXML private Label alliesNameLabel;
    @FXML private Label totalCandidatesLabel;
    @FXML private Label inQueueLabel;
    @FXML private Label pulledLabel;
    @FXML private Label CompletedLabel;
    @FXML private ContestDetailsController contestDetailsController;
    @FXML private CandidatesComponentController candidatesComponentController;

    private AgentAppController agentAppController;

    private final StringProperty alliesName;
    private final IntegerProperty totalCandidates;
    private final IntegerProperty inQueue;
    private final IntegerProperty pulled;
    private final IntegerProperty completed;
    private final BooleanProperty finished;
    private final BooleanProperty inContest;

    public AgentMainController(){
        alliesName = new SimpleStringProperty("");
        totalCandidates = new SimpleIntegerProperty(0);
        inQueue = new SimpleIntegerProperty(0);
        pulled = new SimpleIntegerProperty(0);
        completed = new SimpleIntegerProperty(0);
        finished = new SimpleBooleanProperty(false);
        inContest = new SimpleBooleanProperty(false);
    }

    public void setMainController(AgentAppController agentAppController) {
        this.agentAppController = agentAppController;
    }

    @FXML
    public void initialize() {
        if(contestDetailsController != null && candidatesComponentController != null){
            contestDetailsController.setMainApplicationController(this);
            candidatesComponentController.setMainApplicationController(this);
            candidatesComponentController.hideAllyColumn();
        }
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
}
