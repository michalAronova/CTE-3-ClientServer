package alliesClient.components.missionSizeChooser;

import alliesClient.alliesMain.AlliesMainController;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.VBox;
import javafx.util.converter.IntegerStringConverter;

import java.util.function.UnaryOperator;

public class MissionSizeChooserController {

    @FXML private Label errorLabel;
    @FXML private VBox vBoxRoot;
    @FXML private TextField missionsSizeTextField;
    @FXML private Button readyButton;
    private AlliesMainController alliesMainController;

    private StringProperty errorMessage;

    public MissionSizeChooserController() {
        errorMessage = new SimpleStringProperty("");
    }

    @FXML public void initialize(){
        errorLabel.textProperty().bind(errorMessage);

        UnaryOperator<TextFormatter.Change> integerFilter = change -> {
            String newText = change.getControlNewText();
            if (newText.matches("-?([1-9][0-9]*)?")) {
                return change;
            }
            return null;
        };

        missionsSizeTextField.setTextFormatter(
                new TextFormatter<>(new IntegerStringConverter(), 0, integerFilter));
    }

    public void setAlliesMainController(AlliesMainController alliesMainController) {
        this.alliesMainController = alliesMainController;
        vBoxRoot.visibleProperty().bind(alliesMainController.ongoingContestProperty().not());
    }

    public void onReadyClicked(ActionEvent actionEvent) {
        try {
            alliesMainController.missionSizeChosen(Integer.parseInt(missionsSizeTextField.getText()));
        }
        catch(NumberFormatException e){
            errorMessage.set("Error: Size must be a positive number");
        }
    }
}
