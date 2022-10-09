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

public class ActiveAgentsDisplayController {
    @FXML private Label encryptionLabel;
    private final StringProperty encryption;
    @FXML private TableView<ActiveAgentDTO> activeAgentsTableView;
    @FXML private TableColumn<ActiveAgentDTO, String> agentNameColumn;
    @FXML private TableColumn<ActiveAgentDTO, String> totalMissionsLabel;
    @FXML private TableColumn<ActiveAgentDTO, String> awaitingMissionsLabel;
    @FXML private TableColumn<ActiveAgentDTO, String> totalCandidates;

    private final ObservableList<ActiveAgentDTO> dataList = FXCollections.observableArrayList();

    private AlliesMainController mainApplicationController;

    public ActiveAgentsDisplayController(){
        encryption = new SimpleStringProperty("");
    }

    @FXML public void initialize(){
        encryptionLabel.textProperty().bind(encryption);

        agentNameColumn.setCellValueFactory(param ->
                new SimpleStringProperty(param.getValue().getName()));
        totalMissionsLabel.setCellValueFactory(param ->
                new SimpleStringProperty(String.valueOf(param.getValue().getTotalMissions())));
        awaitingMissionsLabel.setCellValueFactory(param ->
                new SimpleStringProperty(String.valueOf(param.getValue().getPendingMissions())));
        totalCandidates.setCellValueFactory(param ->
                new SimpleStringProperty(String.valueOf(param.getValue().getCandidatesProduced())));

        activeAgentsTableView.setItems(dataList);
    }

    public void setMainApplicationController(AlliesMainController mainApplicationController){
        this.mainApplicationController = mainApplicationController;
    }

    public void addSingleAgent(ActiveAgentDTO agent){
        dataList.add(agent);
    }

    public void addMultipleAgents(List<ActiveAgentDTO> agents){
        dataList.addAll(agents);
    }

    public String getEncryption() {
        return encryption.get();
    }

    public StringProperty encryptionProperty() {
        return encryption;
    }
}

