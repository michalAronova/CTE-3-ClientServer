package uBoatClient.uBoatMain;

import DTO.MachineDetails;
import DTO.codeObj.CodeObj;
import DTO.processResponse.ProcessResponse;
import clientUtils.MainAppController;
import clientUtils.activeTeams.ActiveTeamsComponentController;
import clientUtils.candidatesComponent.CandidatesComponentController;
import javafx.beans.property.*;
import okhttp3.*;
import uBoatClient.components.codeConfigurationComponent.CodeConfigComponentController;
import uBoatClient.components.codeObjDisplayComponent.CodeObjDisplayComponentController;
import uBoatClient.components.dictionaryComponent.DictionaryComponentController;
import uBoatClient.components.machineDetails.MachineDetailsController;
import uBoatClient.components.processComponent.ProcessComponentController;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import org.jetbrains.annotations.NotNull;
import uBoatClient.refreshers.ActiveTeamsRefresher;
import uBoatClient.refreshers.CandidatesRefresher;
import uBoatClient.uBoatApp.UBoatAppController;
import util.Constants;
import util.http.HttpClientUtil;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.util.Timer;

import static parameters.ConstantParams.MSG_TO_PROCESS;
import static util.Constants.GSON_INSTANCE;
import static util.Constants.REFRESH_RATE;

public class UBoatMainController implements MainAppController, Closeable {
    @FXML private Button continueButton;
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
    //machine tab controllers
    @FXML private MachineDetailsController machineDetailsController;
    @FXML private CodeConfigComponentController codeConfigController;

    //contest tab controllers
    @FXML private CodeObjDisplayComponentController codeObjDisplayController;
    @FXML private ProcessComponentController processComponentController;
    @FXML private DictionaryComponentController dictionaryComponentController;
    @FXML private CandidatesComponentController  candidatesComponentController;
    @FXML private ActiveTeamsComponentController activeTeamsComponentController;
    private UBoatAppController uBoatAppController;
    private final BooleanProperty isFileLoaded;
    private final StringProperty filePath;
    private final StringProperty xmlErrorMessage;
    private CodeObj currentCode;
    private int requiredTeams;
    private IntegerProperty teamsLeftForStart;
    private BooleanProperty isReady;
    private BooleanProperty isCompetitionOn;

    private ActiveTeamsRefresher activeTeamsRefresher;

    private Timer teamsTimer;
    private CandidatesRefresher candidatesRefresher;
    private Timer candidatesTimer;

    public UBoatMainController(){
        isFileLoaded = new SimpleBooleanProperty(false);
        filePath = new SimpleStringProperty("");
        xmlErrorMessage = new SimpleStringProperty("");
        isReady = new SimpleBooleanProperty(false);
        isCompetitionOn = new SimpleBooleanProperty(false);
    }
    @FXML
    public void initialize() {
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

        readyButton.disableProperty().bind(isReady);
        dictionaryComponentController.disableProperty().bind(isReady);
        processComponentController.disableProperty().bind(isReady);

        competitionTabPane.disableProperty().bind(isFileLoaded.not());
        loadFileButton.disableProperty().bind(isFileLoaded);

        xmlErrorLabel.textProperty().bind(xmlErrorMessage);
        fileTextField.textProperty().bind(filePath);
        contestTab.setDisable(true);
        continueButton.setDisable(true);

        entityLabel.setText("UBOAT");
    }

