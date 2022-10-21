package alliesClient.alliesMain;

import DTO.contest.Contest;
import DTO.dmProgress.DMProgress;
import DTO.team.Team;
import alliesClient.alliesApp.AlliesAppController;
import alliesClient.components.activeAgentsDisplay.ActiveAgentsRefresher;
import alliesClient.components.missionSizeChooser.MissionSizeChooserController;
import alliesClient.components.missionsProgress.MissionsProgressRefresher;
import alliesClient.refreshers.CandidatesRefresher;
import alliesClient.refreshers.IsWinnerFoundRefresher;
import alliesClient.refreshers.RivalAlliesRefresher;
import clientUtils.MainAppController;
import clientUtils.activeTeams.ActiveTeamsComponentController;
import clientUtils.candidatesComponent.CandidatesComponentController;
import clientUtils.contestDetails.ContestDetailsController;
import alliesClient.components.activeAgentsDisplay.ActiveAgentsDisplayController;
import alliesClient.components.activeContests.ActiveContestsController;
import alliesClient.components.agentDisplay.AgentDisplayController;
import alliesClient.components.missionsProgress.MissionsProgressController;
import com.google.gson.JsonSyntaxException;
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
import static parameters.ConstantParams.MISSION_SIZE;
import static util.Constants.*;

public class AlliesMainController implements MainAppController {
    @FXML private GridPane contestOnGrid;
    @FXML private Label usernameLabel;

    //dashboard tab
    @FXML private Tab dashboardTab;
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
    private AlliesAppController alliesAppController;
    @FXML private Tab contestTab;

    private Contest chosenContest;
    private BooleanProperty registeredToContest;
    private BooleanProperty isCompetitionOn;
    private BooleanProperty isAllyReady;
    private RivalAlliesRefresher rivalAlliesRefresher;
    private CandidatesRefresher candidatesRefresher;
    private ActiveAgentsRefresher activeAgentsRefresher;
    private MissionsProgressRefresher missionProgressRefresher;
    private IsWinnerFoundRefresher competitionOnRefresher;
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
        contestTab.setDisable(true);
    }

    public void chooseContest(String boatName) {
        registerToUBoatRequest(boatName);

        /*if(chosenContest != null){
            contestTab.setDisable(false);
            competitionTabPane.getSelectionModel().select(contestTab);
            dashboardTab.setDisable(true);
            startRivalAlliesRefresher();
        }
        else{
            System.out.println("not registered yet");
        }*/
    }

    public void registerToUBoatRequest(String boatName) {
        System.out.println("boat name: " + boatName);
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
                    String stringFromBody = responseBody.string();
                    if (response.code() == 200) {
                        try {
                            chosenContest = GSON_INSTANCE.fromJson(stringFromBody, Contest.class);
                        }
                        catch(IllegalStateException | JsonSyntaxException exception) {
                            System.out.println("*********************");
                            System.out.println("response body:");
                            System.out.println(stringFromBody);
                            System.out.println("*********************");
                        }
                        registeredToContest.set(true);
                        System.out.println(chosenContest);
                        contestTab.setDisable(false);
                        competitionTabPane.getSelectionModel().select(contestTab);
                        dashboardTab.setDisable(true);
                        startRivalAlliesRefresher();
                    } else {
                        System.out.println("Error! " + stringFromBody);
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
        String finalUrl = HttpUrl
                .parse(ALLIES_READY)
                .newBuilder()
                .addQueryParameter(MISSION_SIZE, String.valueOf(missionSize))
                .build()
                .toString();

        HttpClientUtil.runAsync(finalUrl, new Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try (ResponseBody responseBody = response.body()) {
                    if (response.code() == 200) {
                        System.out.println("Ally updated as READY successfully");
                    }
                    else {
                        System.out.println("Error: "+ responseBody.string());
                    }
                }
            }
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println("failed");
            }
        });
        //dispatch to server
        //both to update mission size
        //and notify that this ally is ready for battle!
        //start the requests for information to fill the various tables in the contest tab
    }
    public void startRivalAlliesRefresher() {
        rivalAlliesRefresher = new RivalAlliesRefresher(this::replaceAllRivals, registeredToContest );
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

    public void replaceAllRivals(List<Team> teams){
        activeTeamsController.replaceTeams(teams);
        contestDetailsController.update(chosenContest);
        checkIsCompetitionOn();
    }

    private void checkIsCompetitionOn() {
        if(activeTeamsController.getActiveTeamsAmount() == chosenContest.getTotalRequiredTeams()){
            isCompetitionOn.set(true);
        }
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

    public void handleWinnerFound(String winnerFound){
        //TODO
            //pop relavnt message to user with OK option
            //reset data of contest
        //chosenContest = null;
        //contestTab.setDisable(true);
    }


    private void startWinnerFoundRefresher(){
        competitionOnRefresher = new IsWinnerFoundRefresher(this::handleWinnerFound, isCompetitionOn);
        CompetitionOnTimer = new Timer();
        CompetitionOnTimer.schedule(activeAgentsRefresher, REFRESH_RATE, REFRESH_RATE);
    }
    public Contest getChosenContest() {
        return chosenContest;
    }
    public void updateChosenContestDisplay(Contest contest) {
        chosenContest.setByContest(contest);
        contestDetailsController.update(contest);
    }
}
