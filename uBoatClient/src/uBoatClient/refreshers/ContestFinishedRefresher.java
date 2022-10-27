package uBoatClient.refreshers;

import DTO.contestStatus.ContestStatus;
import clientUtils.popUpDialog;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.StringProperty;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.jetbrains.annotations.NotNull;
import util.http.HttpClientUtil;

import java.io.IOException;
import java.util.TimerTask;

import static util.Constants.*;

public class ContestFinishedRefresher extends TimerTask {
    private final BooleanProperty isCompetitionOn;
    private final StringProperty winnerName;
    private final Runnable okClicked;

    public ContestFinishedRefresher(BooleanProperty isCompetitionOn, StringProperty winnerName, Runnable okClicked) {
        this.isCompetitionOn = isCompetitionOn;
        this.winnerName = winnerName;
        this.okClicked = okClicked;
    }

    @Override
    public void run() {
        if(!isCompetitionOn.getValue()){
            return;
            //the change of this property to true is handled by checking the number of active teams
            //each request to get the list of active teams registered to this uboat
        }

        //hence, this refresher only looks for the END of a competition - during which
        //the property is TRUE (during a competition)

        HttpClientUtil.runAsync(CONTEST_FINISHED, new Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try(ResponseBody responseBody = response.body()) {
                    String jsonStatus = responseBody.string();
                    ContestStatus contestStatus = GSON_INSTANCE.fromJson(jsonStatus, ContestStatus.class);
                    if(!contestStatus.isCompetitionOn()){
                        winnerName.set(contestStatus.getWinnerName());
                        isCompetitionOn.set(false);
                        Platform.runLater(() -> new popUpDialog("UBoat",FINAL_MESSAGE + winnerName.getValue(), okClicked));
                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println("failed");
            }
        });
    }
}