    @FXML
    public void onLogoutClicked(ActionEvent event) {
        //dispatch to server...
    }
    @FXML
    public void onReadyClicked(ActionEvent event) {
        //dispatch to server...
        String finalUrl = HttpUrl
                .parse(Constants.UBOAT_READY)
                .newBuilder()
                .build()
                .toString();

        HttpClientUtil.runAsync(finalUrl, new Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try (ResponseBody responseBody = response.body()) {
                    String str = responseBody.string();
                    if (response.code() == 200) {
                        System.out.println(str);
                        Platform.runLater(() -> isReady.set(true));
                    }
                    else {
                        System.out.println(str);
                        Platform.runLater(() -> {

                        });
                    }
                }
            }
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println("failed");
            }
        });
    }

    public void setMainController(UBoatAppController uBoatAppController) {
        this.uBoatAppController = uBoatAppController;
        usernameLabel.textProperty().bind(uBoatAppController.getUsernameProperty());
    }

    public void codeResetRequested() {
        //dispatch request to server
        resetCodeRequest();
    }

    private void resetCodeRequest() {
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = RequestBody.create("", mediaType);
        Request request = new Request.Builder()
                .url(Constants.CODE_RESET)
                .method("POST", body)
                .build();

        enqueueCodeObjCallBack(request);
    }

    private void enqueueCodeObjCallBack(Request request) {
        Call call = HttpClientUtil.HTTP_CLIENT.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try (ResponseBody responseBody = response.body()) {
                    //receive back the new code and insert it to code obj display with onCodeChosen
                    String codeJson = responseBody.string();
                    currentCode = GSON_INSTANCE.fromJson(codeJson, CodeObj.class);
                    Platform.runLater(() -> {
                        codeObjDisplayController.onCodeChosen(currentCode);
                    });
                }
            }
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println("failed :(");
            }
        });
    }

    public void onProcessButtonPressed(String text) {
        processMsgRequest(text);
    }

    public void injectWordToProcess(String rowData) {
        processComponentController.injectWord(rowData);
    }

    public void handleSetByRandom() {
        //dispatch random set request to server
        configRandomRequest();
        continueButton.setDisable(false);
    }

    public void handleManualSet(CodeObj code) {
        //dispatch manual set request to server
        configManualRequest(code);
        continueButton.setDisable(false);
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

        loadXMLRequest(selectedFile);
    }


    public void onContinueClicked(ActionEvent actionEvent) {
        //below switches to the contest tab
        contestTab.setDisable(false);
        competitionTabPane.getSelectionModel().select(contestTab);
    }
    private void loadXMLRequest(File selectedFile) {
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
                try (ResponseBody responseBody = response.body()) {
                    String body = responseBody.string();
                    if (response.code() != 200) {
                        Platform.runLater(() ->
                                {
                                    xmlErrorLabel.setStyle("-fx-text-fill: red");
                                    xmlErrorMessage.set("Error:" + body);
                                    filePath.set("");
                                }
                        );
                    }
                    else {
                        MachineDetails details = GSON_INSTANCE
                                .fromJson(body, MachineDetails.class);
                        requiredTeams = details.getRequiredTeams();
                        teamsLeftForStart = new SimpleIntegerProperty(details.getRequiredTeams());
                        teamsLeftForStart.addListener((observable, oldValue, newValue) -> {
                            if(newValue.intValue() == 0){
                                isCompetitionOn.set(true);
                            }
                        });
                        startTeamsRefresher();
                        startCandidatesRefresher();

                        Platform.runLater(() -> {
                            xmlErrorLabel.setStyle("-fx-text-fill: green");
                            xmlErrorMessage.set("Successfully uploaded file to server!");
                            isFileLoaded.set(true);
                            filePath.set(selectedFile.getName());

                            machineDetailsController.fileLoaded(details);
                            codeConfigController.handleFileLoaded(details.getKeys(), details.getRotorsInUse(),
                                    details.getTotalRotors(), details.getReflectorsCount());
                            dictionaryComponentController.fillDictionaryTable(details.getDictionary());
                        });
                    }
                }
            }
        });
    }

    private void startTeamsRefresher() {
        activeTeamsRefresher = new ActiveTeamsRefresher(
                ((teams) -> {
                    activeTeamsComponentController.replaceTeams(teams);
                    teamsLeftForStart.set(requiredTeams - teams.size());
                }),
                isReady, isCompetitionOn);
        teamsTimer = new Timer();
        teamsTimer.schedule(activeTeamsRefresher, REFRESH_RATE, REFRESH_RATE);
    }

    private void startCandidatesRefresher(){
        candidatesRefresher = new CandidatesRefresher(
                (candidates) -> candidatesComponentController.addMultiple(candidates, false),
                isCompetitionOn);
        candidatesTimer = new Timer();
        candidatesTimer.schedule(candidatesRefresher, REFRESH_RATE, REFRESH_RATE);
    }

    private void configManualRequest(CodeObj code){
        String finalUrl = HttpUrl
                .parse(Constants.MANUAL_CONFIG)
                .newBuilder()
                .build()
                .toString();

        String codeJson = GSON_INSTANCE.toJson(code);
        RequestBody body =
                new MultipartBody.Builder()
                        .addFormDataPart("code", codeJson)
                        .build();

        Request request = new Request.Builder()
                .url(finalUrl)
                .post(body)
                .build();

        enqueueCodeObjCallBack(request);
    }
    private void configRandomRequest() {
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = RequestBody.create("", mediaType);
        Request request = new Request.Builder()
                .url(Constants.RANDOM_CONFIG)
                .method("POST", body)
                .build();

        enqueueCodeObjCallBack(request);
    }
    private void processMsgRequest(String msg){
        String finalUrl = HttpUrl
                .parse(Constants.PROCESS)
                .newBuilder()
                .addQueryParameter(MSG_TO_PROCESS, msg)
                .build()
                .toString();

        HttpClientUtil.runAsync(finalUrl, new Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try (ResponseBody responseBody = response.body()) {
                    String str = responseBody.string();
                    if (response.code() == 200) {
                        ProcessResponse processResponse = GSON_INSTANCE.fromJson(str, ProcessResponse.class);
                        currentCode = processResponse.getNewCode();
                        Platform.runLater(() -> {
                            processComponentController.showOutput(processResponse.getOutput());
                            codeObjDisplayController.onCodeChosen(currentCode);
                        });
                    }
                    else {
                        Platform.runLater(() -> {
                            processComponentController.showErrorMessage(str);
                        });
                    }
                }
            }
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println("failed");
            }
        });
    }

    public int getTeamsLeftForStart() {
        return teamsLeftForStart.get();
    }

    public IntegerProperty teamsLeftForStartProperty() {
        return teamsLeftForStart;
    }

    @Override
    public void close() throws IOException {
        if (activeTeamsRefresher != null && teamsTimer != null) {
            activeTeamsRefresher.cancel();
            teamsTimer.cancel();
        }
        if (candidatesRefresher != null && candidatesTimer != null) {
            candidatesRefresher.cancel();
            candidatesTimer.cancel();
        }
    }
}

