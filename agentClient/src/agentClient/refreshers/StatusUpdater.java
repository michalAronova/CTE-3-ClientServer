package agentClient.refreshers;

import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import util.Constants;
import util.http.HttpClientUtil;

import java.io.IOException;
import java.util.TimerTask;

import static parameters.ConstantParams.*;
import static parameters.ConstantParams.THREAD_COUNT;
import static util.Constants.UPDATE_WORK_STATUS;

public class StatusUpdater extends TimerTask {
    private final IntegerProperty candidatesProduced;
    private final IntegerProperty missionsLeft;
    private final IntegerProperty missionsDone;

    public StatusUpdater(IntegerProperty candidatesProduced, IntegerProperty missionsLeft,
                         IntegerProperty missionsDone) {
        this.candidatesProduced = candidatesProduced;
        this.missionsLeft = missionsLeft;
        this.missionsDone = missionsDone;
    }

    @Override
    public void run() {
        String finalUrl = HttpUrl
                .parse(UPDATE_WORK_STATUS)
                .newBuilder()
                .addQueryParameter(CANDIDATES_PRODUCED, String.valueOf(candidatesProduced.getValue()))
                .addQueryParameter(MISSIONS_LEFT, String.valueOf(missionsLeft.getValue()))
                .addQueryParameter(MISSIONS_DONE_BY_AGENT, String.valueOf(missionsDone.getValue()))
                .build()
                .toString();

        HttpClientUtil.runAsync(finalUrl, new Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try (ResponseBody responseBody = response.body()) {
                    String body = responseBody.string();
                    if (response.code() == 200) {
                        System.out.println("updated successfully");
                    }
                    else {
                        System.out.println("Error occurred in server: "+ body);
                    }
                }
            }
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println("request failed");
            }
        });
    }
}
