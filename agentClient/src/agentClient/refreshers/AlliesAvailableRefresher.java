package agentClient.refreshers;


import com.google.gson.reflect.TypeToken;
import javafx.application.Platform;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.jetbrains.annotations.NotNull;
import util.http.HttpClientUtil;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Set;
import java.util.TimerTask;
import java.util.function.Consumer;

import static util.Constants.*;

public class AlliesAvailableRefresher extends TimerTask {

    private final Consumer<Set<String>> consumeAllies;

    public AlliesAvailableRefresher(Consumer<Set<String>> consumeAllies) {
        this.consumeAllies = consumeAllies;
    }

    @Override
    public void run() {
        HttpClientUtil.runAsync(ALLIES_LIST, new Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try(ResponseBody responseBody = response.body()) {
                    String alliesJsonList = responseBody.string();
                    Type setType = new TypeToken<Set<String>>() { }.getType();
                    Set<String> allies = GSON_INSTANCE.fromJson(alliesJsonList, setType);
                    Platform.runLater(() -> consumeAllies.accept(allies));
                }
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println("failed");
            }
        });
    }
}
