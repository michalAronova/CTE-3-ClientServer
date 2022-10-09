package uBoatClient.components.codeConfigurationComponent.plugsComponent;

import uBoatClient.components.codeConfigurationComponent.CodeConfigComponentController;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Pair;

import java.util.*;

public class PlugsComponentController {
    @FXML public SplitPane rootSplitPane;
    @FXML
    private FlowPane plugsFlowPane;
    @FXML private VBox PlugsVBox;
    @FXML private Button addPlugsButton;
    private Map<String, ToggleButton> toggleButtons;
    private Set<String> currentPlug;
    private Set<String> allSelectedPlugs;
    private int plugsSelected = 0;
    private Map<String, String> selectedDoublePlugs;
    private CodeConfigComponentController codeConfigComponentController;

    private final String myStyleSheet = "plugsComponent";

    @FXML public void initialize(){
        toggleButtons = new HashMap<>();
        currentPlug = new HashSet<>();
        allSelectedPlugs = new HashSet<>();
        selectedDoublePlugs = new HashMap<>();


        addPlugsButton.setDisable(true);
    }

    public void setComponent(List<Character> ABC){
        if(!plugsFlowPane.getChildren().isEmpty()){
            plugsFlowPane.getChildren().clear();
            toggleButtons.clear();
        }

        for (Character c: ABC) {
            ToggleButton TG = new ToggleButton(c.toString());
            TG.getStyleClass().add("keyButton");
            toggleButtons.put(TG.getText(), TG);
            TG.setOnAction(e -> onPlugButtonToggled(TG.getText()));
            plugsFlowPane.getChildren().add(TG);
        }
    }

    public void onAddPlugsClicked(){
        if(currentPlug.size() != 2){
            return;
        }

        allSelectedPlugs.addAll(currentPlug);
        String[] current = currentPlug.toArray(new String[2]);
        selectedDoublePlugs.put(current[0], current[1]);

        PlugsVBox.getChildren().add(createHBOXPlug(current[0], current[1]));

        resetChoiceButtons();
        plugsSelected = 0;
        currentPlug.clear();
    }

    public void onPlugButtonToggled(String id){
        if(toggleButtons.get(id).isSelected()){
            plugsSelected++;
            currentPlug.add(id);
        }
        else{
            plugsSelected--;
            currentPlug.remove(id);
        }
        if(plugsSelected == 2){
            toggleButtons.forEach((c, button) -> {
                if(!button.isSelected()){
                    button.setDisable(true);
                }
            });
            addPlugsButton.setDisable(false);
        }
        else{
            resetChoiceButtons();
        }
    }

    private void resetChoiceButtons() {
        addPlugsButton.setDisable(true);
        toggleButtons.forEach((c, button) ->
                button.setDisable(allSelectedPlugs.contains(c)));
    }

    private HBox createHBOXPlug(String plug1Name, String plug2Name){
        Label plug1 = new Label(plug1Name);
        Label plug2 = new Label(plug2Name);
        Label separator = new Label("|");
        plug1.setStyle("-fx-text-fill: black;");
        plug2.setStyle("-fx-text-fill: black;");
        separator.setStyle("-fx-text-fill: black;");
        Button removePlugButton = new Button("X");
        removePlugButton.getStyleClass().add("xButton");
        removePlugButton.getStyleClass().add("removePlugButton");
        HBox hBox = new HBox(plug1, separator,plug2,removePlugButton);
        removePlugButton.setOnAction(e-> onRemovePlugClicked(plug1Name, plug2Name, hBox));
        hBox.setSpacing(5);
        hBox.setAlignment(Pos.CENTER);
        hBox.getStyleClass().add("plugHBox");
        return hBox;
    }

    public void onRemovePlugClicked(String plug1Name, String plug2Name, HBox parent){
        allSelectedPlugs.remove(plug1Name);
        allSelectedPlugs.remove(plug2Name);

        if(selectedDoublePlugs.containsKey(plug1Name)){
            selectedDoublePlugs.remove(plug1Name);
        }
        else{
            selectedDoublePlugs.remove(plug2Name);
        }

        resetChoiceButtons();
        toggleButtons.forEach((c, button) -> {
            if(!button.isDisabled()){
                button.setSelected(false);
            }
        });
        plugsSelected = 0;
        currentPlug.clear();
        PlugsVBox.getChildren().remove(parent);
    }

    public void setCodeConfigController(CodeConfigComponentController codeConfigComponentController){
        this.codeConfigComponentController = codeConfigComponentController;
    }

    public void reset(){
        removeChoices();
        PlugsVBox.getChildren().clear();
    }

    public List<Pair<Character, Character>> getSelectedPlugs(){
        List<Pair<Character, Character>> plugs = new ArrayList<>();
        selectedDoublePlugs.forEach((plug1, plug2) -> {
            plugs.add(new Pair<>(plug1.charAt(0), plug2.charAt(0)));
        });
        return plugs;
    }

    public void removeChoices() {
        PlugsVBox.getChildren().clear();
        toggleButtons.forEach((str, button) -> {
            button.setDisable(false);
            button.setSelected(false);
        });

        currentPlug.clear();
        allSelectedPlugs.clear();
        plugsSelected = 0;
        selectedDoublePlugs.clear();
        addPlugsButton.setDisable(true);
    }

    public void changeTheme(Object cssPrefix) {
        rootSplitPane.getStylesheets().clear();
        if(cssPrefix != null){
            String css = cssPrefix + myStyleSheet;
            rootSplitPane.getStylesheets()
                    .add(getClass().getClassLoader().getResource(String.format("%s.css", css)).toExternalForm());
        }
    }
}
