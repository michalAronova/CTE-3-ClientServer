package alliesClient.components.rivalAlliesRefresher;

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
    private final BooleanProperty isCompetitionOn;

    public RivalAlliesRefresher(Consumer<List<Team>> rivalListConsumer, Boolean isCompetitionOn) {
        this.rivalListConsumer = rivalListConsumer;
        this.isCompetitionOn = new SimpleBooleanProperty(isCompetitionOn);
    }

    @Override
    public void run() {
        if (isCompetitionOn.get()) {
            return;
        }
        HttpClientUtil.runAsync(RIVAL_ALLIES, new Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String jsonArrayOfContest = response.body().string();
                Type listType = new TypeToken<List<Team>>() { }.getType();
                List<Team> rivalTeams = GSON_INSTANCE.fromJson(jsonArrayOfContest, listType);
                Platform.runLater(() -> rivalListConsumer.accept(rivalTeams));
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println("failed");
            }
        });
    }
}
