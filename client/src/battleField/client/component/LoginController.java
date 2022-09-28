package battleField.client.component;

import battleField.client.component.global.entityChooser.EntityChooserController;
import battleField.client.component.mainApp.MainAppController;

public interface LoginController {
    void setMainController(MainAppController mainController);

    void onUsernameSelected(String text);
}
