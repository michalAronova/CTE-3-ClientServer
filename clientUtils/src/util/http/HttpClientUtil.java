package util.http;

import DTO.missionResult.MissionResult;
import com.google.gson.Gson;
import okhttp3.*;

import java.io.File;
import java.util.function.Consumer;

import static util.Constants.GSON_INSTANCE;

public class HttpClientUtil {

    private final static SimpleCookieManager simpleCookieManager = new SimpleCookieManager();
    public final static OkHttpClient HTTP_CLIENT =
            new OkHttpClient.Builder()
                    .cookieJar(simpleCookieManager)
                    .followRedirects(true)
                    .build();

    public static void setCookieManagerLoggingFacility(Consumer<String> logConsumer) {
        simpleCookieManager.setLogData(logConsumer);
    }

    public static void removeCookiesOf(String domain) {
        simpleCookieManager.removeCookiesOf(domain);
    }

    public static void runAsync(String finalUrl, Callback callback) {
        Request request = new Request.Builder()
                .url(finalUrl)
                .build();

        Call call = HttpClientUtil.HTTP_CLIENT.newCall(request);

        call.enqueue(callback);
    }

    public static void runAsyncFileUpload(String finalUrl, File file, Callback callback) {
        RequestBody body =
                new MultipartBody.Builder()
                        .addFormDataPart("BattleFile", file.getName(), RequestBody.create(file, MediaType.parse("text/xml")))
                        .build();

        Request request = new Request.Builder()
                .url(finalUrl)
                .post(body)
                .build();

        Call call = HttpClientUtil.HTTP_CLIENT.newCall(request);

        call.enqueue(callback);
    }

    public static void runAsyncResultUpload(String finalUrl, MissionResult result, Callback callback) {
        String resultJson = GSON_INSTANCE.toJson(result);
        RequestBody body =
                new MultipartBody.Builder()
                        .addFormDataPart("result", resultJson)
                        .build();

        Request request = new Request.Builder()
                .url(finalUrl)
                .post(body)
                .build();

        Call call = HttpClientUtil.HTTP_CLIENT.newCall(request);

        call.enqueue(callback);
    }

    public static void runPostAsync(String finalUrl, Callback callback){

    }

    public static void shutdown() {
        System.out.println("Shutting down HTTP CLIENT");
        HTTP_CLIENT.dispatcher().executorService().shutdown();
        HTTP_CLIENT.connectionPool().evictAll();
    }

/*
 *  HOW TO: make sure response is always closed! (prevent leaks)
 * Call call = client.newCall(request);
 * call.enqueue(new Callback() {
 *   public void onResponse(Call call, Response response) throws IOException {
 *     try (ResponseBody responseBody = response.body()) {
 *     ... // Use the response.
 *     }
 *   }
 *
 *   public void onFailure(Call call, IOException e) {
 *   ... // Handle the failure.
 *   }
 * });
 */
}
