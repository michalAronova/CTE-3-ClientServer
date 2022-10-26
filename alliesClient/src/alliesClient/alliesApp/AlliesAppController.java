package alliesClient.alliesApp;

import alliesClient.alliesMain.AlliesMainController;
import clientUtils.LoginController;
import clientUtils.chooseNameComponent.ChooseNameComponentController;
import clientUtils.popUpDialog;
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
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import util.Constants;
import util.http.HttpClientUtil;

import java.io.IOException;
import java.net.URL;

import static parameters.ConstantParams.USERNAME;
import static util.Constants.ALLIES_MAIN_FXML_RESOURCE_LOCATION;

public class AlliesAppController implements LoginController {
    @FXML public ScrollPane mainScrollPane;
    @FXML public VBox vBox;
    @FXML public ChooseNameComponentController chooseNameComponentController;

    @FXML public AlliesMainController alliesMainController;
    private BooleanProperty isValidUsername;
    private StringProperty usernameProperty;

    public AlliesAppController(){
        isValidUsername = new SimpleBooleanProperty(false);
        usernameProperty = new SimpleStringProperty("");
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
        URL mainPageUrl = getClass().getResource(ALLIES_MAIN_FXML_RESOURCE_LOCATION);
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(mainPageUrl);
            Node main = fxmlLoader.load();
            alliesMainController = fxmlLoader.getController();
            alliesMainController.setMainController(this);
            vBox.getChildren().clear();
            vBox.getChildren().add(main);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public StringProperty getUsernameProperty() {
        return usernameProperty;
    }

    @Override
    public void onUsernameSelected(String username) {
        System.out.println("dispatch allies login request to server...");
        String finalUrl = HttpUrl
                .parse(Constants.ALLIES_LOGIN_PAGE)
                .newBuilder()
                .addQueryParameter(USERNAME, username)
                .build()
                .toString();

        HttpClientUtil.runAsync(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() ->
                        chooseNameComponentController
                                .errorMessageProperty()
                                .set("Something went wrong: " + e.getMessage())
                );
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() != 200) {
                    String responseBody = response.body().string();
                    Platform.runLater(() ->
                            chooseNameComponentController
                                    .errorMessageProperty().set("Something went wrong: " + responseBody)
                    );
                } else {
                    Platform.runLater(() -> {
                        usernameProperty.set(username);
                        isValidUsername.set(true);
                    });
                }
            }
        });
    }
}
