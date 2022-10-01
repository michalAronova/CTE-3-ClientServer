package clientUtils.codeObjDisplayComponent;

import DTO.codeObj.CodeObj;
import application.MainApplicationController;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Pair;

public class CodeObjDisplayComponentController {
    @FXML public ScrollPane rootScrollPane;
    @FXML private AnchorPane reflectorDataAnchorPane;
    @FXML private HBox plugsConfiguredHBox;
    @FXML private FlowPane plugsFlowPane;
    @FXML private HBox rotorsDataHBox;
    @FXML private Label HeaderLabel;
    private MainApplicationController mainApplicationController;
    private final String myStyleSheet = "codeObjDisplay";

    @FXML public void initialize(){
    }

    public void setMainApplicationController(MainApplicationController mainApplicationController){
        this.mainApplicationController = mainApplicationController;
        mainApplicationController.getCodeChangesProperty()
                .addListener(e -> onCodeChosen(mainApplicationController.getCurrentCode()));
    }

    public void onCodeChosen(CodeObj code){
        rotorsDataHBox.getChildren().clear();
        reflectorDataAnchorPane.getChildren().clear();
        plugsFlowPane.getChildren().clear();

        if(code == null) return;

        for (int i = code.getID2PositionList().size() - 1; i >= 0; i--) {
            int ID = code.getID2PositionList().get(i).getKey();
            Character pos = code.getID2PositionList().get(i).getValue();
            int notch = code.getNotchRelativeLocations()
                    .get(code.getID2PositionList().get(i).getKey());
            rotorsDataHBox.getChildren().add(createRotorVBox(ID, pos, notch));
        }

        reflectorDataAnchorPane.getChildren().add(createReflectorLabel(code.getReflectorID()));
        code.getPlugs().forEach((p) -> plugsFlowPane.getChildren().add(createPlugLabel(p)));
        //code.getPlugs().forEach((p) -> plugsConfiguredHBox.getChildren().add(createPlugLabel(p)));
    }

    private HBox createPlugLabel(Pair<Character, Character> newPlug) {
        Label newPlugLabel = new Label();
        newPlugLabel.setText(newPlug.getKey().toString() + " | " + newPlug.getValue());
        newPlugLabel.setPadding(new Insets(5, 5, 5, 5));
        newPlugLabel.setId("plugConfiguredLabel");

        HBox hbox = new HBox(newPlugLabel);
        HBox.setMargin(newPlugLabel, new Insets(5, 5, 5, 0));
        return hbox;
    }
    private Label createReflectorLabel(String reflectorID){
        Label newReflectorLabel = new Label();
        newReflectorLabel.setText(reflectorID);
        newReflectorLabel.setPadding(new Insets(5, 5, 5, 5));
        return newReflectorLabel;
    }
    private VBox createRotorVBox(Integer Id, Character position, Integer notch){
        //Id label
        Label IdLabel = new Label();
        IdLabel.setText(Id.toString());
        IdLabel.setPadding(new Insets(3, 3, 0, 0));

        //Id label
        Label positionLabel = new Label();
        positionLabel.setText(position.toString());
        positionLabel.setPadding(new Insets(3, 3, 0, 0));

        //Id label
        Label notchLabel = new Label();
        notchLabel.setText(notch.toString());
        notchLabel.setPadding(new Insets(3, 3, 0, 0));

        //VBox
        VBox vBox = new VBox(IdLabel, positionLabel, notchLabel);
        vBox.setPadding(new Insets(3, 3, 3, 3));
        HBox.setMargin(vBox, new Insets(3, 3, 3, 3));
        return vBox;
    }
    public void setHeaderLabelText(String headerLabel) {
        HeaderLabel.setText(headerLabel);
    }

    public void changeTheme(Object cssPrefix) {
        rootScrollPane.getStylesheets().clear();
        if(cssPrefix != null){
            String css = cssPrefix + myStyleSheet;
            rootScrollPane.getStylesheets()
                    .add(getClass().getClassLoader().getResource(String.format("%s.css", css)).toExternalForm());
        }
    }
}
