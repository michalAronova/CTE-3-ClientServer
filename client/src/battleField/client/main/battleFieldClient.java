package battleField.client.main;

import battleField.client.component.entityChooser.EntityChooserController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class battleFieldClient extends Application {
    private EntityChooserController entityChooserController;

    @Override
    public void start(Stage primaryStage) {

        primaryStage.setTitle("Choose Entity");

        URL loginPage = getClass().getResource("/battleField/client/component/entityChooser/entityChooser.fxml");
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(loginPage);
            Parent root = fxmlLoader.load();
            entityChooserController = fxmlLoader.getController();
            entityChooserController.setMyApp(this);
            Scene scene = new Scene(root, 600, 400);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

//    @Override
//    public void stop() {
//        entityChooserController.close(); // chain reaction to shut down everything
//                                   // important to remove user from server
//        HttpClientUtil.shutdown();
//    }

    public static void main(String[] args) {
        launch(args);
    }
}
