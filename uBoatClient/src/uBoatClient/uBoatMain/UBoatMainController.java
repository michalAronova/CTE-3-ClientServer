package uBoatClient.uBoatMain;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import uBoatClient.uBoatApp.UBoatAppController;

public class UBoatMainController {

    @FXML private Tab setupTab;
    @FXML private Button readyButton;
    @FXML private Tab viewTab;
    @FXML private Label inputLabel;
    @FXML private Label outputLabel;
    @FXML private Button logoutButton;
    private UBoatAppController uBoatAppController;


    @FXML
    public void onLogoutClicked(ActionEvent event) {

    }
    @FXML
    public void onReadyClicked(ActionEvent event) {

    }

    public void setMainController(UBoatAppController uBoatAppController) {
        this.uBoatAppController = uBoatAppController;
    }
}

