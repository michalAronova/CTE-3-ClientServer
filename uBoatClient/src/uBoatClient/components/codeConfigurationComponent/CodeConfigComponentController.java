package uBoatClient.components.codeConfigurationComponent;

import DTO.codeObj.CodeObj;
import uBoatClient.components.codeConfigurationComponent.plugsComponent.PlugsComponentController;
import uBoatClient.components.codeConfigurationComponent.rotorConfigurationComponent.RotorConfigComponentController;
import enigmaMachine.reflector.ReflectorID;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import uBoatClient.uBoatMain.UBoatMainController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CodeConfigComponentController {

    @FXML public Button setByManualButton;
    @FXML public Button setByRandomButton;
    @FXML public Label messageLabel;
    @FXML public Accordion codeConfigurationAccordion;
    @FXML public Button setButton;
    @FXML public Button clearButton;
    @FXML public VBox rootVBox;
    @FXML private ComboBox<String> reflectorComboBox;

    @FXML private PlugsComponentController plugsComponentController;
    @FXML private RotorConfigComponentController rotorConfigComponentController;
    private final BooleanProperty rotorsFilled;
    private List<Integer> rotorIDs;
    private List<Character> rotorPositions;

    private UBoatMainController mainApplicationController;



    public CodeConfigComponentController(){
        rotorsFilled = new SimpleBooleanProperty(false);
    }

    @FXML public void initialize(){
        if(plugsComponentController != null && rotorConfigComponentController != null){
            plugsComponentController.setCodeConfigController(this);
            rotorConfigComponentController.setCodeConfigController(this);
        }
        setButton.disableProperty().bind(Bindings
                .createBooleanBinding(()->
                        reflectorComboBox.valueProperty().getValue() == null || !rotorsFilled.getValue()
                        , reflectorComboBox.valueProperty(), rotorsFilled));

        clearButton.setDisable(true);
        reflectorComboBox.setPromptText("Reflector ID");
        setByRandomButton.setDisable(true);
        setByManualButton.setDisable(true);
    }

    public void setMainApplicationController(UBoatMainController mainApplicationController){
        this.mainApplicationController = mainApplicationController;
    }
    @FXML public void onSetByManualClicked(ActionEvent actionEvent) {
        codeConfigurationAccordion.setDisable(false);
        setByManualButton.setDisable(true);
    }
    public BooleanProperty getRotorsFilledProperty(){ return rotorsFilled; }
    @FXML public void onSetByRandomClicked(ActionEvent actionEvent) {
        codeConfigurationAccordion.setDisable(true);
        setByManualButton.setDisable(false);
        removeAllChoices();
        mainApplicationController.handleSetByRandom();
    }
    @FXML public void onClearButtonClicked(){
        removeAllChoices();
    }
    @FXML public void onSetClicked(ActionEvent actionEvent) {
        sendManualCodeToParentController();
        removeAllChoices();
    }

    private void sendManualCodeToParentController() {
        CodeObj code = new CodeObj();

        Collections.reverse(rotorIDs);
        code.setRotorIDs(this.rotorIDs);
        Collections.reverse(rotorIDs);

        Collections.reverse(rotorPositions);
        code.setRotorPos(this.rotorPositions);
        Collections.reverse(rotorPositions);

        code.setPlugs(plugsComponentController.getSelectedPlugs());
        code.setReflectorID(reflectorComboBox.valueProperty().getValue());

        mainApplicationController.handleManualSet(code);

        removeAllChoices();
        codeConfigurationAccordion.setDisable(true);
        setByManualButton.setDisable(false);
    }

    private void removeAllChoices() {
        rotorConfigComponentController.removeChoices();
        plugsComponentController.removeChoices();
        reflectorComboBox.valueProperty().set(null);
        rotorIDs = null;
        rotorPositions = null;
    }

    private void resetConfigComponent(){
        codeConfigurationAccordion.setDisable(true);
        setByManualButton.setDisable(false);

        plugsComponentController.reset();

        rotorConfigComponentController.reset();
        rotorIDs = null;
        rotorPositions = null;

        reflectorComboBox.setItems(FXCollections.observableArrayList(new ArrayList<>()));
        reflectorComboBox.valueProperty().set(null);
    }

    public void onRotorsCompleted(List<Integer> rotorIDs, List<Character> rotorPositions){
        //this method will be called from within the rotors controller
        //when the rotors have all been chosen ("this is the listener")
        this.rotorIDs = rotorIDs;
        this.rotorPositions = rotorPositions;
    }

    public void handleFileLoaded(List<Character> keys, int rotorsRequired,
                                 int totalRotorAmount, int reflectorAmount){
        setByRandomButton.setDisable(false);
        setByManualButton.setDisable(false);
        clearButton.disableProperty().bind(setByManualButton.disabledProperty().not());
        
        resetConfigComponent();
        reflectorComboBox.setItems(FXCollections
                .observableArrayList(ReflectorID.getListToLimit(reflectorAmount)));
        plugsComponentController.setComponent(keys);
        rotorConfigComponentController.setComponent(rotorsRequired, totalRotorAmount, keys);
    }

    public void changeTheme(Object cssPrefix) {
        rotorConfigComponentController.changeTheme(cssPrefix);
        plugsComponentController.changeTheme(cssPrefix);
    }
}
