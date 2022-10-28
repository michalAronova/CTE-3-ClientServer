package clientUtils.contestDetails;

import DTO.contest.Contest;
import clientUtils.MainAppController;
import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ContestDetailsController {

    @FXML private Label battleNameLabel;
    @FXML private Label uboatNameLabel;
    @FXML private Label statusLabel;
    @FXML private Label difficultyLabel;
    @FXML private Label inGameLabel;

    private final StringProperty battleName;
    private final StringProperty uboatName;
    private final StringProperty activeOrIdle;
    private final StringProperty difficulty;
    private final IntegerProperty inGame;
    private final IntegerProperty required;

    private final String noData = "---";
    private final String active = "active";
    private final String idle = "idle";
    private MainAppController mainApplicationController;

    public ContestDetailsController() {
        battleName = new SimpleStringProperty(noData);
        uboatName = new SimpleStringProperty(noData);
        activeOrIdle = new SimpleStringProperty(noData);
        difficulty = new SimpleStringProperty(noData);
        inGame = new SimpleIntegerProperty(0);
        required = new SimpleIntegerProperty(0);
    }

    @FXML public void initialize(){
        battleNameLabel.textProperty().bind(battleName);
        uboatNameLabel.textProperty().bind(uboatName);
        statusLabel.textProperty().bind(activeOrIdle);
        difficultyLabel.textProperty().bind(difficulty);
        inGameLabel.textProperty().bind(Bindings.format("%d / %d", inGame, required));
    }

    public void update(Contest contest){
        if(contest == null) return;

        battleName.set(contest.getBattleFieldName());
        uboatName.set(contest.getuBoatName());
        activeOrIdle.set(contest.getActive() ? active : idle);
        difficulty.set(contest.getDifficulty());
        inGame.set(contest.getTeamsInContest());
        required.set(contest.getTotalRequiredTeams());
    }

    public void setMainApplicationController(MainAppController mainApplicationController){
        this.mainApplicationController = mainApplicationController;
    }

    public Label getInGameLabel() {
        return inGameLabel;
    }

    public void setInGameLabel(String data) {
        inGameLabel.setText(data);
    }

    public void setDisable(boolean disable) {

    }
    public void clear() {
        battleName.set(noData);
        uboatName.set(noData);
        activeOrIdle.set(noData);
        difficulty.set(noData);
        inGame.set(0);
        required.set(0);
    }
}

