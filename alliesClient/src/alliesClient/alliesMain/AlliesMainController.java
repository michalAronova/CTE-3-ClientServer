package alliesClient.alliesMain;

import DTO.contest.Contest;
import alliesClient.alliesApp.AlliesAppController;
import alliesClient.components.missionSizeChooser.MissionSizeChooserController;
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
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import util.Constants;
import util.http.HttpClientUtil;

import java.io.IOException;

import static parameters.ConstantParams.DESIRED_UBOAT;

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
    private AlliesAppController alliesAppController;

    private Contest chosenContest;
    private BooleanProperty ongoingContest;

    public AlliesMainController(){
        ongoingContest = new SimpleBooleanProperty(false);
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
        chosenContest = registerToUBoatRequest(boatName);

        //dispatch request to server...
        //on success, switch to the contest tab and fill the data there
        //should get contest data back in response ?
        //chosenContest = ?
    }

    public Contest registerToUBoatRequest(String boatName) {
        String finalUrl = HttpUrl
                .parse(Constants.PROCESS)
                .newBuilder()
                .addQueryParameter(DESIRED_UBOAT, boatName)
                .build()
                .toString();
        HttpClientUtil.runAsync(finalUrl, new Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try (ResponseBody responseBody = response.body()) {
                    if (response.code() == 200) {
                        String str = responseBody.string();
                        System.out.println("Output: " + str);
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


    public boolean isOngoingContest() {
        return ongoingContest.get();
    }

    public BooleanProperty ongoingContestProperty() {
        return ongoingContest;
    }

    public void missionSizeChosen(int missionSize) {
        //dispatch to server
        //both to update mission size
        //and notify that this ally is ready for battle!
        //start the requests for information to fill the various tables in the contest tab
    }
}
