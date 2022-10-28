package alliesClient.components.missionsProgress;

import DTO.dmProgress.DMProgress;
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

public class MissionsProgressRefresher extends TimerTask {

    private final Consumer<DMProgress> DMProgressConsumer;
    private final BooleanProperty isCompetitionOn;

    public MissionsProgressRefresher(Consumer<DMProgress> DMProgressConsumer, BooleanProperty isCompetitionOn) {
        this.DMProgressConsumer = DMProgressConsumer;
        this.isCompetitionOn = isCompetitionOn;
    }
    @Override
    public void run() {
        //start after competition starts
        if (!isCompetitionOn.get()) {
            return;
        }
        HttpClientUtil.runAsync(DM_PROGRESS, new Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String jsonDMProgress = response.body().string();
                DMProgress status = GSON_INSTANCE.fromJson(jsonDMProgress, DMProgress.class);
                Platform.runLater(() -> DMProgressConsumer.accept(status));
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println("failed");
            }
        });
    }
}
