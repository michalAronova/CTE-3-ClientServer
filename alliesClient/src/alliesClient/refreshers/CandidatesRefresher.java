package alliesClient.refreshers;

import DTO.missionResult.MissionResult;
import com.google.gson.reflect.TypeToken;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import util.http.HttpClientUtil;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.TimerTask;
import java.util.function.Consumer;

import static parameters.ConstantParams.*;
import static util.Constants.*;

public class CandidatesRefresher extends TimerTask {
    private final Consumer<List<MissionResult>> resultListConsumer;
    private final BooleanProperty isCompetitionOn;

    private final IntegerProperty candidatesAmount;


    public CandidatesRefresher(Consumer<List<MissionResult>> resultListConsumer,
                               BooleanProperty isCompetitionOn, IntegerProperty candidatesAmount) {
        this.resultListConsumer = resultListConsumer;
        this.isCompetitionOn = isCompetitionOn;
        this.candidatesAmount = candidatesAmount;
    }

    @Override
    public void run() {
        //start when the competition starts
        if (!isCompetitionOn.get()) {
            return;
        }

        String finalUrl = HttpUrl
                .parse(ALLIES_CANDIDATES)
                .newBuilder()
                .addQueryParameter(CANDIDATES_VERSION, String.valueOf(candidatesAmount.getValue()))
                .build()
                .toString();


        HttpClientUtil.runAsync(finalUrl, new Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try(ResponseBody responseBody = response.body()) {
                    String candidatesJsonList = responseBody.string();
                    Type listType = new TypeToken<List<MissionResult>>() { }.getType();
                    List<MissionResult> results = GSON_INSTANCE.fromJson(candidatesJsonList, listType);
                    Platform.runLater(() -> resultListConsumer.accept(results));
                }
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println("failed");
            }
        });
    }
}
