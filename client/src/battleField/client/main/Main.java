package battleField.client.main;

import DTO.codeObj.CodeObj;
import DTO.contest.Contest;
import DTO.mission.MissionDTO;
import DTO.team.Team;
import com.google.gson.reflect.TypeToken;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import util.Constants;
import util.http.HttpClientUtil;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static util.Constants.GSON_INSTANCE;
import static util.http.HttpClientUtil.HTTP_CLIENT;

public class Main {

    private static final Object DMLock = new Object();

    public static void main(String[] args) {
        Contest contest = new Contest("pearl", "name", true, "hard", 3, 2, "out");
        Contest contest2 = new Contest("pearl", "name", true, "hard", 3, 2, "out");
        List<Contest> allContests = new ArrayList<>();
        allContests.add(contest);
        allContests.add(contest2);
        String json = GSON_INSTANCE.toJson(allContests);
        System.out.println("JSON-----------------");
        System.out.println(json);
        Type listType = new TypeToken<List<Contest>>() { }.getType();
        List<Contest> contests = GSON_INSTANCE.fromJson(json, listType);
        System.out.println("JAVA-----------------");
        System.out.println(contests);

//        registerUBoat();
//        try {
//            Thread.sleep(2000);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
//        registerUBoat();
//        try {
//            Thread.sleep(2000);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
//        registerAllies();
//        try {
//            Thread.sleep(2000);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
//        registerAllies();
//        try {
//            Thread.sleep(2000);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
        // **load XML**
//        loadXMLRequest();
//        try {
//            Thread.sleep(2000);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
        // **config Code**
//        configRandomRequest();
//        try {
//            Thread.sleep(2000);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
        // **process MSG**
//        processMsgRequest();

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
                        String responseBodyString = responseBody.string();
                        System.out.println(responseBodyString);
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
    public static void updateWorkStatusRequest(){
        //get these from agent
        String candidatesProduced = "10";
        String missionsLeft = "10";
        String missionDone = "10";

        String finalUrl = HttpUrl
                .parse("/agent/work-status") //to update!!!!!!!!!!!!
                .newBuilder()
                .addQueryParameter("candidates-produced", candidatesProduced)
                .addQueryParameter("missions-left", missionsLeft)
                .addQueryParameter("mission-done-by-agent", missionDone)
                .build()
                .toString();

        HttpClientUtil.runAsync(finalUrl, new Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try (ResponseBody responseBody = response.body()) {
                    if (response.code() == 200) {
                        System.out.println("Agent work status loaded successfully");
                    }
                    else {
                        System.out.println("Error: "+ responseBody.string());
                    }
                }
            }
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println("failed");
            }
        });
    }
    public static void uBoatParticipantsListRequest(){
        String finalUrl = HttpUrl
                .parse("/uboat/participants") //to update with constants1!!!!!
                .newBuilder()
                .build()
                .toString();

        HttpClientUtil.runAsync(finalUrl, new Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try (ResponseBody responseBody = response.body()) {
                    if (response.code() == 200) {
                        Type listType = new TypeToken<List<Team>>() { }.getType();
                        List<Team> participants = GSON_INSTANCE.fromJson(responseBody.string(),listType);
                        System.out.println(participants);
                    }
                    else {
                        System.out.println("Error! "+ responseBody.string());
                    }
                }
            }
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println("request failed");
            }
        });
    }
    public static void registerAllies(){
        String username = "Ally";
        String finalUrl = HttpUrl
                .parse(Constants.ALLIES_LOGIN_PAGE)
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
                        String responseBodyString = responseBody.string();
                        System.out.println(responseBodyString);

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
}
