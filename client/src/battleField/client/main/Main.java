package battleField.client.main;

import DTO.codeObj.CodeObj;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import util.Constants;
import util.http.HttpClientUtil;

import java.io.File;
import java.io.IOException;
import static util.Constants.GSON_INSTANCE;
import static util.http.HttpClientUtil.HTTP_CLIENT;

public class Main {

    private static final Object DMLock = new Object();

    public static void main(String[] args) {
        registerUBoat();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        loadXMLRequest();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        configRandomRequest();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        processMsgRequest();

//        Engine engine = new TheEngine();
//        engine.loadDataFromXML("C:\\Users\\micha\\IdeaProjects\\CTE-3-ClientServer\\engine\\src\\resources\\ex3-basic.xml");
//        engine.setByAutoGeneratedCode();
//        CodeObj code1 = engine.getInitialCode();
//        engine.setByAutoGeneratedCode();
//        CodeObj code2 = engine.getInitialCode();
//
//        List<Pair<String,CodeObj>> candidates = new ArrayList<>();
//        candidates.add(new Pair<>("agent1", code1));
//        candidates.add(new Pair<>("agent2", code2));
//        MissionResult result = new MissionResult(candidates, "Bob", 0);
//
////        UBoat uboat = new UBoat("Boat");
////
////        Allies ally1 = new Allies("bob");
////        Allies ally2 = new Allies("alice");
//
//        Gson gson = new Gson();
//
////        uboat.addParticipant(ally1);
////        uboat.addParticipant(ally2);
//
//        String json = gson.toJson(result);
//        System.out.println("JSON: ");
//        System.out.println(json);
//        System.out.println("-----------");
//        System.out.println("AND THEN:");
//
//        Type type = new TypeToken<MissionResult>() { }.getType();
//        MissionResult participants = gson.fromJson(json, type);
//        System.out.println(participants);
    }

    public static void registerUBoat(){
        String username = "Michal";
        String finalUrl = HttpUrl
                .parse(Constants.UBOAT_LOGIN_PAGE)
                .newBuilder()
                .addQueryParameter("username", username)
                .build()
                .toString();

        HttpClientUtil.runAsync(finalUrl, new Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try (ResponseBody responseBody = response.body()) {
                    if (response.code() == 200) {
                        System.out.println("registered as " + username);
                    }
                    else {
                        String responseBodyString = responseBody.string();
                        System.out.println(responseBodyString);
                    }
                }
            }
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println("failed");
            }
        });
    }

    public static void loadXMLRequest(){
        File selectedFile = new File("C:\\Users\\micha\\IdeaProjects\\CTE-3-ClientServer\\engine\\src\\resources\\ex3-basic.xml");
        String finalUrl = HttpUrl
                .parse(Constants.UPLOAD_XML)
                .newBuilder()
                .build()
                .toString();

        HttpClientUtil.runAsyncFileUpload(finalUrl, selectedFile, new Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try (ResponseBody responseBody = response.body()) {
                    if (response.code() == 200) {
                        String str = responseBody.string();
                        System.out.println("loaded!");
                    } else {
                        System.out.println("not loaded :(");
                        System.out.println(responseBody.string());
                    }
                }
            }
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println(e.getMessage());
            }

        });
    }

    public static void configRandomRequest() {
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = RequestBody.create("", mediaType);
        Request request = new Request.Builder()
                .url(Constants.RANDOM_CONFIG)
                .method("POST", body)
                .build();
        HTTP_CLIENT.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try (ResponseBody responseBody = response.body()) {
                    CodeObj code = GSON_INSTANCE.fromJson(responseBody.string(), CodeObj.class);
                    System.out.println(code);
                }
            }
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println("failed :(");
            }
        });
    }

    public static void processMsgRequest(){
        String msg = "Hello";
        String finalUrl = HttpUrl
                .parse(Constants.PROCESS)
                .newBuilder()
                .addQueryParameter("message-to-process", msg)
                .build()
                .toString();

        HttpClientUtil.runAsync(finalUrl, new Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try (ResponseBody responseBody = response.body()) {
                    if (response.code() == 200) {
                        String str = responseBody.string();
                        System.out.println("Output: "+ str);
                    }
                    else {
                        System.out.println("Error! "+ responseBody.string());
                    }
                }
            }
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println("failed");
            }
        });
    }


//    public static void puller(DecipherManager DM){
//        List<Runnable> missions = new LinkedList<>();
//        System.out.println("Using thread: "+ Thread.currentThread().getName());
////        try {
////            System.out.println(Thread.currentThread().getName()+" going to sleep");
////            Thread.sleep(3000);
////        } catch (InterruptedException e) {
////            System.out.println(Thread.currentThread().getName()+"was interrupted during sleep");
////        }
//
//        while (!Thread.currentThread().isInterrupted()){
//            int missionPullAmount = 5;
//            synchronized (DMLock){
//                while(!DM.getWorkQueue().isEmpty() && missionPullAmount > 0){
//                    --missionPullAmount;
//                    try {
//                        missions.add(DM.getWorkQueue().take());
//                    } catch (InterruptedException e) {
//                        System.out.println("interrupted...");
//                    }
//                }
//            }
//
//            System.out.println("pulled "+ missions.size()+" missions!");
//            missions.clear();
//        }
//    }
}
