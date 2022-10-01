package clientUtils.codeConfigurationComponent.rotorConfigurationComponent;

import components.codeConfigurationComponent.CodeConfigComponentController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.*;
import javafx.scene.image.WritableImage;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RotorConfigComponentController {

    @FXML public GridPane rootGrid;
    @FXML private FlowPane rotorChoicesFlowPane;
    @FXML private HBox rotorConfigHBox;

    private final List<VBox> singleRotorComponentList;
    private List<Integer> rotorIDs;
    private List<Character> rotorPos;

    Map<String,Label> ID2Label;
    private int rotorsSet = 0;
    private CodeConfigComponentController codeConfigComponentController;
    private final String DEFAULT_ROTOR_ID = "ID";
    private final String myStyleSheet = "rotorConfigComponent";

    public RotorConfigComponentController(){
        singleRotorComponentList = new ArrayList<>();
        ID2Label = new HashMap<>();
    }

    @FXML public void initialize(){
        //rotorIDComboBox.setOnAction(e -> onComboBoxChoice());
    }

    public void setCodeConfigController(CodeConfigComponentController codeConfigComponentController){
        this.codeConfigComponentController = codeConfigComponentController;
    }

    public void setComponent(int rotorsRequired, int totalRotorAmount, List<Character> keys){
        rotorIDs = Stream.generate(() -> 0)
                            .limit(rotorsRequired)
                            .collect(Collectors.toList());
        rotorPos = Stream.generate(() -> keys.get(0))
                            .limit(rotorsRequired)
                            .collect(Collectors.toList());

        for(int i = 0; i < rotorsRequired; i++) {
            VBox vBox = createRotorConfigVBox(keys, i);
            rotorConfigHBox.getChildren().add(vBox);
            singleRotorComponentList.add(i, vBox);
        }

        for (int i = 1; i <= totalRotorAmount; i++) {
            Label label = createDraggableRotorLabel(i);
            ID2Label.put(i + "", label);
            rotorChoicesFlowPane.getChildren().add(label);
        }

        //setRotorsComboBox(totalRotorAmount);
    }
//    private void setRotorsComboBox(int totalRotorAmount) {
//        List<Integer> comboBoxValues = new ArrayList<>();
//        for(int i = 1; i <= totalRotorAmount; i++) {
//            comboBoxValues.add(i);
//        }
//
//        ObservableList<Integer> obList = FXCollections.observableList(comboBoxValues);
//        rotorIDComboBox.getItems().clear();
//        rotorIDComboBox.getItems().addAll(comboBoxValues);
//    }
//
//    private void onComboBoxChoice(){
//        setOptionRotorLabel(rotorIDComboBox.getValue());
//        rotorIDChosenLabel.setDisable(false);
//    }
    private VBox createRotorConfigVBox(List<Character> keys, int index){
        //rotor ID label
        Label rotorIDLabel = createRotorIDDragBox(index);

        //undoBtn
        AnchorPane buttonAnchorPane = createButtonAnchorPane(rotorIDLabel, index);

        //spinner
        Spinner<String> positionSpinner = createPositionSpinner(keys, index);

        //VBox
        VBox vBox = new VBox(buttonAnchorPane, rotorIDLabel, positionSpinner);
        setRotorVBoxLayout(rotorIDLabel, positionSpinner, vBox);

        return vBox;
    }

    private void setRotorVBoxLayout(Label rotorIDLabel, Spinner<String> positionSpinner, VBox vBox) {
        vBox.setAlignment(Pos.CENTER);
        vBox.setLayoutX(12.0);
        vBox.setLayoutY(12.0);
        vBox.setPadding(new Insets(5, 5, 5, 5));
        VBox.setMargin(rotorIDLabel,new Insets(10, 10, 10, 10));
        VBox.setMargin(positionSpinner,new Insets(5, 5, 5, 5));
    }

    private Spinner<String> createPositionSpinner(List<Character> keys, int index) {
        Spinner<String> positionSpinner = new Spinner<>();
        List<String> keysAsString = new ArrayList<>();
        keys.forEach(k -> keysAsString.add(0, k.toString()));
        ObservableList<String> values = FXCollections.observableArrayList(keysAsString);
        positionSpinner
                .setValueFactory(new SpinnerValueFactory
                                        .ListSpinnerValueFactory<String>(values));

        positionSpinner.setEditable(true);

        positionSpinner.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                positionSpinner.increment(0); //to commit the change on focus lost
            }
        });

        positionSpinner.valueProperty().addListener(((observable, oldValue, newValue) -> {
            if(newValue == null || newValue.length() != 1 || !keys.contains(newValue.toUpperCase().charAt(0))){
                positionSpinner.getValueFactory().setValue(oldValue);
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setHeaderText("Invalid Input");
                errorAlert.setContentText(String.format(" '%s' is invalid. %n Valid values are: %s", newValue, keys));
                errorAlert.showAndWait();
            }
            else{
                positionSpinner.getValueFactory().setValue(newValue.toUpperCase());
                rotorPos.set(index, newValue.toUpperCase().charAt(0));
            }
        }));

        rotorPos.set(index, positionSpinner.getValue().charAt(0));

        positionSpinner.setPrefHeight(40);
        positionSpinner.setPrefWidth(70);
        positionSpinner.setMaxHeight(40);
        positionSpinner.setMaxWidth(70);
        positionSpinner.setMinHeight(40);
        positionSpinner.setMinWidth(70);
        positionSpinner.setPadding(new Insets(5, 5, 5, 5));
        return positionSpinner;
    }

    private AnchorPane createButtonAnchorPane(Label rotorIDLabel, int index) {
        AnchorPane buttonAnchorPane = new AnchorPane();
        Button undoButton = new Button("x");
        undoButton.getStyleClass().add("xButton");
        undoButton.setOnAction(e -> handleUndoButton(rotorIDLabel, index));
        undoButton.setLayoutX(55.0);
        undoButton.setMaxHeight(22.0);
        undoButton.setMaxWidth(22.0);
        undoButton.setMinHeight(22.0);
        undoButton.setMinWidth(22.0);
        undoButton.setPrefHeight(22.0);
        undoButton.setPrefWidth(22.0);
        undoButton.setMnemonicParsing(false);
        buttonAnchorPane.getChildren().add(undoButton);
        return buttonAnchorPane;
    }

    private void handleUndoButton(Label rotorID, int index) {
        if(!rotorID.getText().equals(DEFAULT_ROTOR_ID)) {
            //setOptionRotorLabel(Integer.parseInt(rotorID.getText())); //undo rotor id choice
            ID2Label.get(rotorID.getText()).setDisable(false);
            rotorRemoved(index, Integer.parseInt(rotorID.getText()));
        }
        rotorID.setText(DEFAULT_ROTOR_ID); //set the chosen id to default id
        rotorID.getStyleClass().clear();
        rotorID.getStyleClass().add("rotorIDDragBox");
    }

    public void reset(){
        singleRotorComponentList.forEach(component ->
                rotorConfigHBox.getChildren().remove(component));
        rotorChoicesFlowPane.getChildren().clear();
        ID2Label.clear();
        singleRotorComponentList.clear();
        rotorsSet = 0;
    }

    private Label createDraggableRotorLabel(int number){
        Label label = new Label(number + "");
        label.setId("draggableRotorLabel"+number);
        label.getStyleClass().add("draggable-rotor-label");

        label.setOnDragDetected((event) -> {
            WritableImage snapshot = label.snapshot(new SnapshotParameters(), null);
            Dragboard db = label.startDragAndDrop(TransferMode.ANY);

            ClipboardContent content = new ClipboardContent();
            content.putString(number + "");
            db.setContent(content);
            db.setDragView(snapshot, snapshot.getWidth() / 2, snapshot.getHeight() / 2);

            event.consume();
        });

        label.setOnDragDone((event) -> {
            if (event.getTransferMode() == TransferMode.MOVE) {
                label.setDisable(true);
            }
            event.consume();
        });

        label.setAlignment(Pos.CENTER);
        label.setPrefWidth(35);
        label.setPrefHeight(35);

        return label;
    }
