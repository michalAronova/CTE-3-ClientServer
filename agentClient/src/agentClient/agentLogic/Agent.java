package agentClient.agentLogic;

import DTO.mission.MissionDTO;
import DTO.missionResult.MissionResult;
import com.google.gson.reflect.TypeToken;
import engine.decipherManager.mission.Mission;
import engine.decipherManager.resultListener.ResultListener;
import enigmaMachine.Machine;
import enigmaMachine.keyBoard.KeyBoard;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.concurrent.Task;
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

public class Agent {

    private final String username;
    private final String myAllies;
    private final int threadCount;
    private final int missionAmountPull;

    private BlockingQueue<MissionResult> resultQueue;

    private final BlockingQueue<Runnable> workQueue;

    private ThreadPoolExecutor threadExecutor;

    private final BooleanProperty isEmptyQueue;

    private final BooleanProperty isCompetitionOn;
    private Consumer<MissionResult> updateUICandidates;

    private KeyBoard keyboard;
    private Set<String> dictionary;
    private Task<Boolean> decipherTask;

    private CountDownLatch cdl;

    //data for UI...
    private final IntegerProperty missionsInQueue;
    private final IntegerProperty missionsDone;
    private final IntegerProperty totalMissionsPulled;

    public Agent(String username, String myAllies, int threadCount, int missionAmountPull) {
        this.username = username;
        this.myAllies = myAllies;
        this.threadCount = threadCount;
        this.missionAmountPull = missionAmountPull;
        missionsDone = new SimpleIntegerProperty(0);
        missionsInQueue = new SimpleIntegerProperty(0);
        totalMissionsPulled = new SimpleIntegerProperty(0);

        //create result queue
        resultQueue = new LinkedBlockingQueue<>();

        //create work queue and thread pool of agents
        BasicThreadFactory factory = new BasicThreadFactory.Builder()
                .namingPattern("Thread %d of " + username)
                .build();
        workQueue = new ArrayBlockingQueue<>(missionAmountPull);
        threadExecutor = new ThreadPoolExecutor(threadCount, threadCount,
                Long.MAX_VALUE, TimeUnit.NANOSECONDS, workQueue, factory);

        isEmptyQueue = new SimpleBooleanProperty(true);
        isCompetitionOn = new SimpleBooleanProperty(false);

        isCompetitionOn.addListener((observable, oldValue, newValue) -> {
            if(newValue){ //is competition is on, start
                System.out.println("competition on notified inside Agent Object, start!");
                start();
            }
            else{ //if competition turned not on, stop
                System.out.println("competition off notified inside Agent Object, stop!");
                stop();
            }
        });

//        isEmptyQueue.addListener((observable, oldValue, newValue) -> {
//            if (newValue && isCompetitionOn.getValue()) {
//                new Thread(this::pullMissionsFromAlly, "Pull Thread for " + username).start();
//            }
//        });

//        missionsInQueue.addListener((observable, oldValue, newValue) -> {
//            System.out.println("-------------------------mission in queue changed: "+ newValue);
//        });
//
//        missionsDone.addListener((observable, oldValue, newValue) -> {
//            System.out.println("*************************missions done changed: "+ newValue);
//        });
    }

    public void updateByContest(List<Character> keys, Set<String> words){
        if(keys == null || words == null) return;

        this.keyboard = new KeyBoard(charListToString(keys));
        this.dictionary = words;
    }

    public String charListToString(List<Character> keys){
        StringBuilder sb = new StringBuilder();
        keys.forEach(sb::append);
        return sb.toString();
    }

    public void start(){
        decipherTask = new Task<Boolean>() {
            @Override
            protected Boolean call() throws Exception {
                decipher();
                return true;
            }
        };

        new Thread(decipherTask, "decipher task thread").start();
    }

    public void stop(){
        if(decipherTask != null){
            decipherTask.cancel();
        }
        workQueue.clear();
        resultQueue.clear();
    }

    private void decipher() {
        System.out.println(Thread.currentThread().getName()+" is working");
        //isCompetitionOn.set(true);
        threadExecutor.prestartAllCoreThreads();

        new Thread(new ResultListener(resultQueue, this::transferMissionResult).addBooleanToProceed(isCompetitionOn),
                "Agent " + username + " Result Listener").start();
        new Thread(this::pullMissionsFromAlly, "First Pull Thread for " + username).start();
    }

