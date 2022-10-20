package alliesClient.refreshers;

import DTO.team.Team;
import com.google.gson.reflect.TypeToken;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import util.http.HttpClientUtil;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.TimerTask;
import java.util.function.Consumer;

import static util.Constants.*;

public class RivalAlliesRefresher extends TimerTask {

    private final Consumer<List<Team>> rivalListConsumer;
    private final BooleanProperty registeredToContest;

    public RivalAlliesRefresher(Consumer<List<Team>> rivalListConsumer, BooleanProperty registeredToContest) {
        this.rivalListConsumer = rivalListConsumer;
        this.registeredToContest = registeredToContest;
    }

    @Override
    public void run() {
        //start after registration to a contest
        if (!registeredToContest.get()) {
            return;
        }
        HttpClientUtil.runAsync(RIVAL_ALLIES, new Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String jsonAlliesList = response.body().string();
                Type listType = new TypeToken<List<Team>>() { }.getType();
                List<Team> rivalTeams = GSON_INSTANCE.fromJson(jsonAlliesList, listType);
                Platform.runLater(() -> rivalListConsumer.accept(rivalTeams));
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println("failed");
            }
        });
    }
}
