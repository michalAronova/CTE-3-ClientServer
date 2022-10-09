package uBoatClient.uBoatLogin;

import clientUtils.LoginController;
import clientUtils.MainAppController;
import clientUtils.chooseNameComponent.*;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import util.Constants;
import util.http.HttpClientUtil;

import java.io.File;
import java.io.IOException;

public class UBoatLoginController implements LoginController {
    @FXML private ScrollPane mainScroll;
    @FXML private Button loadXMLButton;
    @FXML private TextField loadXMLTextField;
    @FXML private Label xmlErrorLabel;
    @FXML private Button registerButton;
    private MainAppController mainController;

    @FXML private Pane chooseNameComponent;
    @FXML private ChooseNameComponentController chooseNameComponentController;

    private BooleanProperty usernameValidProperty;

    private StringProperty xmlErrorMessageProperty;

    private File selectedFile;

    private BooleanProperty isFileSelectedProperty;

    private StringProperty fileName;

    public UBoatLoginController(){
        usernameValidProperty = new SimpleBooleanProperty(false);
        xmlErrorMessageProperty = new SimpleStringProperty("");
        isFileSelectedProperty = new SimpleBooleanProperty(false);
        fileName = new SimpleStringProperty("");
    }

    @FXML
    public void initialize(){
        if(chooseNameComponentController != null) {
            chooseNameComponentController.setParentController(this);
            chooseNameComponentController.initialize();
        }
        else{
            System.out.println("null choose name controller");
        }

        loadXMLButton.visibleProperty().bind(usernameValidProperty);
        loadXMLTextField.visibleProperty().bind(usernameValidProperty);
        xmlErrorLabel.visibleProperty().bind(usernameValidProperty);
        registerButton.visibleProperty().bind(usernameValidProperty);

        registerButton.disableProperty().bind(isFileSelectedProperty.not());

        usernameValidProperty.addListener((observable, oldValue, newValue) -> {
            if(newValue){
                chooseNameComponentController.hideComponent();
            }
        });

        xmlErrorLabel.textProperty().bind(xmlErrorMessageProperty);
        loadXMLTextField.setEditable(false);
        loadXMLTextField.textProperty().bind(fileName);
    }

    public void setMainController(MainAppController mainController) {
        this.mainController = mainController;
    }

    @Override
    public void onUsernameSelected(String username) {

    }

    public boolean isUsernameValidProperty() {
        return usernameValidProperty.get();
    }

    public BooleanProperty usernameValidPropertyProperty() {
        return usernameValidProperty;
    }

    public String getXmlErrorMessageProperty() {
        return xmlErrorMessageProperty.get();
    }

    public StringProperty xmlErrorMessagePropertyProperty() {
        return xmlErrorMessageProperty;
    }

    @FXML
    public void onLoadClicked(ActionEvent actionEvent) {


    }

    public void onRegisterClicked(ActionEvent actionEvent) {
        //mainController.onRegistered();
    }
}