    private void transferMissionResult(MissionResult missionResult) {
        updateUICandidates.accept(missionResult);

        String finalUrl = HttpUrl
                .parse(Constants.UPLOAD_CANDIDATES)
                .newBuilder()
                .build()
                .toString();

        //totalCandidatesFound += missionResult.getCandidates().size();
        //this updates in UI by sending the whole mission result (see first line in this method)

        HttpClientUtil.runAsyncResultUpload(finalUrl, missionResult, new Callback() {
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
        return "Agent "+ username +" in team "+myAllies;
    }

    public boolean isEmptyQueue() {
        return isEmptyQueue.get();
    }

    public BooleanProperty emptyQueueProperty() {
        return isEmptyQueue;
    }

    public void pullMissionsFromAlly(){
        List<MissionDTO> missionsDTOs;

        missionsDTOs = requestMissionPull();
        final int missionAmount = missionsDTOs.size();
        cdl = new CountDownLatch(missionAmount);
        Platform.runLater(() -> {
            missionsInQueue.set((int)cdl.getCount());
            totalMissionsPulled.set(totalMissionsPulled.get() + missionAmount);
        });

        if (missionsDTOs.size() > 0) {
            missionsDTOs.forEach(missionDTO -> {
                try {
                    Mission mission = new Mission(deepCopyMachine(missionDTO.getMachineEncoded()), missionDTO.getStartPositions(),
                            missionDTO.getMissionSize(), missionDTO.getToDecrypt(), dictionary, this::speedometer,
                            missionsDone, resultQueue, myAllies, username, missionsInQueue, cdl);
                    workQueue.put(mission);
                } catch (InterruptedException e) {
                    System.out.println(Thread.currentThread().getName() + " was interrupted");
                }
            });
        }

        try {
            cdl.await();
            if(isCompetitionOn.getValue() && !Thread.currentThread().isInterrupted()) {
                pullMissionsFromAlly();
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private Machine deepCopyMachine(String machineEncoded) {
        try{
            return (Machine)objectFromString(machineEncoded);
        }
        catch(ClassNotFoundException | IOException e){
            e.printStackTrace();
            return null;
        }
    }

    private List<MissionDTO> requestMissionPull(){
        List<MissionDTO> missionDTOS = new ArrayList<>();
        String finalUrl = HttpUrl
                .parse(Constants.PULL_MISSIONS)
                .newBuilder()
                .build()
                .toString();

        Request request = new Request.Builder()
                .url(finalUrl)
                .build();

        try {
            Response response = HTTP_CLIENT.newCall(request).execute();
            try (ResponseBody responseBody = response.body()) {
                if (response.code() == 200) { //HttpServletResponse.SC_OK
                    Type listType = new TypeToken<List<MissionDTO>>() { }.getType();
                    missionDTOS = GSON_INSTANCE.fromJson(responseBody.string(), listType);
                }
                else if(response.code() == 204){ //HttpServletResponse.SC_NO_CONTENT - contest has ended
                    //System.out.println("can't pull because competition has ended...");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return missionDTOS;
    }

    public boolean isCompetitionOn() {
        return isCompetitionOn.get();
    }

    public BooleanProperty isCompetitionOnProperty() {
        return isCompetitionOn;
    }

    public void setUpdateUICandidates(Consumer<MissionResult> updateUICandidates) {
        this.updateUICandidates = updateUICandidates;
    }

    public int getMissionsInQueue() {
        return missionsInQueue.get();
    }

    public IntegerProperty missionsInQueueProperty() {
        return missionsInQueue;
    }

    public int getMissionsDone() {
        return missionsDone.get();
    }

    public IntegerProperty missionsDoneProperty() {
        return missionsDone;
    }

    public int getTotalMissionsPulled() {
        return totalMissionsPulled.get();
    }

    public IntegerProperty totalMissionsPulledProperty() {
        return totalMissionsPulled;
    }

    public void clearData() {
        missionsDone.set(0);
        missionsInQueue.set(0);
        totalMissionsPulled.set(0);
    }
}
