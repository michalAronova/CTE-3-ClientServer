package engine.entity;

import DTO.codeObj.CodeObj;
import DTO.mission.MissionDTO;
import DTO.missionResult.MissionResult;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import engine.Engine;
import engine.decipherManager.dictionary.Dictionary;
import engine.decipherManager.mission.Mission;
import engine.decipherManager.missionTaker.MissionTaker;
import engine.decipherManager.resultListener.ResultListener;
import enigmaMachine.keyBoard.KeyBoard;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import okhttp3.*;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.jetbrains.annotations.NotNull;
import util.Constants;
import util.http.HttpClientUtil;

import java.io.*;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.Consumer;

import static util.Constants.GSON_INSTANCE;
import static util.http.HttpClientUtil.HTTP_CLIENT;

public class Agent implements Entity {

    private final String username;
    private Engine engine;
    private final Allies myAllies;
    private final int threadCount;
    private final int missionAmountPull;

    private BlockingQueue<MissionResult> resultQueue;

    private final BlockingQueue<Runnable> workQueue;

    private ThreadPoolExecutor threadExecutor;

    private final BooleanProperty isEmptyQueue;

    private final BooleanProperty isCompetitionOn;

    private IntegerProperty missionsDone;

    private KeyBoard keyboard;
    private Dictionary dictionary;

    public Agent(String username, Allies myAllies, int threadCount, int missionAmountPull){
        this.username = username;
        this.myAllies = myAllies;
        this.threadCount = threadCount;
        this.missionAmountPull = missionAmountPull;
        missionsDone = new SimpleIntegerProperty(0);

        //create result queue and register listener thread
        resultQueue = new LinkedBlockingQueue<>();

        //create work queue and thread pool of agents
        BasicThreadFactory factory = new BasicThreadFactory.Builder()
                .namingPattern("Thread %d of "+username)
                .build();
        workQueue = new ArrayBlockingQueue<>(missionAmountPull);
        threadExecutor = new ThreadPoolExecutor(threadCount, threadCount,
                Long.MAX_VALUE, TimeUnit.NANOSECONDS, workQueue , factory);

        isEmptyQueue = new SimpleBooleanProperty(true);
        isCompetitionOn = new SimpleBooleanProperty(false);

        isEmptyQueue.addListener((observable, oldValue, newValue) -> {
            if(newValue && isCompetitionOn.getValue()){
                new Thread(this::pullMissionsFromAlly, "Pull Thread for "+username).start();
            }
        });
    }

    public void decipher(Consumer<MissionResult> transferMissionResult){
        missionsDone.set(0);
        isCompetitionOn.set(true);
        threadExecutor.prestartAllCoreThreads();
        new Thread(new ResultListener(resultQueue, transferMissionResult),
                "Agent "+username+" Result Listener").start();
        new Thread(this::pullMissionsFromAlly, "Pull Thread for "+username).start();
    }

    private List<Character> speedometer(List<Character> characters) {
        List<Character> updated = new ArrayList<>(characters);
        List<Character> keys = keyboard.getAsCharList();
        String keysString = keyboard.getKeyBoardString();

        for (int i = 0; i < characters.size(); i++) {
            if (updated.get(i).equals(keys.get(keys.size() - 1))) {
                updated.set(i, keys.get(0));
            } else {
                updated.set(i, keys.get(keysString
                        .indexOf(
                                updated.get(i)) + 1));
                break;
            }
        }
        return updated;
    }

    private static String serializableToString( Serializable o ) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(o);
        oos.close();
        return Base64.getEncoder().encodeToString(baos.toByteArray());
    }

    private static Object objectFromString(String s) throws IOException, ClassNotFoundException
    {
        byte [] data = Base64.getDecoder().decode(s);
        ObjectInputStream ois = new ObjectInputStream(
                new ByteArrayInputStream(data));
        Object o  = ois.readObject();
        ois.close();
        return o;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public Engine getEngine() {
        return engine;
    }

    public EntityEnum getEntity(){
        return EntityEnum.AGENT;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Agent)) return false;
        Agent agent = (Agent) o;
        return username.equals(agent.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }

    @Override
    public String toString() {
        return "Agent "+ username +" in team "+myAllies.getUsername();
    }

    public void bindWorkAndResultQueues(BlockingQueue<Runnable> alliesWorkQueue,
                                        Consumer<MissionResult> transferMissionResult) {
//        new Thread(new MissionTaker(alliesWorkQueue, workQueue, resultQueue, missionAmountPull),
//                "Agent "+username+" MissionDTO Taker").start();
        decipher(transferMissionResult);
    }

    public boolean isEmptyQueue() {
        return isEmptyQueue.get();
    }

    public BooleanProperty emptyQueueProperty() {
        return isEmptyQueue;
    }

    public void pullMissionsFromAlly(){
        List<MissionDTO> missionsDTOs = new LinkedList<>();
        System.out.println("Using thread: "+ Thread.currentThread().getName());
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        while(isEmptyQueue() && !Thread.currentThread().isInterrupted()) {
            missionsDTOs = requestMissionPull();

            System.out.println(username+" pulled " + missionsDTOs.size() + " missions!");

            if (missionsDTOs.size() > 0) {
                missionsDTOs.forEach(missionDTO -> {
                    try {
                        Mission mission = new Mission(missionDTO, dictionary, keyboard, this::speedometer,
                                missionsDone, resultQueue, workQueue, isEmptyQueue);
                        workQueue.put(mission);
                    } catch (InterruptedException e) {
                        System.out.println(Thread.currentThread().getName() + " was interrupted");
                    }
                });
                isEmptyQueue.set(false);
            }
        }
    }

    private List<MissionDTO> requestMissionPull(){
        List<MissionDTO> missionDTOS = new ArrayList<>();
        String finalUrl = HttpUrl
                .parse(Constants.PULL_MISSIONS)
                .newBuilder()
                //.addQueryParameter("mission-done-by-agent", String.valueOf(?missionDoneDelta?))
                .build()
                .toString();

        Request request = new Request.Builder()
                .url(finalUrl)
                .build();

        Call call = HTTP_CLIENT.newCall(request);

        try {
            Response response = HTTP_CLIENT.newCall(request).execute();
            try (ResponseBody responseBody = response.body()) {
                if (response.code() == 200) {
                    Type listType = new TypeToken<List<MissionDTO>>() { }.getType();
                    missionDTOS = GSON_INSTANCE.fromJson(responseBody.string(), listType);
                }
                else {
                    System.out.println("Error: "+ responseBody.string());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return missionDTOS;
    }

    private void jsonTester(List<Mission> missions){
        System.out.println("--------------- JSON TESTER ---------------");
        Gson gson = new Gson();
        //Type missionListType = new TypeToken<List<MissionDTO>>() { }.getType();
        String missionsToJson = gson.toJson(missions);
        System.out.println("Json string below:");
        System.out.println(missionsToJson + System.lineSeparator());

        //List<MissionDTO> missionFromJson = gson.fromJson(missionsToJson, missionListType);
        //System.out.println(missionFromJson);
        System.out.println("--------------------------------------------");
    }

    public boolean isCompetitionOn() {
        return isCompetitionOn.get();
    }

    public BooleanProperty isCompetitionOnProperty() {
        return isCompetitionOn;
    }
}
