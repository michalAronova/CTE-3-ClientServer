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

    private StringProperty battleName;
    private StringProperty uboatName;
    private BooleanProperty isActive;
    private StringProperty activeOrIdle;
    private StringProperty difficulty;
    private IntegerProperty inGame;
    private IntegerProperty required;

    private final String noData = "---";
    private final String active = "active";
    private final String idle = "idle";

    private MainAppController mainApplicationController;

    public ContestDetailsController() {
        battleName = new SimpleStringProperty(noData);
        uboatName = new SimpleStringProperty(noData);
        isActive = new SimpleBooleanProperty(false);
        activeOrIdle = new SimpleStringProperty(noData);
        difficulty = new SimpleStringProperty(noData);
        inGame = new SimpleIntegerProperty(0);
        required = new SimpleIntegerProperty(0);
    }

    @FXML public void initialize(){
        battleNameLabel.textProperty().bind(battleName);
        uboatNameLabel.textProperty().bind(uboatName);

        isActive.addListener((observable, oldValue, newValue) -> {
            activeOrIdle.set(newValue ? active : idle);
        });
        statusLabel.textProperty().bind(activeOrIdle);

        difficultyLabel.textProperty().bind(difficulty);
        inGameLabel.textProperty().bind(Bindings.format("%d / %d", inGame, required));
    }

    public void update(Contest contest){
        battleName.set(contest.getBattleFieldName());
        uboatName.set(contest.getuBoatName());
        isActive.set(contest.getActive());
        difficulty.set(contest.getDifficulty());
        inGame.set(contest.getTeamsInContest());
        required.set(contest.getTotalRequiredTeams());
    }

    public void setMainApplicationController(MainAppController mainApplicationController){
        this.mainApplicationController = mainApplicationController;
    }
}

