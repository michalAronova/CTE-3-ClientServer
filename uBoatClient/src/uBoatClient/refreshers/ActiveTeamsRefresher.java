package uBoatClient.refreshers;

import DTO.contest.Contest;
import DTO.team.Team;
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

import static util.Constants.*;

public class ActiveTeamsRefresher extends TimerTask {

    private final Consumer<List<Team>> teamsListConsumer;
    private final BooleanProperty isReady;
    private final BooleanProperty isCompetitionOn;
    private final BooleanProperty refreshTeams;

    public ActiveTeamsRefresher(Consumer<List<Team>> teamsListConsumer, BooleanProperty isReady,
                                BooleanProperty isCompetitionOn, BooleanProperty refreshTeams) {
        this.teamsListConsumer = teamsListConsumer;
        this.isReady = isReady;
        this.isCompetitionOn = isCompetitionOn;
        this.refreshTeams = refreshTeams;
    }

    @Override
    public void run() {
        if (isCompetitionOn.get()) {
            return;
        }

        if(!isReady.get()){
            return;
        }

        if(!refreshTeams.get()){
            return;
        }

        HttpClientUtil.runAsync(PARTICIPANTS, new Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try(ResponseBody responseBody = response.body()) {
                    String alliesJsonList = responseBody.string();
                    Type listType = new TypeToken<List<Team>>() { }.getType();
                    List<Team> teams = GSON_INSTANCE.fromJson(alliesJsonList, listType);
                    Platform.runLater(() -> teamsListConsumer.accept(teams));
                }
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println("failed");
            }
        });
    }
}
