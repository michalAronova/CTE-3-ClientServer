package clientUtils.chat.chatroom;

import clientUtils.MainAppController;
import clientUtils.chat.chatarea.ChatAreaController;
import clientUtils.chat.users.UsersListController;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.io.Closeable;
import java.io.IOException;

public class ChatRoomMainController implements Closeable{

    @FXML private VBox usersListComponent;
    @FXML private UsersListController usersListComponentController;
    @FXML private GridPane chatAreaComponent;
    @FXML private ChatAreaController chatAreaComponentController;

    private MainAppController chatAppMainController;

    @FXML
    public void initialize() {
        usersListComponentController.startListRefresher();
        chatAreaComponentController.startListRefresher();
    }

    @Override
    public void close(){
        usersListComponentController.close();
        chatAreaComponentController.close();
    }

    public void setInActive() {
        try {
            usersListComponentController.close();
            chatAreaComponentController.close();
        } catch (Exception ignored) {}
    }

    public void setChatAppMainController(MainAppController chatAppMainController) {
        this.chatAppMainController = chatAppMainController;
    }
}
