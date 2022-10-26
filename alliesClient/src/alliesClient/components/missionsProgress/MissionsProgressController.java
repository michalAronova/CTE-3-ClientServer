package alliesClient.components.missionsProgress;

import DTO.dmProgress.DMProgress;
import alliesClient.alliesMain.AlliesMainController;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.util.Timer;

import static util.Constants.REFRESH_RATE;

public class MissionsProgressController {
    @FXML private TableView<DMProgress> missionDataTableView;
    @FXML private TableColumn<DMProgress, String> totalColumn;
    @FXML private TableColumn<DMProgress, String> producedColumn;
    @FXML private TableColumn<DMProgress, String> completedColumn;
    private DMProgress progressDTO;
    private final ObservableList<DMProgress> dataList = FXCollections.observableArrayList();
    private AlliesMainController mainApplicationController;
    private Timer missionProgressTimer;
    private MissionsProgressRefresher missionProgressRefresher;


    @FXML public void initialize(){
        totalColumn.setCellValueFactory(param ->
                new SimpleStringProperty(String.valueOf(param.getValue().getTotal())));
        producedColumn.setCellValueFactory(param ->
                new SimpleStringProperty(String.valueOf(param.getValue().getProduced())));
        completedColumn.setCellValueFactory(param ->
                new SimpleStringProperty(String.valueOf(param.getValue().getCompleted())));

        missionDataTableView.setItems(dataList);
    }

    public void setDTO(DMProgress progressDTO){
        this.progressDTO = progressDTO;
        dataList.clear();
        dataList.add(progressDTO);
    }

    public DMProgress getProgressDTO() {
        return progressDTO;
    }

    public void setMainApplicationController(AlliesMainController mainApplicationController){
        this.mainApplicationController = mainApplicationController;
        //startMissionProgressRefresher();
    }

    public void startMissionProgressRefresher() {
        missionProgressRefresher = new MissionsProgressRefresher(this::setDTO, mainApplicationController.isCompetitionOnProperty());
        missionProgressTimer = new Timer();
        missionProgressTimer.schedule(missionProgressRefresher, REFRESH_RATE, REFRESH_RATE);
    }
}

