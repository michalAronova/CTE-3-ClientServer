package alliesClient.components.agentDisplay;

import DTO.agent.SimpleAgentDTO;
import alliesClient.alliesMain.AlliesMainController;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;

import java.util.Map;
import java.util.Timer;

import static util.Constants.REFRESH_RATE;

public class AgentDisplayController {
    @FXML public VBox rootVBox;
    @FXML private TableView<SimpleAgentDTO> agentsTableView;
    @FXML private TableColumn<SimpleAgentDTO, String> nameColumn;
    @FXML private TableColumn<SimpleAgentDTO, String> threadAmountColumn;
    @FXML private TableColumn<SimpleAgentDTO, String> missionPullColumn;

    private final ObservableList<SimpleAgentDTO> dataList = FXCollections.observableArrayList();

    private AlliesMainController mainApplicationController;
    private AllAgentsRefresher allAgentsRefresher;
    private Timer allAgentsTimer;

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
        startAllAgentsRefresher();
    }

    public void addSingleAgent(SimpleAgentDTO agent){
        dataList.add(agent);
    }

    public void addMultipleAgents(Map<String, SimpleAgentDTO> agents){
        dataList.clear();
        dataList.addAll(agents.values());
    }


    private void startAllAgentsRefresher(){
        allAgentsRefresher = new AllAgentsRefresher(
                (myAgents) -> addMultipleAgents(myAgents)
        );
        allAgentsTimer = new Timer();
        allAgentsTimer.schedule(allAgentsRefresher, REFRESH_RATE, REFRESH_RATE);

    }

    public void stopRefresher() {
        if (allAgentsRefresher != null && allAgentsTimer != null) {
            allAgentsRefresher.cancel();
            allAgentsTimer.cancel();
        }
    }
}
