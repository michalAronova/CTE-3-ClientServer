package alliesClient.alliesMain;

import DTO.contest.Contest;
import DTO.team.Team;
import alliesClient.alliesApp.AlliesAppController;
import alliesClient.components.missionSizeChooser.MissionSizeChooserController;
import alliesClient.components.rivalAlliesRefresher.RivalAlliesRefresher;
import clientUtils.MainAppController;
import clientUtils.activeTeams.ActiveTeamsComponentController;
import clientUtils.candidatesComponent.CandidatesComponentController;
import clientUtils.contestDetails.ContestDetailsController;
import alliesClient.components.activeAgentsDisplay.ActiveAgentsDisplayController;
import alliesClient.components.activeContests.ActiveContestsController;
import alliesClient.components.agentDisplay.AgentDisplayController;
import alliesClient.components.missionsProgress.MissionsProgressController;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.GridPane;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import util.Constants;
import util.http.HttpClientUtil;

import java.io.IOException;
import java.util.List;
import java.util.Timer;

import static parameters.ConstantParams.DESIRED_UBOAT;
import static util.Constants.GSON_INSTANCE;
import static util.Constants.REFRESH_RATE;

public class AlliesMainController implements MainAppController {
    @FXML private GridPane contestOnGrid;
    @FXML private Label usernameLabel;

    //dashboard tab
    @FXML private ActiveContestsController activeContestsController;
    @FXML private AgentDisplayController agentDisplayController;

    //contest tab
    @FXML private CandidatesComponentController candidatesComponentController;
    @FXML private ActiveTeamsComponentController activeTeamsController;
    @FXML private ContestDetailsController contestDetailsController;
    @FXML private ActiveAgentsDisplayController activeAgentsDisplayController;
    @FXML private MissionsProgressController missionsProgressController;
    @FXML private MissionSizeChooserController missionSizeChooserController;
    @FXML private TabPane competitionTabPane;
    @FXML private Tab contestTab;
    private AlliesAppController alliesAppController;

    private Contest chosenContest;
    private BooleanProperty isCompetitionOn;
    private RivalAlliesRefresher refresher;
    private Timer timer;

    public AlliesMainController(){
        isCompetitionOn = new SimpleBooleanProperty(false);
    }

    public void setMainController(AlliesAppController alliesAppController) {
        this.alliesAppController = alliesAppController;
        if(usernameLabel != null) {
            usernameLabel.textProperty().bind(alliesAppController.getUsernameProperty());
        }
    }

    @FXML
    public void initialize() {
        if(activeContestsController != null && agentDisplayController != null
                && candidatesComponentController != null && activeTeamsController != null
                && contestDetailsController != null && activeAgentsDisplayController != null
                && missionsProgressController != null && missionSizeChooserController != null){
            activeContestsController.setMainApplicationController(this);
            agentDisplayController.setMainApplicationController(this);
            candidatesComponentController.setMainApplicationController(this);
            activeTeamsController.setMainApplicationController(this);
            contestDetailsController.setMainApplicationController(this);
            activeAgentsDisplayController.setMainApplicationController(this);
            missionsProgressController.setMainApplicationController(this);
            missionSizeChooserController.setAlliesMainController(this);
        }
        else{
            System.out.println("null component in allies main");
        }
    }

    public void chooseContest(String boatName) {
        registerToUBoatRequest(boatName);
        //startAlliesRivalsRefresher();
        if(chosenContest != null){
            competitionTabPane.getSelectionModel().select(contestTab);
            //startAlliesRivalsRefresher();
            /*//TODO:update contest in UI maybe move to registerToUboat
            Platform.runLater(() -> {
                        codeObjDisplayController.onCodeChosen(currentCode);
                    });
             */
        }
        else{
            System.out.println("not registered yet");
        }
        //dispatch request to server...
        //on success, switch to the contest tab and fill the data there
        //should get contest data back in response ?
        //chosenContest = ?
    }

    public void registerToUBoatRequest(String boatName) {
        String finalUrl = HttpUrl
                .parse(Constants.REGISTER_TO_UBOAT)
                .newBuilder()
                .addQueryParameter(DESIRED_UBOAT, boatName)
                .build()
                .toString();
        HttpClientUtil.runAsync(finalUrl, new Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try (ResponseBody responseBody = response.body()) {
                    if (response.code() == 200) {
                        chosenContest = GSON_INSTANCE.fromJson(responseBody.string(), Contest.class);
                        System.out.println(chosenContest);
                    } else {
                        System.out.println("Error! " + responseBody.string());
                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println("failed");
            }
        });
    }


    public boolean getIsCompetitionOn() {
        return isCompetitionOn.get();
    }

    public BooleanProperty isCompetitionOnProperty() {
        return isCompetitionOn;
    }

    public void missionSizeChosen(int missionSize) {
        //dispatch to server
        //both to update mission size
        //and notify that this ally is ready for battle!
        //start the requests for information to fill the various tables in the contest tab
    }
    public void startAlliesRivalsRefresher() {
        System.out.println("here");
        refresher = new RivalAlliesRefresher(this::replaceAll, chosenContest.getActive() );
        timer = new Timer();
        timer.schedule(refresher, REFRESH_RATE, REFRESH_RATE);
    }
    public void replaceAll(List<Team> teams){
        activeTeamsController.getDataList().clear();
        activeTeamsController.getDataList().addAll(teams);
        //contestDetailsController.setInGameLabel(String.format("%d / %d", teams.size(), chosenContest.getTotalRequiredTeams()));
        contestDetailsController.setInGameAndRequired(teams.size(), chosenContest.getTotalRequiredTeams());
    }
}
