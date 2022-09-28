package battleField.client.main;

import battleField.client.component.global.entityChooser.EntityChooserController;
import battleField.client.component.mainApp.MainAppController;
import battleField.client.util.http.HttpClientUtil;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class battleFieldClient extends Application {
    private MainAppController mainAppController;

    @Override
    public void start(Stage primaryStage) {

        primaryStage.setTitle("Battle Field");

        URL loginPage = getClass().getResource("/battleField/client/component/mainApp/mainApp.fxml");
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(loginPage);
            Parent root = fxmlLoader.load();
            mainAppController = fxmlLoader.getController();

            Scene scene = new Scene(root, 600, 400);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void stop() {
        mainAppController.close(); // chain reaction to shut down everything
                                   // important to remove user from server
        HttpClientUtil.shutdown();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
