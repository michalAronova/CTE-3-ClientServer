package clientUtils.chat.users;

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
import java.util.*;
import java.util.function.Consumer;

import static util.Constants.GSON_INSTANCE;

public class UserListRefresher extends TimerTask {

    private final Consumer<List<String>> usersListConsumer;
    private final BooleanProperty shouldUpdate;


    public UserListRefresher(BooleanProperty shouldUpdate, Consumer<List<String>> usersListConsumer) {
        this.shouldUpdate = shouldUpdate;
        this.usersListConsumer = usersListConsumer;
    }

    @Override
    public void run() {

        if (!shouldUpdate.get()) {
            return;
        }

        HttpClientUtil.runAsync(Constants.USERS_LIST, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println("failed");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String jsonArrayOfUsersNames = response.body().string();
                Type setType = new TypeToken<Set<String>>() { }.getType();
                Set<String> usersNames = GSON_INSTANCE.fromJson(jsonArrayOfUsersNames, setType);
                usersListConsumer.accept(new LinkedList<>(usersNames));
            }
        });
    }
}
