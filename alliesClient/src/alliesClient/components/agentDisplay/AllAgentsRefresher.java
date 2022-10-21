package alliesClient.components.agentDisplay;

import DTO.agent.SimpleAgentDTO;
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
import java.util.Map;
import java.util.TimerTask;
import java.util.function.Consumer;

import static util.Constants.*;

public class AllAgentsRefresher extends TimerTask {
    private final Consumer<Map<String, SimpleAgentDTO>> resultListConsumer;

    public AllAgentsRefresher(Consumer<Map<String, SimpleAgentDTO>> resultListConsumer) {
        this.resultListConsumer = resultListConsumer;
    }

    @Override
    public void run() {
        //always refreshes

        HttpClientUtil.runAsync(ALL_AGENTS, new Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try(ResponseBody responseBody = response.body()) {
                    String myAgentsJson = responseBody.string();
                    Type listType = new TypeToken<Map<String, SimpleAgentDTO>>() { }.getType();
                    Map<String, SimpleAgentDTO> allAgents = GSON_INSTANCE.fromJson(myAgentsJson, listType);
                    Platform.runLater(() -> resultListConsumer.accept(allAgents));
                }
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println("failed to load all agents");
            }
        });
    }
}
