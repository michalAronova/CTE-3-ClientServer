package agentClient.refreshers;

import DTO.contestStatus.ContestStatus;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.jetbrains.annotations.NotNull;
import util.http.HttpClientUtil;

import java.io.IOException;
import java.util.TimerTask;

import static util.Constants.*;

public class AllyApprovedRefresher extends TimerTask {
    private final BooleanProperty waitForAllyApproval;

    private final Runnable handleAllyOkClicked;

    public AllyApprovedRefresher(BooleanProperty waitForAllyApproval, Runnable handleAllyOkClicked) {
        this.waitForAllyApproval = waitForAllyApproval;
        this.handleAllyOkClicked = handleAllyOkClicked;
    }
    @Override
    public void run() {
        //run while waiting to ally approval!
        if(!waitForAllyApproval.getValue()){
            return;
        }
        HttpClientUtil.runAsync(ALLY_APPROVED, new Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try(ResponseBody responseBody = response.body()) {
                    String bodyString = responseBody.string();
                    if(response.code() == 200){
                        if(!bodyString.isEmpty()){ //not empty only if ally pressed ok!
                            handleAllyOkClicked.run();
                        }
                    }
                    else{
                        System.out.println(bodyString);
                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println("failed");
            }
        });
    }
}
