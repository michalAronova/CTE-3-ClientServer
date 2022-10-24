package clientUtils;

import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.DialogEvent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.function.Consumer;

public class popUpDialog {
    public popUpDialog(String message, Runnable onDialogOKClicked) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("We have a winner!");
        alert.setHeaderText("Contest ended");
        alert.setContentText(message);
        alert.setOnCloseRequest(new EventHandler<DialogEvent>() {
            @Override
            public void handle(DialogEvent event) {
                onDialogOKClicked.run();
            }
        });
        alert.showAndWait();
    }
    public popUpDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("We have a winner!");
        alert.setHeaderText("Contest ended");
        alert.setContentText(message);
        alert.setOnCloseRequest(new EventHandler<DialogEvent>() {
            @Override
            public void handle(DialogEvent event) {
                System.out.println("ok clicked");
            }
        });
        alert.showAndWait();
    }
}