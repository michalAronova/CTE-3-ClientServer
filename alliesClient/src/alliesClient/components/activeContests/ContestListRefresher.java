package alliesClient.components.activeContests;

import DTO.contest.Contest;
import com.google.gson.reflect.TypeToken;
import javafx.beans.property.BooleanProperty;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import util.Constants;
import util.http.HttpClientUtil;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.TimerTask;
import java.util.function.Consumer;

import static util.Constants.ACTIVE_CONTESTS;
import static util.Constants.GSON_INSTANCE;

public class ContestListRefresher extends TimerTask {

    private final Consumer<List<Contest>> usersListConsumer;
    private final BooleanProperty isCompetitionOn;

    public ContestListRefresher(Consumer<List<Contest>> usersListConsumer, BooleanProperty isCompetitionOn) {
        this.usersListConsumer = usersListConsumer;
        this.isCompetitionOn = isCompetitionOn;
    }

    @Override
    public void run() {
        if (isCompetitionOn.get()) {
            return;
        }
        HttpClientUtil.runAsync(ACTIVE_CONTESTS, new Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String jsonArrayOfContest = response.body().string();
                Type listType = new TypeToken<List<Contest>>() { }.getType();
                List<Contest> contests = GSON_INSTANCE.fromJson(jsonArrayOfContest, listType);
                usersListConsumer.accept(contests);
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println("failed");
            }
        });
        }

    }

}
