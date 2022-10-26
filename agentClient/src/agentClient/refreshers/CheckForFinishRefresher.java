package agentClient.refreshers;


import DTO.contestForAgent.ContestForAgent;
import DTO.contestStatus.ContestStatus;
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

import static util.Constants.*;

public class CheckForFinishRefresher extends TimerTask {
    private final BooleanProperty inContest;
    private final BooleanProperty waitForAllyApproval;

    public CheckForFinishRefresher(BooleanProperty inContest, BooleanProperty waitForAllyApproval) {
        this.inContest = inContest;
        this.waitForAllyApproval = waitForAllyApproval;
    }

    @Override
    public void run() {
        if(!inContest.getValue()){
            return;
        }

        HttpClientUtil.runAsync(IS_CONTEST_FINISHED, new Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try(ResponseBody responseBody = response.body()) {
                    String bodyString = responseBody.string();
                    if(response.code() == 200){
                        ContestStatus contestStatus = GSON_INSTANCE
                                .fromJson(bodyString, ContestStatus.class);
                        if(!contestStatus.isCompetitionOn()){
                            inContest.set(false);
                            waitForAllyApproval.set(true);
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
