package uBoatClient.components.processComponent;

import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.HBox;
import javafx.util.Duration;
import org.controlsfx.control.SegmentedButton;
import uBoatClient.uBoatMain.UBoatMainController;

public class ProcessComponentController {
    @FXML private HBox toggleContainer;
    @FXML private ToggleButton automatToggle;
    @FXML private ToggleButton singleToggle;
    @FXML private TextField userTextField;
    @FXML private TextField resultTextField;
    @FXML private Button processButton;
    @FXML private Button resetButton;
    @FXML private Button clearButton;
    @FXML private Button doneButton;
    @FXML private Label errorLabel;
    @FXML private Label promptTextLabel;
    private UBoatMainController mainApplicationController;
    private StringBuilder resultText;
    private FadeTransition errorTransition;
    private String myStyleSheet = "processComponent";

    public Boolean isBruteForceProcess() { return bruteForceProcess; }
    public void setBruteForceProcess(Boolean bruteForceProcess) {
        this.bruteForceProcess = bruteForceProcess;
    }

    private Boolean bruteForceProcess;
    public ToggleButton getSingleToggle() { return singleToggle; }
    @FXML public void initialize(){
        errorLabel.setOpacity(0);
        errorLabel.setStyle("-fx-text-fill: red");
        errorTransition = createErrorTransition();
        resultTextField.setEditable(false);
        resultTextField.setOnMouseClicked(e -> userTextField.requestFocus());

        changeLabelForBruteForce();
        singleToggle.setSelected(false);
        automatToggle.setSelected(true);
        toggleContainer.getChildren().clear();

        processButton.visibleProperty().bind(automatToggle.selectedProperty());
        doneButton.visibleProperty().bind(singleToggle.selectedProperty());

        resultText = new StringBuilder();

        processButton.setOnAction(e -> handleProcessButton());
        resetButton.setOnAction(e -> handleResetButton());
        clearButton.setOnAction(e -> clearAllFields());
    }

    private FadeTransition createErrorTransition() {
        errorTransition = new FadeTransition(Duration.millis(3500), errorLabel);
        errorTransition.setFromValue(0);
        errorTransition.setToValue(1.0);
        errorTransition.setCycleCount(2);
        errorTransition.setAutoReverse(true);
        return errorTransition;
    }

    private void clearAllFields() {
        userTextField.clear();
        resultText.delete(0, resultText.length());
        resultTextField.clear();
    }

    private void handleResetButton() {
        mainApplicationController.codeResetRequested();
    }

    public void setMainApplicationController(UBoatMainController mainApplicationController){
        this.mainApplicationController = mainApplicationController;
    }

    private void handleProcessButton() {
        mainApplicationController.onProcessButtonPressed(userTextField.getText());
        userTextField.clear();
    }

    public void showOutput(String output) {
        resultTextField.setText(output);
    }

    public void showErrorMessage(String message) {
        errorLabel.setText(message);
        errorTransition.play();
    }

    public void changeLabelForBruteForce() {
        promptTextLabel.setText("Enter dictionary words below: ");
    }

    public void injectWord(String rowData) {
        userTextField.setText(userTextField.getText() + rowData +" ");
    }

}
