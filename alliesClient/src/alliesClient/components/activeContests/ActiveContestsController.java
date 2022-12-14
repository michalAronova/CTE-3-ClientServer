package alliesClient.components.activeContests;

import DTO.contest.Contest;
import alliesClient.alliesMain.AlliesMainController;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;
import java.util.Timer;

import static util.Constants.REFRESH_RATE;

public class ActiveContestsController implements Closeable {
    @FXML public VBox rootVBox;
    @FXML private Label errorLabel;
    @FXML private TableView<Contest> contestTableView;
    @FXML private TableColumn<Contest, String> battleFieldColumn;
    @FXML private TableColumn<Contest, String> uBoatColumn;
    @FXML private TableColumn<Contest, String> statusColumn;
    @FXML private TableColumn<Contest, String> levelColumn;
    @FXML private TableColumn<Contest, String> inGameColumn;
    @FXML private Label contestNameLabel;
    private StringProperty chosenContestName;
    private StringProperty errorMessage;
    @FXML private Button readyButton;

    private final String active = "active";
    private final String awaiting = "awaiting";

    private final ObservableList<Contest> dataList = FXCollections.observableArrayList();

    private AlliesMainController mainApplicationController;
    private Timer timer;
    private ContestListRefresher refresher;
    private BooleanProperty disabledProperty;

    public ActiveContestsController(){
        chosenContestName = new SimpleStringProperty("");
        errorMessage = new SimpleStringProperty("");
        disabledProperty = new SimpleBooleanProperty(false);
    }

    @FXML
    public void initialize(){
        rootVBox.disableProperty().bind(disabledProperty);
        battleFieldColumn.setCellValueFactory(param ->
                new SimpleStringProperty(param.getValue().getBattleFieldName()));
        uBoatColumn.setCellValueFactory(param ->
                new SimpleStringProperty(String.valueOf(param.getValue().getuBoatName())));
        statusColumn.setCellValueFactory(param ->
                new SimpleStringProperty(param.getValue().getActive() ? active : awaiting));
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
                    if(!rowData.getActive()) {
                        chosenContestName.set(rowData.getuBoatName());
                        errorMessage.set("");
                    }
                    else if(rowData.getTotalRequiredTeams().equals(rowData.getTeamsInContest())){
                        errorMessage.set("Error: this contest is full");
                    }
                    else{
                        errorMessage.set("Error: can't join an active contest");
                    }
                }
            });
            return row;
        });

        contestNameLabel.textProperty().bind(chosenContestName);
        errorLabel.textProperty().bind(errorMessage);
    }

    public void setMainApplicationController(AlliesMainController mainApplicationController){
        this.mainApplicationController = mainApplicationController;
        startContestsListRefresher();
    }

    public void addSingleContest(Contest contest){
        dataList.add(contest);
    }

    public void addMultipleContests(List<Contest> contests){
        dataList.addAll(contests);
    }

    @FXML public void onContestChosen(ActionEvent event) {
        //notify main application controller
        mainApplicationController.chooseContest(chosenContestName.getValue());
    }

    public StringProperty errorMessageProperty() {
        return errorMessage;
    }

    public void replaceAllContestsList(List<Contest> contests){
        dataList.clear();
        dataList.addAll(contests);
        if(mainApplicationController.getChosenContest() != null){
            for (Contest contest : contests) {
                if (contest.getBattleFieldName().equals(mainApplicationController.getChosenContest().getBattleFieldName())) {
                    mainApplicationController.updateChosenContestDisplay(contest);
                }
            }
        }
    }

    public void startContestsListRefresher() {
        refresher = new ContestListRefresher(this::replaceAllContestsList, mainApplicationController.isCompetitionOnProperty());
        timer = new Timer();
        timer.schedule(refresher, REFRESH_RATE, REFRESH_RATE);
    }

    @Override
    public void close() throws IOException {
        dataList.clear();
        if (refresher != null && timer != null) {
            refresher.cancel();
            timer.cancel();
        }
    }

    public BooleanProperty disableProperty() {
        return disabledProperty;
    }

    public void clearChoice(){
        chosenContestName.set("");
    }

    public void stopRefresher() {
        if (refresher != null && timer != null) {
            refresher.cancel();
            timer.cancel();
        }
    }
}

