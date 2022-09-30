package battleField.client.component.mainApp;

import battleField.client.component.LoginController;
import battleField.client.component.global.entityChooser.EntityChooserController;
import battleField.client.util.Constants;
import battleField.client.util.http.HttpClientUtil;
import engine.entity.EntityEnum;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import uBoatClient.UBoatClientMain;

import java.io.IOException;
import java.net.URL;

import static battleField.client.util.Constants.*;

public class MainAppController {

    @FXML private BorderPane mainBorderPane;
    @FXML private Label entityLabel;

    @FXML private Label usernameLabel;

    @FXML private AnchorPane mainAnchorPane;

    @FXML private EntityChooserController entityChooserController;

    private Application myApp;

    private Node loginComponent;
    private LoginController loginController;
    private StringProperty usernameProperty;
    private StringProperty entityProperty;
    private EntityEnum entity;

    public MainAppController(){
        usernameProperty = new SimpleStringProperty(Constants.JOHN_DOE);
        entityProperty = new SimpleStringProperty("");
    }

    @FXML
    public void initialize(){
        if(entityChooserController != null){
            entityChooserController.setMainController(this);
        }
        else{
            System.out.println("entity chooser controller is null");
        }

        usernameLabel.textProperty().bind(usernameProperty);
        entityLabel.textProperty().bind(entityProperty);
    }

    public void setMyApp(Application app){
        this.myApp = app;
    }


    public StringProperty usernameProperty() {
        return usernameProperty;
    }

    public StringProperty entityProperty() {
        return entityProperty;
    }

    public EntityEnum getEntity() {
        return entity;
    }

    private void switchByEntity() {
        switch (entity){
            case UBOAT:
                loadUBoatLogin();
                break;
            case ALLIES:
                loadAlliesLogin();
                break;
            case AGENT:
                loadAgentLogin();
                break;
        }
        setMainPanelTo(loginComponent);
    }

    private void setMainPanelTo(Node node) {
        mainAnchorPane.getChildren().clear();
        mainAnchorPane.getChildren().add(node);
        AnchorPane.setBottomAnchor(node, 1.0);
        AnchorPane.setTopAnchor(node, 1.0);
        AnchorPane.setLeftAnchor(node, 1.0);
        AnchorPane.setRightAnchor(node, 1.0);
    }

    private void loadAgentLogin() {

    }

    private void loadAlliesLogin() {

    }

    private void loadUBoatLogin() {
        URL loginPageUrl = getClass().getResource(UBOAT_LOGIN_FXML_RESOURCE_LOCATION);
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(loginPageUrl);
            loginComponent = fxmlLoader.load();
            loginController = fxmlLoader.getController();
            loginController.setMainController(this);
            setMainPanelTo(loginComponent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setEntity(EntityEnum entity) {
        this.entity = entity;
        entityProperty.set(entity.toString());
        switchByEntity();
    }

    public void close() {

        String finalUrl = HttpUrl
                .parse(Constants.LOGOUT)
                .newBuilder().build()
                .toString();

        HttpClientUtil.runAsync(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println("couldn't logout");
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
                System.out.println("logged out successfully for " + usernameProperty.getValue());
                entity = null;
                Platform.runLater(() -> {
                    usernameProperty.set(Constants.JOHN_DOE);
                    entityProperty.set("");
                });
            }
        });
    }

    public BorderPane getMainBorderPane() {
        return mainBorderPane;
    }

    public void onUBoatRegistered() {
        System.out.println("switch to uBoat main panel!");
        //switch to uBoat Panel! Let's goooo
    }

    public void openNewApp() {
        UBoatClientMain.launch();
        try{
            myApp.stop();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}

