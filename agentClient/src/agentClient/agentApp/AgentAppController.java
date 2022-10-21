package agentClient.agentApp;

import agentClient.agentMain.AgentMainController;
import agentClient.refreshers.AlliesAvailableRefresher;
import clientUtils.LoginController;
import clientUtils.chooseNameComponent.ChooseNameComponentController;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.util.converter.IntegerStringConverter;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import util.Constants;
import util.http.HttpClientUtil;

import java.io.Closeable;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.function.UnaryOperator;

import static parameters.ConstantParams.*;
import static util.Constants.AGENT_MAIN_FXML_RESOURCE_LOCATION;
import static util.Constants.REFRESH_RATE;

public class AgentAppController implements LoginController, Closeable {
    @FXML private VBox vBox;
    @FXML private Label errorLabel;
    @FXML private ScrollPane mainScrollPane;
    @FXML private Slider threadSlider;
    @FXML private VBox allyRadioButtonVBox;
    @FXML private TextField missionPullTextField;
    @FXML private Button registerButton;
    private final StringProperty errorMessage;
    private ToggleGroup allies;
    private AgentMainController agentMainController;
    @FXML ChooseNameComponentController chooseNameComponentController;

    private final StringProperty username;
    private String ally;
    private BooleanProperty isValidAgent;
    private final StringProperty allyChosen;
    private final Map<String,Toggle> allyname2toggle;

    private AlliesAvailableRefresher alliesAvailableRefresher;
    private Timer alliesTimer;

    public AgentAppController(){
        errorMessage = new SimpleStringProperty("");
        username = new SimpleStringProperty("");
        allyChosen = new SimpleStringProperty("");
        isValidAgent = new SimpleBooleanProperty(false);
        allyname2toggle = new HashMap<>();
    }

    @FXML public void initialize(){
        if(chooseNameComponentController != null){
            chooseNameComponentController.setParentController(this);
            chooseNameComponentController.hideRegisterButton();
        }
        else {
            System.out.println("choose name component is null");
        }

        isValidAgent.addListener((observable, oldValue, newValue) -> {
            if(newValue){
                stopAlliesRefresher();
                switchToMain();
            }
        });

        errorLabel.textProperty().bind(errorMessage);

        UnaryOperator<TextFormatter.Change> integerFilter = change -> {
            String newText = change.getControlNewText();
            if (newText.matches("-?([1-9][0-9]*)?")) {
                return change;
            }
            return null;
        };

        missionPullTextField.setTextFormatter(
                new TextFormatter<>(new IntegerStringConverter(), 0, integerFilter));

        allies = new ToggleGroup();
        allyRadioButtonVBox.getChildren().clear();

        username.bind(chooseNameComponentController.textFieldProperty());
        allyChosen.bind(Bindings.createStringBinding(() ->
                {
                    Toggle toggleChosen = allies.selectedToggleProperty().getValue();
                    return toggleChosen == null ? "" : toggleChosen.getUserData().toString();
                },
                allies.selectedToggleProperty()));

        registerButton.disableProperty().bind(Bindings.createBooleanBinding(() ->
                        username.getValue().equals("") ||
                        missionPullTextField.textProperty().getValue().equals("") ||
                        allies.selectedToggleProperty().getValue() == null,
                username, missionPullTextField.textProperty(), allies.selectedToggleProperty()));

        startAlliesRefresher();
    }

    private void startAlliesRefresher() {
        alliesAvailableRefresher = new AlliesAvailableRefresher(this::updateAllyList);
        alliesTimer = new Timer();
        alliesTimer.schedule(alliesAvailableRefresher, REFRESH_RATE, REFRESH_RATE);
    }

    private void stopAlliesRefresher() {
        if (alliesAvailableRefresher != null && alliesTimer != null) {
            alliesAvailableRefresher.cancel();
            alliesTimer.cancel();
        }
    }

    private void updateAllyList(Set<String> allies) {
        for (String ally: allyname2toggle.keySet()) { //to remove old
            if(!allies.contains(ally)){
                removeToggle(ally, allyname2toggle.get(ally));
            }
        }
        for (String ally: allies) { //to add new
            if(!allyname2toggle.containsKey(ally)){
                addAllyRadioButton(ally);
            }
        }
    }

    private void removeToggle(String allyName, Toggle toggle) {
        toggle.setSelected(false);
        if(allies.getSelectedToggle().equals(toggle)){
            allies.selectToggle(null);
        }
        allyRadioButtonVBox.getChildren().remove(toggle);
        allyname2toggle.remove(allyName);
    }

    private void addAllyRadioButton(String allyUsername){
        RadioButton ally = new RadioButton(allyUsername);
        ally.setToggleGroup(allies);
        ally.setUserData(allyUsername);
        allyRadioButtonVBox.getChildren().add(ally);
    }
    @FXML void onRegisterClicked(ActionEvent event) {
        if(username.getValue() == null || username.getValue().isEmpty()){
            chooseNameComponentController.errorMessageProperty().set("Error: Must Choose username");
            return;
        }
        if(allies.getSelectedToggle() == null){
            errorMessage.set("Error: must select an ally to join");
            return;
        }
        ally = allies.getSelectedToggle().getUserData().toString();
        int threadCount = (int) threadSlider.getValue();
        int missionPull = -1;
        try {
            missionPull = Integer.parseInt(missionPullTextField.getText());
            if(missionPull <= 0) throw new NumberFormatException();
        }
        catch(NumberFormatException e){
            errorMessage.set("Error: mission pull must be positive integer");
            return;
        }
        System.out.println("username: "+ username.getValue());
        System.out.println("mission pull: "+ missionPull);
        System.out.println("thread count: "+ threadCount);
        System.out.println("ally: "+ ally);

        //dispatch request to server to login as agent
        registrationRequest(username.getValue(), missionPull, threadCount, ally);
    }

    private void registrationRequest(String username, int missionPull, int threadCount, String ally) {
        String finalUrl = HttpUrl
                .parse(Constants.AGENT_LOGIN_PAGE)
                .newBuilder()
                .addQueryParameter(ALLIES_JOINED, ally)
                .addQueryParameter(USERNAME, username)
                .addQueryParameter(MISSION_AMOUNT_PULL, String.valueOf(missionPull))
                .addQueryParameter(THREAD_COUNT, String.valueOf(threadCount))
                .build()
                .toString();

        HttpClientUtil.runAsync(finalUrl, new Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try (ResponseBody responseBody = response.body()) {
                    if (response.code() == 200) {
                        Platform.runLater(() -> {
                            usernameProperty().set(username);
                            isValidAgent.set(true);
                        });
                    }
                    else {
                        String responseBodyString = responseBody.string();
                        Platform.runLater(() -> errorMessage.set(responseBodyString));
                    }
                }
            }
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println("request failes");
                Platform.runLater(() -> errorMessage.set("Request failed"));
            }
        });
    }

    private void switchToMain() {
        URL mainPageUrl = getClass().getResource(AGENT_MAIN_FXML_RESOURCE_LOCATION);
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(mainPageUrl);
            Node main = fxmlLoader.load();
            agentMainController = fxmlLoader.getController();
            agentMainController.setMainController(this);
            agentMainController.alliesNameProperty().set(ally);
            vBox.getChildren().clear();
            vBox.getChildren().add(main);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUsernameSelected(String username) {

    }

    public StringProperty usernameProperty() {
        return username;
    }

    @Override
    public void close() {
        stopAlliesRefresher();
        agentMainController.close();
    }
}
