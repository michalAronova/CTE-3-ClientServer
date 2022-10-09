package clientUtils.chooseNameComponent;

import clientUtils.LoginController;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class ChooseNameComponentController {

    @FXML public AnchorPane mainAnchor;
    private LoginController parentController;
    @FXML private TextField usernameTextField;

    @FXML private Button registerButton;

    @FXML private Label errorLabel;

    private StringProperty errorMessageProperty;

    public ChooseNameComponentController(){
        errorMessageProperty = new SimpleStringProperty("");
    }

    @FXML
    public void initialize(){
        errorLabel.textProperty().bind(errorMessageProperty);
    }

    @FXML
    public void onRegisterClicked(ActionEvent event) {
        parentController.onUsernameSelected(usernameTextField.getText());
    }

    public void setParentController(LoginController parentController) {
        this.parentController = parentController;
    }

    public void hideComponent(){
        mainAnchor.setVisible(false);
    }

    public StringProperty errorMessageProperty() {
        return errorMessageProperty;
    }

    public String usernameChosen(){
        return usernameTextField.getText();
    }

    public void hideRegisterButton(){
        registerButton.setVisible(false);
    }
}
