package clientUtils.activeTeams;

import DTO.team.Team;
import clientUtils.MainAppController;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;

import java.util.List;

public class ActiveTeamsComponentController {
    @FXML
    public VBox rootVBox;
    @FXML private TableView<Team> teamsTableView;
    @FXML private TableColumn<Team, String> nameColumn;
    @FXML private TableColumn<Team, String> agentAmountColumn;
    @FXML private TableColumn<Team, String> missionSizeColumn;

    private final ObservableList<Team> dataList = FXCollections.observableArrayList();

    private MainAppController mainApplicationController;

    @FXML
    public void initialize(){
        nameColumn.setCellValueFactory(param ->
                new SimpleStringProperty(param.getValue().getName()));
        agentAmountColumn.setCellValueFactory(param ->
                new SimpleStringProperty(String.valueOf(param.getValue().getAgentAmount())));
        missionSizeColumn.setCellValueFactory(param ->
                new SimpleStringProperty(String.valueOf(param.getValue().getMissionSize())));

        teamsTableView.setItems(dataList);
    }

    public void setMainApplicationController(MainAppController mainApplicationController){
        this.mainApplicationController = mainApplicationController;
    }

    public void addSingleTeam(Team team){
        dataList.add(team);
    }

    public void addMultipleTeams(List<Team> teams){
        dataList.addAll(teams);
    }

    public void replaceTeams(List<Team> teams) {
        dataList.clear();
        dataList.addAll(teams);
    }

    public int getActiveTeamsAmount(){
        return dataList.size();
    }
    public void clear() {
        dataList.clear();
    }
}
