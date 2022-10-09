package uBoatClient.uBoatMain;

import DTO.codeObj.CodeObj;
import clientUtils.MainAppController;
import clientUtils.activeTeams.ActiveTeamsComponentController;
import clientUtils.candidatesComponent.CandidatesComponentController;
import uBoatClient.components.codeConfigurationComponent.CodeConfigComponentController;
import uBoatClient.components.codeObjDisplayComponent.CodeObjDisplayComponentController;
import uBoatClient.components.dictionaryComponent.DictionaryComponentController;
import uBoatClient.components.machineDetails.MachineDetailsController;
import uBoatClient.components.processComponent.ProcessComponentController;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import uBoatClient.uBoatApp.UBoatAppController;
import util.Constants;
import util.http.HttpClientUtil;

import java.io.File;
import java.io.IOException;

public class UBoatMainController implements MainAppController{
    @FXML private Button readyButton;
    @FXML private VBox mainVBox;
    @FXML private Label usernameLabel;
    @FXML private Label entityLabel;
    @FXML private Button loadFileButton;
    @FXML private TextField fileTextField;
    @FXML private Label xmlErrorLabel;
    @FXML private TabPane competitionTabPane;
    @FXML private Tab machineTab;
    @FXML private Tab contestTab;
    @FXML private Button logoutButton;
    @FXML private CodeObjDisplayComponentController codeObjDisplayController;
    @FXML private ProcessComponentController processComponentController;
    @FXML private DictionaryComponentController dictionaryComponentController;
    @FXML private MachineDetailsController machineDetailsController;
    @FXML private CodeConfigComponentController codeConfigController;
    @FXML private CandidatesComponentController  candidatesComponentController;
    @FXML private ActiveTeamsComponentController activeTeamsComponentController;
    private UBoatAppController uBoatAppController;
    private final BooleanProperty isFileLoaded;
    private final StringProperty filePath;
    private final StringProperty xmlErrorMessage;

    public UBoatMainController(){
        isFileLoaded = new SimpleBooleanProperty(false);
        filePath = new SimpleStringProperty("");
        xmlErrorMessage = new SimpleStringProperty("");
    }
    @FXML
    public void initialize() {
        usernameLabel.textProperty().bind(uBoatAppController.getUsernameProperty());
        if(codeObjDisplayController != null && processComponentController != null
        && dictionaryComponentController != null && machineDetailsController != null
        && codeConfigController != null && candidatesComponentController != null
        && activeTeamsComponentController != null){
            codeObjDisplayController.setMainApplicationController(this);
            processComponentController.setMainApplicationController(this);
            dictionaryComponentController.setMainApplicationController(this);
            machineDetailsController.setMainApplicationController(this);
            codeConfigController.setMainApplicationController(this);
            candidatesComponentController.setMainApplicationController(this);
            activeTeamsComponentController.setMainApplicationController(this);
        }

        competitionTabPane.disableProperty().bind(isFileLoaded.not());
        loadFileButton.disableProperty().bind(isFileLoaded);

        xmlErrorLabel.textProperty().bind(xmlErrorMessage);
        fileTextField.textProperty().bind(filePath);
    }

    @FXML
    public void onLogoutClicked(ActionEvent event) {
        //dispatch to server...
    }
    @FXML
    public void onReadyClicked(ActionEvent event) {
        //dispatch to server...

        //below switched to the contest tab
        competitionTabPane.getSelectionModel().select(contestTab);
    }

    public void setMainController(UBoatAppController uBoatAppController) {
        this.uBoatAppController = uBoatAppController;
    }

    public void codeResetRequested() {
        //dispatch request to server
        //receive back the original code and display it
    }

    public void onProcessButtonPressed(String text) {
        //dispatch process request to server
        //receive back the translated message and display it:
                //send translation to process component
                //processComponentController.showOutput(output);
        //or upon error (not in keyboard, not in dictionary)
                //send error to process component
                //processComponentController.showErrorMessage(errorFromBody);
    }

    public void injectWordToProcess(String rowData) {
        processComponentController.injectWord(rowData);
    }

    public void handleSetByRandom() {
        //dispatch random set request to server
        //receive back the new code and insert it to code obj display with onCodeChosen
    }

    public void handleManualSet(CodeObj code) {
        //dispatch random set request to server
        //receive back the new code and insert it to code obj display with onCodeChosen
    }

    public void onLoadClicked(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select XML file");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML files", "*.xml"));
        File selectedFile = fileChooser.showOpenDialog(mainVBox.getScene().getWindow());

        if(selectedFile == null){
            xmlErrorLabel.setStyle("-fx-text-fill: red");
            xmlErrorMessage.set("Something went wrong...");
            return;
        }

        String finalUrl = HttpUrl
                .parse(Constants.UPLOAD_XML)
                .newBuilder()
                .build()
                .toString();

        HttpClientUtil.runAsyncFileUpload(finalUrl, selectedFile, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() ->
                        {
                            xmlErrorMessage.set("Something went wrong...");
                            filePath.set("");
                        }
                );
                System.out.println(e.getMessage());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() != 200) {
                    String responseBody = response.body().string();
                    Platform.runLater(() ->
                            {
                                xmlErrorLabel.setStyle("-fx-text-fill: red");
                                xmlErrorMessage.set("Something went wrong...");
                                filePath.set("");
                                //exception message from engine will be above (in body?)
                            }
                    );
                } else {
                    Platform.runLater(() -> {
                        xmlErrorLabel.setStyle("-fx-text-fill: green");
                        xmlErrorMessage.set("Successfully uploaded file to server!");
                        isFileLoaded.set(true);
                        filePath.set(selectedFile.getName());
                        //also should get from server the following details:
                        //machine details -> fill the component
                        //dictionary words -> fillDictionaryTable(List<Word> words)
                    });
                }
            }
        });
    }
}

