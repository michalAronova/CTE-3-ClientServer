package alliesClient.refreshers;

import DTO.team.Team;
import com.google.gson.reflect.TypeToken;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
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

public class IsCompetitionOnRefresher extends TimerTask {
    private final Consumer<String> updateCompetitionOn;
    private final BooleanProperty isCompetitionOn;

    public IsCompetitionOnRefresher(Consumer<String> updateCompetitionOn, BooleanProperty isCompetitionOn) {
        this.updateCompetitionOn = updateCompetitionOn;
        this.isCompetitionOn = isCompetitionOn;
    }

    @Override
    public void run() {
        //start after contest begins
        if (!isCompetitionOn.get()) {
            return;
        }
        HttpClientUtil.runAsync(IS_COMPETITION_ON, new Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String responseBodyString = response.body().string();
                if(!responseBodyString.equals("")){ //responseBodyString is the name of the winner
                    updateCompetitionOn.accept(responseBodyString);
                }
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println("failed");
            }
        });
    }
}
