package agentClient.agentApp;

import agentClient.agentMain.AgentMainController;
import clientUtils.LoginController;
import clientUtils.MainAppController;
import clientUtils.chooseNameComponent.ChooseNameComponentController;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.util.converter.IntegerStringConverter;
import javafx.util.converter.NumberStringConverter;

import java.io.IOException;
import java.net.URL;
import java.util.function.UnaryOperator;

import static util.Constants.AGENT_MAIN_FXML_RESOURCE_LOCATION;
import static util.Constants.ALLIES_MAIN_FXML_RESOURCE_LOCATION;

public class AgentAppController implements LoginController {
    @FXML private VBox vBox;
    @FXML private Label errorLabel;
    @FXML private ScrollPane mainScrollPane;
    @FXML private Slider threadSlider;
    @FXML private VBox allyRadioButtonVBox;
    @FXML private TextField missionPullTextField;
    @FXML private Button registerButton;
    private StringProperty errorMessage;
    private ToggleGroup allies;
    private AgentMainController agentMainController;
    @FXML ChooseNameComponentController chooseNameComponentController;

    public AgentAppController(){
        errorMessage = new SimpleStringProperty("");
    }

    @FXML public void initialize(){
        if(chooseNameComponentController != null){
            chooseNameComponentController.setParentController(this);
            chooseNameComponentController.hideRegisterButton();
        }
        else {
            System.out.println("choose name component is null");
        }

        errorLabel.textProperty().bind(errorMessage);

        UnaryOperator<TextFormatter.Change> integerFilter = change -> {
            String newText = change.getControlNewText();
            if (newText.matches("-?([1-9][0-9]*)?")) {
                return change;
            }
            return null;
        };

        missionPullTextField.setTextFormatter(
                new TextFormatter<>(new IntegerStringConverter(), 0, integerFilter));

        allies = new ToggleGroup();
        //go to server to retrieve all names of allies
        //add above allies to the allyRadioButtonVBox as new radio buttons
        //using method addAllyRadioButton
        //make the first of them 'selected'
    }

    private void addAllyRadioButton(String allyUsername){
        RadioButton ally = new RadioButton(allyUsername);
        ally.setToggleGroup(allies);
        ally.setUserData(allyUsername);
        allyRadioButtonVBox.getChildren().add(ally);
    }
    @FXML void onRegisterClicked(ActionEvent event) {
        String username = chooseNameComponentController.usernameChosen();
        if(username == null || username.isEmpty()){
            chooseNameComponentController.errorMessageProperty().set("Must Choose username!");
            return;
        }
        String ally = (String) allies.getSelectedToggle().getUserData();
        int threadCount = (int) threadSlider.getValue();
        try {
            int missionPull = Integer.parseInt(missionPullTextField.getText());
        }
        catch(NumberFormatException e){
            errorMessage.set("Error: mission pull must be positive integer");
        }
        //dispatch request to server to login as agent
        //upon success, switch to main -> switchToMain();
        //upon failure, change error message accordingly
    }

    private void switchToMain() {
        URL mainPageUrl = getClass().getResource(AGENT_MAIN_FXML_RESOURCE_LOCATION);
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(mainPageUrl);
            Node main = fxmlLoader.load();
            agentMainController = fxmlLoader.getController();
            agentMainController.setMainController(this);
            agentMainController.initialize();
            vBox.getChildren().clear();
            vBox.getChildren().add(main);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUsernameSelected(String username) {

    }
}
