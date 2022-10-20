package alliesClient.refreshers;

import DTO.missionResult.MissionResult;
import com.google.gson.reflect.TypeToken;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.jetbrains.annotations.NotNull;
import util.http.HttpClientUtil;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.TimerTask;
import java.util.function.Consumer;

import static util.Constants.GSON_INSTANCE;
import static util.Constants.UBOAT_CANDIDATES;

public class CandidatesRefresher extends TimerTask {
    private final Consumer<List<MissionResult>> resultListConsumer;
    private final BooleanProperty isCompetitionOn;


    public CandidatesRefresher(Consumer<List<MissionResult>> resultListConsumer, BooleanProperty isCompetitionOn) {
        this.resultListConsumer = resultListConsumer;
        this.isCompetitionOn = isCompetitionOn;
    }

    @Override
    public void run() {
        if (!isCompetitionOn.get()) {
            return;
        }

        HttpClientUtil.runAsync(UBOAT_CANDIDATES, new Callback() {
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
