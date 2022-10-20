package alliesClient.alliesMain;

import DTO.contest.Contest;
import DTO.dmProgress.DMProgress;
import DTO.team.Team;
import alliesClient.alliesApp.AlliesAppController;
import alliesClient.components.activeAgentsDisplay.ActiveAgentsRefresher;
import alliesClient.components.missionSizeChooser.MissionSizeChooserController;
import alliesClient.components.missionsProgress.MissionsProgressRefresher;
import alliesClient.refreshers.CandidatesRefresher;
import alliesClient.refreshers.IsCompetitionOnRefresher;
import alliesClient.refreshers.RivalAlliesRefresher;
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
    private BooleanProperty registeredToContest;
    private BooleanProperty isCompetitionOn;
    private BooleanProperty isAllyReady;
    private RivalAlliesRefresher rivalAlliesRefresher;
    private CandidatesRefresher candidatesRefresher;
    private ActiveAgentsRefresher activeAgentsRefresher;
    private MissionsProgressRefresher missionProgressRefresher;
    private IsCompetitionOnRefresher competitionOnRefresher;
    private Timer rivalAlliesTimer;
    private Timer candidatesTimer;
    private Timer agentsTimer;
    private Timer missionProgressTimer;
    private Timer CompetitionOnTimer;

    public AlliesMainController(){
        isCompetitionOn = new SimpleBooleanProperty(false);
        isAllyReady = new SimpleBooleanProperty(false);
        registeredToContest = new SimpleBooleanProperty(false);
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
            candidatesComponentController.changeColumnToAgent();
            
            activeTeamsController.setMainApplicationController(this);
            contestDetailsController.setMainApplicationController(this);
            activeAgentsDisplayController.setMainApplicationController(this);
            missionsProgressController.setMainApplicationController(this);
            missionSizeChooserController.setAlliesMainController(this);
        }
        else{
            System.out.println("null component in allies main");
        }
        chosenContest = null;
    }

    public void chooseContest(String boatName) {
        registerToUBoatRequest(boatName);
        if(chosenContest != null){
            competitionTabPane.getSelectionModel().select(contestTab);
            startAlliesRivalsRefresher();
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
                        registeredToContest.set(true);
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
        rivalAlliesRefresher = new RivalAlliesRefresher(this::replaceAll, registeredToContest );
        rivalAlliesTimer = new Timer();
        rivalAlliesTimer.schedule(rivalAlliesRefresher, REFRESH_RATE, REFRESH_RATE);
    }

    public void updateDMProgress(DMProgress progress){
        missionsProgressController.setDTO(progress);
    }

    public void startMissionProgressRefresher() {
        missionProgressRefresher = new MissionsProgressRefresher(this::updateDMProgress, isCompetitionOn);
        missionProgressTimer = new Timer();
        missionProgressTimer.schedule(rivalAlliesRefresher, REFRESH_RATE, REFRESH_RATE);
    }

    private void startCandidatesRefresher(){
        candidatesRefresher = new CandidatesRefresher(
                (candidates) -> candidatesComponentController.addMultiple(candidates, true),
                isCompetitionOn);
        candidatesTimer = new Timer();
        candidatesTimer.schedule(candidatesRefresher, REFRESH_RATE, REFRESH_RATE);
    }

    public void replaceAll(List<Team> teams){
        activeTeamsController.replaceTeams(teams);
        contestDetailsController.update(chosenContest);
    }

    private void startAgentsRefresher(){
        activeAgentsRefresher = new ActiveAgentsRefresher(
                (myAgents) -> activeAgentsDisplayController.addMultipleAgents(myAgents), isAllyReady);
        agentsTimer = new Timer();
        agentsTimer.schedule(activeAgentsRefresher, REFRESH_RATE, REFRESH_RATE);
    }

    public void setIsAllyReady(boolean ready) {
        isAllyReady.set(ready);
    }

    public void handleCompetitionStatus(String winnerFound){
        //TODO
            //pop relavnt message to user with OK option
            //reset data of contest
    }

    private void startCompetitionOnRefresher(){
        competitionOnRefresher = new IsCompetitionOnRefresher(this::handleCompetitionStatus, isCompetitionOn);
        CompetitionOnTimer = new Timer();
        CompetitionOnTimer.schedule(activeAgentsRefresher, REFRESH_RATE, REFRESH_RATE);
    }
    public Contest getChosenContest() {
        return chosenContest;
    }
    public void updateChosenContestDisplay(Contest contest) {
        contestDetailsController.update(contest);
    }
}
