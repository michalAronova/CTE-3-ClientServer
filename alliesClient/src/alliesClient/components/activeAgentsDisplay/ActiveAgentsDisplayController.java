package alliesClient.components.activeAgentsDisplay;

import DTO.agent.ActiveAgentDTO;
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

import java.util.List;
import java.util.Map;

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
    }

    public void addSingleAgent(SimpleAgentDTO agent){
        dataList.add(agent);
    }

    public void addMultipleAgents(Map<String, SimpleAgentDTO> agents){
        dataList.addAll(agents.values());
    }

    public String getEncryption() {
        return encryption.get();
    }

    public StringProperty encryptionProperty() {
        return encryption;
    }
}

