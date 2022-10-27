package clientUtils;

import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.DialogEvent;
import javafx.stage.Modality;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.function.Consumer;

public class popUpDialog {
    public popUpDialog(String popped, String message, Runnable onDialogOKClicked) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.initModality(Modality.NONE );
        alert.setTitle("We have a winner!");
        alert.setHeaderText(popped+": Contest ended");
        alert.setContentText(message);
        alert.setOnCloseRequest(event -> onDialogOKClicked.run());
        alert.showAndWait();
    }
    public popUpDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.initModality(Modality.NONE );
        alert.setTitle("We have a winner!");
        alert.setHeaderText("Contest ended");
        alert.setContentText(message);
        alert.setOnCloseRequest(event -> System.out.println("ok clicked"));
        alert.showAndWait();
    }
}