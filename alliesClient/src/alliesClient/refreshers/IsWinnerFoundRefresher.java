package alliesClient.refreshers;

import clientUtils.popUpDialog;
import javafx.beans.property.BooleanProperty;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import util.http.HttpClientUtil;

import java.io.IOException;
import java.util.TimerTask;
import java.util.function.Consumer;

import static util.Constants.*;

public class IsWinnerFoundRefresher extends TimerTask {
    private final Consumer<String> updateWinnerFound;
    private final BooleanProperty isCompetitionOn;

    public IsWinnerFoundRefresher(Consumer<String> updateWinnerFound, BooleanProperty isCompetitionOn) {
        this.updateWinnerFound = updateWinnerFound;
        this.isCompetitionOn = isCompetitionOn;
    }

    @Override
    public void run() {
        //start after contest begins
        if (!isCompetitionOn.get()) {
            return;
        }
        HttpClientUtil.runAsync(IS_WINNER_FOUND, new Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String responseBodyString = response.body().string();
                if(responseBodyString.isEmpty()){
                    System.out.println("no winner was found yet");
                }
                else{
                    System.out.println("winner found: "+ responseBodyString);
                    isCompetitionOn.set(false);
                    updateWinnerFound.accept(responseBodyString);
                }
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println("failed");
            }
        });
    }
}
