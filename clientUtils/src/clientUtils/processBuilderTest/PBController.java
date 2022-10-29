package clientUtils.processBuilderTest;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.IOException;

public class PBController {
    @FXML private Button agentButton;

    public void startAgent(ActionEvent actionEvent) {
        String ally = "France";
        ProcessBuilder pb;
        pb = new ProcessBuilder("Java","-jar","agentClient.jar", ally);

        try {
            pb.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
