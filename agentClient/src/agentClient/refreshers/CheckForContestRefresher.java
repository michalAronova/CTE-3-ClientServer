package agentClient.refreshers;

import DTO.contest.Contest;
import DTO.dmInfo.DMInfo;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.jetbrains.annotations.NotNull;
import util.http.HttpClientUtil;

import java.io.IOException;
import java.util.TimerTask;
import java.util.function.Consumer;

import static util.Constants.*;

public class CheckForContestRefresher extends TimerTask {

    private final BooleanProperty inContest;
    private final BooleanProperty inWaitingList;
    private final Consumer<Contest> updateContestDetails;
    private final Consumer<DMInfo> updateDMInfo;
    private final int version = 0;

    public CheckForContestRefresher(BooleanProperty inContest, BooleanProperty inWaitingList, Consumer<Contest> updateContestDetails, Consumer<DMInfo> updateDMInfo) {
        this.inContest = inContest;
        this.inWaitingList = inWaitingList;
        this.updateContestDetails = updateContestDetails;
        this.updateDMInfo = updateDMInfo;
    }

    @Override
    public void run() {
        if(inWaitingList.get()){
            return;
        }

        if(inContest.getValue()){
            return;
        }
        HttpClientUtil.runAsync(IS_UBOAT_REGISTERED, new Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try(ResponseBody responseBody = response.body()) {
                    String bodyString = responseBody.string();
                    if(response.code() == 200){
                        if(!bodyString.isEmpty()){
                            DMInfo dmInfo = GSON_INSTANCE
                                    .fromJson(bodyString, DMInfo.class);

                            inContest.set(dmInfo.isCompetitionOn());
                            Platform.runLater(() -> {
                                updateContestDetails.accept(dmInfo.getContestDetails());
                                updateDMInfo.accept(dmInfo);
                            });
                        }
                    }
                    else{
                        System.out.println(bodyString);
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
