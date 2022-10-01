//package uBoatClient.uBoatLogin;
//
//import clientUtils.LoginController;
//import clientUtils.MainAppController;
//import clientUtils.chooseNameComponent.*;
//import javafx.application.Platform;
//import javafx.beans.property.BooleanProperty;
//import javafx.beans.property.SimpleBooleanProperty;
//import javafx.beans.property.SimpleStringProperty;
//import javafx.beans.property.StringProperty;
//import javafx.event.ActionEvent;
//import javafx.fxml.FXML;
//import javafx.scene.control.Button;
//import javafx.scene.control.Label;
//import javafx.scene.control.TextField;
//import javafx.scene.layout.Pane;
//import javafx.stage.FileChooser;
//import okhttp3.Call;
//import okhttp3.Callback;
//import okhttp3.HttpUrl;
//import okhttp3.Response;
//import org.jetbrains.annotations.NotNull;
//import util.Constants;
//import util.http.HttpClientUtil;
//
//import java.io.File;
//import java.io.IOException;
//
//public class UBoatLoginController implements LoginController {
//    @FXML private Button loadXMLButton;
//    @FXML private TextField loadXMLTextField;
//    @FXML private Label xmlErrorLabel;
//    @FXML private Button registerButton;
//    private MainAppController mainController;
//
//    @FXML private Pane chooseNameComponent;
//    @FXML private ChooseNameComponentController chooseNameComponentController;
//
//    private BooleanProperty usernameValidProperty;
//
//    private StringProperty xmlErrorMessageProperty;
//
//    private File selectedFile;
//
//    private BooleanProperty isFileSelectedProperty;
//
//    private StringProperty fileName;
//
//    public UBoatLoginController(){
//        usernameValidProperty = new SimpleBooleanProperty(false);
//        xmlErrorMessageProperty = new SimpleStringProperty("");
//        isFileSelectedProperty = new SimpleBooleanProperty(false);
//        fileName = new SimpleStringProperty("");
//    }
//
//    @FXML
//    public void initialize(){
//        if(chooseNameComponentController != null) {
//            chooseNameComponentController.setParentController(this);
//            chooseNameComponentController.initialize();
//        }
//        else{
//            System.out.println("null choose name controller");
//        }
//
//        loadXMLButton.visibleProperty().bind(usernameValidProperty);
//        loadXMLTextField.visibleProperty().bind(usernameValidProperty);
//        xmlErrorLabel.visibleProperty().bind(usernameValidProperty);
//        registerButton.visibleProperty().bind(usernameValidProperty);
//
//        registerButton.disableProperty().bind(isFileSelectedProperty.not());
//
//        usernameValidProperty.addListener((observable, oldValue, newValue) -> {
//            if(newValue){
//                chooseNameComponentController.hideComponent();
//            }
//        });
//
//        xmlErrorLabel.textProperty().bind(xmlErrorMessageProperty);
//        loadXMLTextField.setEditable(false);
//        loadXMLTextField.textProperty().bind(fileName);
//    }
//
//    @Override
//    public void setMainController(MainAppController mainController) {
//        this.mainController = mainController;
//    }
//
//    @Override
//    public void onUsernameSelected(String username) {
//        System.out.println("dispatch login request to server...");
//
//        String finalUrl = HttpUrl
//                .parse(Constants.UBOAT_LOGIN_PAGE)
//                .newBuilder()
//                .addQueryParameter("username", username)
//                .build()
//                .toString();
//
//        HttpClientUtil.runAsync(finalUrl, new Callback() {
//            @Override
//            public void onFailure(@NotNull Call call, @NotNull IOException e) {
//                Platform.runLater(() ->
//                        chooseNameComponentController
//                                .errorMessageProperty()
//                                .set("Something went wrong: " + e.getMessage())
//                );
//            }
//
//            @Override
//            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
//                if (response.code() != 200) {
//                    String responseBody = response.body().string();
//                    Platform.runLater(() ->
//                            chooseNameComponentController
//                                    .errorMessageProperty().set("Something went wrong: " + responseBody)
//                    );
//                } else {
//                    Platform.runLater(() -> {
//                        //mainController.usernameProperty().set(username);
//                        usernameValidProperty.set(true);
//                    });
//                }
//            }
//        });
//    }
//
//    public boolean isUsernameValidProperty() {
//        return usernameValidProperty.get();
//    }
//
//    public BooleanProperty usernameValidPropertyProperty() {
//        return usernameValidProperty;
//    }
//
//    public String getXmlErrorMessageProperty() {
//        return xmlErrorMessageProperty.get();
//    }
//
//    public StringProperty xmlErrorMessagePropertyProperty() {
//        return xmlErrorMessageProperty;
//    }
//
//    @FXML
//    public void onLoadClicked(ActionEvent actionEvent) {
//        FileChooser fileChooser = new FileChooser();
//        fileChooser.setTitle("Select XML file");
//        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML files", "*.xml"));
//        selectedFile = fileChooser.showOpenDialog(mainController.getMainBorderPane().getScene().getWindow());
//
//        if(selectedFile == null){
//            xmlErrorLabel.setStyle("-fx-text-fill: red");
//            xmlErrorMessageProperty.set("Something went wrong...");
//            return;
//        }
//
//        String finalUrl = HttpUrl
//                .parse(Constants.UPLOAD_XML)
//                .newBuilder()
//                .build()
//                .toString();
//
//        HttpClientUtil.runAsyncFileUpload(finalUrl, selectedFile, new Callback() {
//            @Override
//            public void onFailure(@NotNull Call call, @NotNull IOException e) {
//                Platform.runLater(() ->
//                    {
//                        xmlErrorMessageProperty.set("Something went wrong...");
//                        fileName.set("");
//                    }
//                );
//                System.out.println(e.getMessage());
//            }
//
//            @Override
//            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
//                if (response.code() != 200) {
//                    String responseBody = response.body().string();
//                    Platform.runLater(() ->
//                            {
//                                xmlErrorLabel.setStyle("-fx-text-fill: red");
//                                xmlErrorMessageProperty.set("Something went wrong...");
//                                fileName.set("");
//                                //exception message from engine will be above (in body?)
//                            }
//                    );
//                } else {
//                    Platform.runLater(() -> {
//                        xmlErrorLabel.setStyle("-fx-text-fill: green");
//                        xmlErrorMessageProperty.set("Successfully uploaded file to server!");
//                        isFileSelectedProperty.set(selectedFile != null);
//                        fileName.set(selectedFile.getName());
//                    });
//                }
//            }
//        });
//
//    }
//
//    public void onRegisterClicked(ActionEvent actionEvent) {
//        mainController.onUBoatRegistered();
//    }
//}
