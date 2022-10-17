package alliesClient;

import alliesClient.alliesApp.AlliesAppController;
import alliesClient.alliesMain.AlliesMainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import util.http.HttpClientUtil;

import java.io.IOException;
import java.net.URL;

import static util.Constants.ALLIES_APP_FXML_RESOURCE_LOCATION;

public class alliesClientMain extends Application {
    private AlliesAppController alliesAppController;

    @Override
    public void start(Stage primaryStage) {

        primaryStage.setTitle("Allies");

        URL loginPage = getClass().getResource(ALLIES_APP_FXML_RESOURCE_LOCATION);
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(loginPage);
            Parent root = fxmlLoader.load();
            alliesAppController = fxmlLoader.getController();

            Scene scene = new Scene(root, 600, 400);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void stop() { //this handles the clicking on x...
        //uBoatAppController.close(); // chain reaction to shut down everything
        // important to remove user from server
        HttpClientUtil.shutdown();
    }

    public static void main(String[] args) { launch(args);}
}
