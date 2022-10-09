package alliesClient.components.activeContests;

import DTO.contest.Contest;
import alliesClient.alliesMain.AlliesMainController;
import clientUtils.MainAppController;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.List;

public class ActiveContestsController {
    @FXML private Label errorLabel;
    @FXML private TableView<Contest> contestTableView;
    @FXML private TableColumn<Contest, String> battleFieldColumn;
    @FXML private TableColumn<Contest, String> uBoatColumn;
    @FXML private TableColumn<Contest, String> statusColumn;
    @FXML private TableColumn<Contest, String> levelColumn;
    @FXML private TableColumn<Contest, String> inGameColumn;
    @FXML private Label contestNameLabel;
    private StringProperty chosenContest;
    private StringProperty errorMessage;
    @FXML private Button readyButton;

    private final String active = "active";
    private final String awaiting = "awaiting";

    private final ObservableList<Contest> dataList = FXCollections.observableArrayList();

    private AlliesMainController mainApplicationController;

    public ActiveContestsController(){
        chosenContest = new SimpleStringProperty("");
        errorMessage = new SimpleStringProperty("");
    }

    @FXML
    public void initialize(){
        battleFieldColumn.setCellValueFactory(param ->
                new SimpleStringProperty(param.getValue().getBattleFieldName()));
        uBoatColumn.setCellValueFactory(param ->
                new SimpleStringProperty(String.valueOf(param.getValue().getuBoatName())));
        statusColumn.setCellValueFactory(param ->
                new SimpleStringProperty(param.getValue().isIsActive() ? active : awaiting));
        levelColumn.setCellValueFactory(param ->
                new SimpleStringProperty(param.getValue().getDifficulty()));
        inGameColumn.setCellValueFactory(param ->
                new SimpleStringProperty(String.format("%d / %d",
                        param.getValue().getTeamsInContest(),
                        param.getValue().getTotalRequiredTeams())));

        contestTableView.setItems(dataList);

        contestTableView.setRowFactory( tv -> {
            TableRow<Contest> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    Contest rowData = row.getItem();
                    if(!rowData.isIsActive()) {
                        chosenContest.set(rowData.getBattleFieldName());
                        errorMessage.set("");
                    }
                    else{
                        errorMessage.set("Error: can't join an active contest");
                    }
                }
            });
            return row;
        });

        contestNameLabel.textProperty().bind(chosenContest);
        errorLabel.textProperty().bind(errorMessage);
    }

    public void setMainApplicationController(AlliesMainController mainApplicationController){
        this.mainApplicationController = mainApplicationController;
    }

    public void addSingleContest(Contest contest){
        dataList.add(contest);
    }

    public void addMultipleContests(List<Contest> contests){
        dataList.addAll(contests);
    }

    @FXML public void onContestChosen(ActionEvent event) {
        //notify main application controller
        mainApplicationController.chooseContest(chosenContest.getValue());
    }

    public StringProperty errorMessageProperty() {
        return errorMessage;
    }
}

