package clientUtils.processComponent;

import application.MainApplicationController;
import exceptions.InputException.InputException;
import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.HBox;
import javafx.util.Duration;
import org.controlsfx.control.SegmentedButton;

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
    private MainApplicationController mainApplicationController;
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
        errorLabel.getStyleClass().add("error-message");
        errorTransition = createErrorTransition();
        //resultTextField.setDisable(true);
        resultTextField.setEditable(false);
        resultTextField.setOnMouseClicked(e -> userTextField.requestFocus());

        PersistentButtonToggleGroup toggleGroup = new PersistentButtonToggleGroup();
        automatToggle.setToggleGroup(toggleGroup);
        singleToggle.setToggleGroup(toggleGroup);

        singleToggle.selectedProperty().addListener((observable, oldValue, newValue) -> {
            mainApplicationController.singleModeOn(newValue);
            if(newValue){
                clearAllFields();
            }
        });

        automatToggle.selectedProperty().addListener(((observable, oldValue, newValue) -> {
            if(newValue){
                clearAllFields();
            }
        }));

        SegmentedButton segmentedButton = new SegmentedButton(automatToggle, singleToggle);
        segmentedButton.setToggleGroup(toggleGroup);
        segmentedButton.getStyleClass().add(SegmentedButton.STYLE_CLASS_DARK);

        toggleContainer.getChildren().clear();
        toggleContainer.getChildren().add(segmentedButton);
        processButton.visibleProperty().bind(automatToggle.selectedProperty());
        doneButton.visibleProperty().bind(singleToggle.selectedProperty());

        resultText = new StringBuilder();
        userTextField.setOnKeyTyped(ke -> {
            String characterTyped = ke.getCharacter().toUpperCase();
            if(!mainApplicationController.checkForKeyInKeyBoard(characterTyped.charAt(0))
            && !characterTyped.equals(System.lineSeparator())){
                errorLabel.setText(ke.getCharacter() + " is not part of this machine's keyboard");
                if(mainApplicationController.getAnimationEnabledProperty().getValue()){
                    errorTransition.play();
                }
                else{
                    errorLabel.setOpacity(1.0);
                }
                ke.consume();
            }
        });
        userTextField.textProperty().addListener((observable, oldValue, newValue)
                                                    -> textListener(oldValue, newValue));

        processButton.setOnAction(e -> handleProcessButton());
        doneButton.setOnAction(e -> handleDoneButton());
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

    public void setMainApplicationController(MainApplicationController mainApplicationController){
        this.mainApplicationController = mainApplicationController;
    }

    private void textListener(String oldValue, String newValue){
        if(singleToggle.isSelected()){
            int change = newValue.length() - oldValue.length();
            if(change > 1){
                userTextField.setText(oldValue);
                errorLabel.setText("You're in single mode, pasting is not supported");
                errorTransition.play();
            }
            else if(change == 1){
                resultText.append(mainApplicationController.processSingleCharacter(newValue.toUpperCase().charAt(newValue.length() - 1)));
                resultTextField.setText(resultText.toString());
                mainApplicationController.getCodeChangesProperty()
                        .setValue(mainApplicationController.getCodeChangesProperty().getValue() + 1);
                mainApplicationController.notifyKeyBoardOfInput();
                //call the keyboard component... notify - animation ensues!
            }
            else{
                resultText.delete(0, resultText.length());
                resultTextField.setText(resultText.toString());
                //resultText.delete(resultText.length() + change, resultText.length());
                //resultTextField.setText(resultText.toString());
            }
        }
    }

    private void handleProcessButton() {
        try{
            mainApplicationController.onProcessButtonPressed(userTextField.getText(), this);
            userTextField.clear();
        }
        catch(InputException e){
            showErrorMessage(e.getMessage());
        }
    }

    private void handleDoneButton(){
        mainApplicationController.onSingleModeDone(userTextField.getText(), resultTextField.getText());
        userTextField.clear();
        resultTextField.clear();
        resultText.delete(0, resultText.length());
    }

    public void keyPressed(String text){
        userTextField.setText(userTextField.getText().concat(text));
    }

    public void showOutput(String output) {
        resultTextField.setText(output);
    }
    public Character getLastProcessedChar() { return resultTextField.getText().charAt(resultTextField.getText().length()-1); }

    public void showErrorMessage(String message) {
        errorLabel.setText(message);
        errorTransition.play();
    }

    public void changeLabelForBruteForce() {
        promptTextLabel.setText("Enter dictionary words below: ");
    }

    public void injectWord(String rowData) {
        userTextField.setText(userTextField.getText() + rowData);
    }

    public void changeTheme(Object cssPrefix) {
        toggleContainer.getScene().getStylesheets().clear();
        if(cssPrefix != null){
            String css = cssPrefix + myStyleSheet;
            toggleContainer.getScene().getStylesheets().add(css);
        }
    }
}
