package uBoatClient;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import uBoatClient.uBoatLogin.UBoatLoginController;
import util.http.HttpClientUtil;

import java.io.IOException;
import java.net.URL;

public class UBoatClientMain extends Application {
    private UBoatLoginController uBoatLoginController;

    @Override
    public void start(Stage primaryStage) {

        primaryStage.setTitle("Battle Field");

        URL loginPage = getClass().getResource("/uBoatClient/uBoatLogin/UBoatLogin.fxml");
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(loginPage);
            Parent root = fxmlLoader.load();
            uBoatLoginController = fxmlLoader.getController();

            Scene scene = new Scene(root, 600, 400);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void stop() {
        //uBoatLoginController.close(); // chain reaction to shut down everything
        // important to remove user from server
        HttpClientUtil.shutdown();
    }

    //public static void main(String[] args) { launch(args);}
}
