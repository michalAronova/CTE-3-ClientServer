package agentClient.refreshers;


import DTO.contestStatus.ContestStatus;
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
    private final BooleanProperty inWaitingList;

    public CheckForFinishRefresher(BooleanProperty inContest, BooleanProperty waitForAllyApproval, BooleanProperty inWaitingList) {
        this.inContest = inContest;
        this.waitForAllyApproval = waitForAllyApproval;
        this.inWaitingList = inWaitingList;
    }

    @Override
    public void run() {
        if(inWaitingList.get()){
            return;
        }

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
                        System.out.println("status: "+contestStatus);
                        if(!contestStatus.isCompetitionOn()){
                            System.out.println("competition on is false in status");
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