//    private void setOptionRotorLabel(int number) {
//        rotorIDChosenLabel.setText(number +"");
//
//        rotorIDChosenLabel.setOnDragDetected((event) -> {
//            WritableImage snapshot = rotorIDChosenLabel.snapshot(new SnapshotParameters(), null);
//            Dragboard db = rotorIDChosenLabel.startDragAndDrop(TransferMode.ANY);
//
//            ClipboardContent content = new ClipboardContent();
//            content.putString(number + "");
//            db.setContent(content);
//            db.setDragView(snapshot, snapshot.getWidth() / 2, snapshot.getHeight() / 2);
//
//            event.consume();
//        });
//
//        rotorIDChosenLabel.setOnDragDone((event) -> {
//            if (event.getTransferMode() == TransferMode.MOVE) {
//                rotorIDChosenLabel.setText("");
//                rotorIDChosenLabel.setDisable(true);
//            }
//            event.consume();
//        });
//    }

    private Label createRotorIDDragBox(int index) {
        Label dragBox = new Label(DEFAULT_ROTOR_ID);
        dragBox.setCenterShape(true);
        dragBox.setPadding(new Insets(10, 10, 10, 10));
        dragBox.setAlignment(Pos.CENTER_RIGHT);
        dragBox.getStyleClass().add("rotorIDDragBox");
        dragBox.setStyle("-fx-font-fill: black");
        dragBox.setOnDragOver((event) -> {
            if (event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            }
            event.consume();
        });

        dragBox.setOnDragEntered((event) -> {
            if (event.getDragboard().hasString()) {
                dragBox.getStyleClass().remove("rotorIDDragBox");
                dragBox.getStyleClass().add("onDragEntered");

            }
            event.consume();
        });

        dragBox.setOnDragExited((event) -> {
            dragBox.getStyleClass().clear();
            dragBox.getStyleClass().add("rotorIDDragBox");

            event.consume();
        });

        dragBox.setOnDragDropped((event) -> {
            Dragboard db = event.getDragboard();
            boolean success = false;
            if (db.hasString()) {
                handleUndoButton(dragBox, index);
                dragBox.setText(db.getString());
                success = true;
                rotorSet(index, Integer.parseInt(db.getString()));
            }
            dragBox.getStyleClass().clear();
            dragBox.getStyleClass().add("draggable-rotor-label");
            event.setDropCompleted(success);
            event.consume();
        });

        return dragBox;
    }

    private void rotorRemoved(int index, int rotorID){
        rotorIDs.set(index, 0);
        rotorsSet--;
        codeConfigComponentController.getRotorsFilledProperty().setValue(false);
    }
    private void rotorSet(int index, int rotorID){
        rotorIDs.set(index, rotorID);
        rotorsSet++;
        if(rotorsSet == singleRotorComponentList.size()){
            codeConfigComponentController.onRotorsCompleted(rotorIDs, rotorPos);
            codeConfigComponentController.getRotorsFilledProperty().setValue(true);
        }
    }

    public void removeChoices() {
        rotorsSet = 0;

        ID2Label.forEach((id, label) -> label.setDisable(false));
        singleRotorComponentList.forEach(vBox -> {
            for (int i = 0; i < vBox.getChildren().size(); i++) {
                if(vBox.getChildren().get(i) instanceof Label){
                    ((Label) vBox.getChildren().get(i)).setText(DEFAULT_ROTOR_ID);
                    ((Label) vBox.getChildren().get(i)).getStyleClass().clear();
                    ((Label) vBox.getChildren().get(i)).getStyleClass().add("rotorIDDragBox");
                }
            }
        });

        codeConfigComponentController.getRotorsFilledProperty().setValue(false);
    }

    public void changeTheme(Object cssPrefix) {
        rootGrid.getStylesheets().clear();
        if(cssPrefix != null){
            String css = cssPrefix + myStyleSheet;
            rootGrid.getStylesheets()
                    .add(getClass().getClassLoader().getResource(String.format("%s.css", css)).toExternalForm());
        }
    }
}
