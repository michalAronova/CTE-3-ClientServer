package engine.entity;

import DTO.missionResult.MissionResult;
import engine.Engine;
import engine.decipherManager.mission.Mission;
import engine.decipherManager.missionTaker.MissionTaker;
import engine.decipherManager.resultListener.ResultListener;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.*;
import java.util.function.Consumer;

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

    public Agent(String username, Allies myAllies, int threadCount, int missionAmountPull){
        this.username = username;
        this.myAllies = myAllies;
        this.threadCount = threadCount;
        this.missionAmountPull = missionAmountPull;

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
        isCompetitionOn.set(true);
        threadExecutor.prestartAllCoreThreads();
        new Thread(new ResultListener(resultQueue, transferMissionResult),
                "Agent "+username+" Result Listener").start();
        new Thread(this::pullMissionsFromAlly, "Pull Thread for "+username).start();
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
//                "Agent "+username+" Mission Taker").start();
        decipher(transferMissionResult);
    }

    public boolean isEmptyQueue() {
        return isEmptyQueue.get();
    }

    public BooleanProperty emptyQueueProperty() {
        return isEmptyQueue;
    }

    public void pullMissionsFromAlly(){
        List<Runnable> missions = new LinkedList<>();
        System.out.println("Using thread: "+ Thread.currentThread().getName());
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        while(isEmptyQueue() && !Thread.currentThread().isInterrupted()) {
            try {
                missions = myAllies.pullMissions(missionAmountPull);
                //above line will be an okHttp Request!!! ^^^^
                System.out.println(username+" pulled " + missions.size() + " missions!");
            } catch (InterruptedException e) {
                System.out.println(Thread.currentThread().getName() + " was interrupted");
            }
            if (missions.size() > 0) {
                missions.forEach(mission -> {
                    try {
                        ((Mission) mission).setWorkQueueAndEmptyProperty(workQueue, isEmptyQueue);
                        ((Mission) mission).setResultQueue(resultQueue);
                        workQueue.put(mission);
                    } catch (InterruptedException e) {
                        System.out.println(Thread.currentThread().getName() + " was interrupted");
                    }
                });
                isEmptyQueue.set(false);
            }
        }
    }

    public boolean isCompetitionOn() {
        return isCompetitionOn.get();
    }

    public BooleanProperty isCompetitionOnProperty() {
        return isCompetitionOn;
    }
}
