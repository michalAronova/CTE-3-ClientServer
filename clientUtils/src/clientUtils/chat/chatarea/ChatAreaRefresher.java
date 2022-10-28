package clientUtils.chat.chatarea;

import clientUtils.chat.chatarea.model.ChatLinesWithVersion;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import util.Constants;
import util.http.HttpClientUtil;

import java.io.IOException;
import java.util.TimerTask;
import java.util.function.Consumer;

import static parameters.ConstantParams.CHAT_VERSION_PARAMETER;
import static util.Constants.GSON_INSTANCE;

public class ChatAreaRefresher extends TimerTask {

    private final Consumer<ChatLinesWithVersion> chatlinesConsumer;
    private final IntegerProperty chatVersion;
    private final BooleanProperty shouldUpdate;

    public ChatAreaRefresher(IntegerProperty chatVersion, BooleanProperty shouldUpdate, Consumer<ChatLinesWithVersion> chatlinesConsumer) {
        this.chatlinesConsumer = chatlinesConsumer;
        this.chatVersion = chatVersion;
        this.shouldUpdate = shouldUpdate;
    }

    @Override
    public void run() {

        if (!shouldUpdate.get()) {
            return;
        }

        //noinspection ConstantConditions
        String finalUrl = HttpUrl
                .parse(Constants.CHAT_LINES_LIST)
                .newBuilder()
                .addQueryParameter(CHAT_VERSION_PARAMETER, String.valueOf(chatVersion.get()))
                .build()
                .toString();


        HttpClientUtil.runAsync(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println("failed");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String rawBody = response.body().string();
                    ChatLinesWithVersion chatLinesWithVersion = GSON_INSTANCE.fromJson(rawBody, ChatLinesWithVersion.class);
                    chatlinesConsumer.accept(chatLinesWithVersion);
                } else {
                    System.out.println(response.body().string());
                    System.out.printf("Error %d%n",response.code());
                }
            }
        });

    }

}
