package alliesClient.components.activeAgentsDisplay;

import DTO.agent.SimpleAgentDTO;
import alliesClient.alliesMain.AlliesMainController;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.util.Map;
import java.util.Timer;

import static util.Constants.REFRESH_RATE;
import static util.Constants.SMALL_REFRESH_RATE;

public class ActiveAgentsDisplayController {
    @FXML private Label encryptionLabel;
    private final StringProperty encryption;
    @FXML private TableView<SimpleAgentDTO> activeAgentsTableView;
    @FXML private TableColumn<SimpleAgentDTO, String> agentNameColumn;
    @FXML private TableColumn<SimpleAgentDTO, String> totalMissionsLabel;
    @FXML private TableColumn<SimpleAgentDTO, String> awaitingMissionsLabel;
    @FXML private TableColumn<SimpleAgentDTO, String> totalCandidates;

    private final ObservableList<SimpleAgentDTO> dataList = FXCollections.observableArrayList();

    private AlliesMainController mainApplicationController;
    private ActiveAgentsRefresher activeAgentsRefresher;
    private Timer agentsTimer;

    public ActiveAgentsDisplayController(){
        encryption = new SimpleStringProperty("");
    }

    @FXML public void initialize(){
        encryptionLabel.textProperty().bind(encryption);
        agentNameColumn.setCellValueFactory(param ->
                new SimpleStringProperty(param.getValue().getName()));
        totalMissionsLabel.setCellValueFactory(param ->
                new SimpleStringProperty(String.valueOf(param.getValue()
                        .getWorkStatus()
                        .getTotalMissions())));
        awaitingMissionsLabel.setCellValueFactory(param ->
                new SimpleStringProperty(String.valueOf(param.getValue()
                        .getWorkStatus()
                        .getMissionLeftInQueue())));
        totalCandidates.setCellValueFactory(param ->
                new SimpleStringProperty(String.valueOf(param.getValue()
                        .getWorkStatus()
                        .getCandidatesProduced())));

        activeAgentsTableView.setItems(dataList);
    }

    public void setMainApplicationController(AlliesMainController mainApplicationController){
        this.mainApplicationController = mainApplicationController;
        startActiveAgentsRefresher();
    }

    public void addSingleAgent(SimpleAgentDTO agent){
        dataList.add(agent);
    }

    public void addMultipleAgents(Map<String, SimpleAgentDTO> agents){
        dataList.clear();
        dataList.addAll(agents.values());
    }

    public String getEncryption() {
        return encryption.get();
    }

    public StringProperty encryptionProperty() {
        return encryption;
    }

    private void startActiveAgentsRefresher(){
        activeAgentsRefresher = new ActiveAgentsRefresher(
                this::addMultipleAgents, mainApplicationController.getIsAllyReady());
        agentsTimer = new Timer();
        agentsTimer.schedule(activeAgentsRefresher, SMALL_REFRESH_RATE, SMALL_REFRESH_RATE);
    }

    public void stopRefresher() {
        if (activeAgentsRefresher != null && agentsTimer != null) {
            activeAgentsRefresher.cancel();
            agentsTimer.cancel();
        }
    }
}

