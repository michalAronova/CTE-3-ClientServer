package battleField.client.component.global.entityChooser;

import battleField.client.component.LoginController;
import battleField.client.component.mainApp.MainAppController;
import engine.entity.EntityEnum;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import uBoatClient.UBoatClientMain;

import java.io.IOException;
import java.net.URL;

public class EntityChooserController {

    @FXML private ScrollPane scrollMain;

    @FXML private AnchorPane anchorPane;
    @FXML private ToggleGroup entityGroup;

    @FXML private RadioButton uBoatRadio;

    @FXML private RadioButton alliesRadio;

    @FXML private RadioButton agentRadio;

    @FXML private Button continueButton;

    private Node loginComponent;
    private LoginController loginController;

    private EntityEnum entity;
    private MainAppController mainAppController;


    public EntityChooserController(){

    }

    @FXML
    public void initialize(){
        uBoatRadio.setUserData(EntityEnum.UBOAT);
        alliesRadio.setUserData(EntityEnum.ALLIES);
        agentRadio.setUserData(EntityEnum.AGENT);
    }

    @FXML public void onContinue(ActionEvent event) {
        //entity = (EntityEnum) entityGroup.getSelectedToggle().getUserData();
        //mainAppController.setEntity(entity);
        mainAppController.openNewApp();
    }

    public void setMainController(MainAppController mainAppController){
        this.mainAppController = mainAppController;
    }

    public void updateUserName(String username) {
        mainAppController.usernameProperty().set(username);
    }
}


