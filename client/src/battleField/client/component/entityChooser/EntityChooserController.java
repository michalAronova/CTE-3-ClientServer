package battleField.client.component.entityChooser;

import battleField.client.main.battleFieldClient;
import engine.entity.EntityEnum;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import uBoatClient.UBoatClientMain;

public class EntityChooserController {

    @FXML private ToggleGroup entityGroup;

    @FXML private RadioButton uBoatRadio;

    @FXML private RadioButton alliesRadio;

    @FXML private RadioButton agentRadio;

    @FXML private Button continueButton;
    private EntityEnum entity;
    private Application myApp;


    @FXML
    public void initialize(){
        uBoatRadio.setUserData(EntityEnum.UBOAT);
        alliesRadio.setUserData(EntityEnum.ALLIES);
        agentRadio.setUserData(EntityEnum.AGENT);

        entity = EntityEnum.valueOf(entityGroup.getSelectedToggle().getUserData().toString());

        entityGroup.selectedToggleProperty().addListener(
                (observable, oldValue, newValue) ->
                        entity = EntityEnum.valueOf(newValue.getUserData().toString()));
    }

    @FXML public void onContinue(ActionEvent event) {
        openNewApp();
        try{
            myApp.stop();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public void openNewApp() {
        switch(entity){
            case UBOAT:
                UBoatClientMain.launch();
                break;
            case ALLIES:
                //AlliesClientMain.launch();
                break;
            case AGENT:
                //AgentClientMain.launch();
                break;
        }
    }

    public void setMyApp(battleFieldClient app) {
        this.myApp = app;
    }
}


