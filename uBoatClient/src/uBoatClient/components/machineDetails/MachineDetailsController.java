package uBoatClient.components.machineDetails;

import DTO.MachineDetails;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;
import uBoatClient.uBoatMain.UBoatMainController;

public class MachineDetailsController {

    @FXML public GridPane machineDetails;
    @FXML private Label battleFieldLabel;
    @FXML private Label requiredTeamsLabel;
    @FXML private Label difficultyLabel;
    @FXML private Label rotorInUseData;
    @FXML private Label totalRotorsData;
    @FXML private Label totalReflectorsData;
    @FXML private Label machineDetailsTitleLabel;
    private UBoatMainController mainApplicationController;

    @FXML
    public void initialize(){

    }

    public void setMainApplicationController(UBoatMainController mainApplicationController) {
        this.mainApplicationController = mainApplicationController;
    }

    public void fileLoaded(MachineDetails machineDetails){
        rotorInUseData.setText(String.format("%d",machineDetails.getRotorsInUse()));
        totalRotorsData.setText(String.format("%d",machineDetails.getTotalRotors()));
        totalReflectorsData.setText(String.format("%d",machineDetails.getReflectorsCount()));
        battleFieldLabel.setText(machineDetails.getBattleField());
        requiredTeamsLabel.setText(String.format("%d",machineDetails.getRequiredTeams()));
        difficultyLabel.setText(machineDetails.getDifficulty());
    }

}
