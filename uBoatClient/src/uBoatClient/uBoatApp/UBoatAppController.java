package uBoatClient.uBoatApp;

import clientUtils.LoginController;
import clientUtils.MainAppController;
import clientUtils.chooseNameComponent.ChooseNameComponentController;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import uBoatClient.uBoatMain.UBoatMainController;
import util.Constants;
import util.http.HttpClientUtil;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import static util.Constants.UBOAT_MAIN_FXML_RESOURCE_LOCATION;

public class UBoatAppController implements LoginController {
    @FXML public ScrollPane mainScrollPane;
    @FXML public VBox vBox;
    @FXML public ChooseNameComponentController chooseNameComponentController;

    @FXML public UBoatMainController uBoatMainController;
    private BooleanProperty isValidUsername;
    private StringProperty usernameProperty;
    private BooleanProperty isFileSelectedProperty;

    public UBoatAppController(){
        isValidUsername = new SimpleBooleanProperty(false);
        isFileSelectedProperty = new SimpleBooleanProperty(false);
    }

    @FXML
    public void initialize() {
        if (chooseNameComponentController != null) {
            chooseNameComponentController.setParentController(this);
            chooseNameComponentController.initialize();
        } else {
            System.out.println("null choose name controller");
        }

        isValidUsername.addListener((observable, oldValue, newValue) -> {
            if(newValue){
                switchToMain();
            }
        });
    }

    private void switchToMain() {
        URL mainPageUrl = getClass().getResource(UBOAT_MAIN_FXML_RESOURCE_LOCATION);
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(mainPageUrl);
            Node main = fxmlLoader.load();
            uBoatMainController = fxmlLoader.getController();
            uBoatMainController.setMainController(this);
            uBoatMainController.initialize();
            vBox.getChildren().clear();
            vBox.getChildren().add(main);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public StringProperty getUsernameProperty() {
        return usernameProperty;
    }

    public StringProperty usernamePropertyProperty() {
        return usernameProperty;
    }

    @Override
    public void onUsernameSelected(String username) {
        System.out.println("dispatch uboat login request to server...");

        String finalUrl = HttpUrl
                .parse(Constants.UBOAT_LOGIN_PAGE)
                .newBuilder()
                .addQueryParameter("username", username)
                .build()
                .toString();

        HttpClientUtil.runAsync(finalUrl, new Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try (ResponseBody responseBody = response.body()) {
                    if (response.code() == 200) {
                        Platform.runLater(() -> {
                            usernameProperty.set(username);
                            isValidUsername.set(true);
                        });
                    }
                    else {
                        String responseBodyString = responseBody.string();
                        Platform.runLater(() ->
                                chooseNameComponentController
                                        .errorMessageProperty().set("Something went wrong: " + responseBodyString)
                        );
                    }
                }
            }
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() ->
                        chooseNameComponentController
                                .errorMessageProperty()
                                .set("Something went wrong: " + e.getMessage())
                );
            }
        });
    }
}
