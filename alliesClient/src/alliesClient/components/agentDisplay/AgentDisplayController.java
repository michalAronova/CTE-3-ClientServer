package alliesClient.components.agentDisplay;

import DTO.agent.SimpleAgentDTO;
import DTO.team.Team;
import alliesClient.alliesMain.AlliesMainController;
import clientUtils.MainAppController;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;

import java.util.List;

public class AgentDisplayController {
    @FXML public VBox rootVBox;
    @FXML private TableView<SimpleAgentDTO> agentsTableView;
    @FXML private TableColumn<SimpleAgentDTO, String> nameColumn;
    @FXML private TableColumn<SimpleAgentDTO, String> threadAmountColumn;
    @FXML private TableColumn<SimpleAgentDTO, String> missionPullColumn;

    private final ObservableList<SimpleAgentDTO> dataList = FXCollections.observableArrayList();

    private AlliesMainController mainApplicationController;

    @FXML
    public void initialize(){
        nameColumn.setCellValueFactory(param ->
                new SimpleStringProperty(param.getValue().getName()));
        threadAmountColumn.setCellValueFactory(param ->
                new SimpleStringProperty(String.valueOf(param.getValue().getThreadCount())));
        missionPullColumn.setCellValueFactory(param ->
                new SimpleStringProperty(String.valueOf(param.getValue().getMissionPull())));

        agentsTableView.setItems(dataList);
    }

    public void setMainApplicationController(AlliesMainController mainApplicationController){
        this.mainApplicationController = mainApplicationController;
    }

    public void addSingleAgent(SimpleAgentDTO agent){
        dataList.add(agent);
    }

    public void addMultipleAgents(List<SimpleAgentDTO> agents){
        dataList.addAll(agents);
    }
}
