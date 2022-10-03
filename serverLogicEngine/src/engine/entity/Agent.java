package engine.entity;

import DTO.missionResult.MissionResult;
import engine.Engine;
import engine.decipherManager.missionTaker.MissionTaker;
import engine.decipherManager.resultListener.ResultListener;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;

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

    private BlockingQueue workQueue;

    private ThreadPoolExecutor threadExecutor;

    public Agent(String username, Allies myAllies, int threadCount, int missionAmountPull){
        this.username = username;
        this.myAllies = myAllies;
        this.threadCount = threadCount;
        this.missionAmountPull = missionAmountPull;

        //create result queue and register listener thread
        resultQueue = new LinkedBlockingQueue<>();

        //create work queue and thread pool of agents
        BasicThreadFactory factory = new BasicThreadFactory.Builder()
                .namingPattern("Agent %d")
                .build();
        workQueue = new ArrayBlockingQueue<>(missionAmountPull);
        threadExecutor = new ThreadPoolExecutor(threadCount, threadCount,
                Long.MAX_VALUE, TimeUnit.NANOSECONDS, workQueue , factory);
    }

    public void decipher(Consumer<MissionResult> transferMissionResult){
        threadExecutor.prestartAllCoreThreads();
        new Thread(new ResultListener(resultQueue, transferMissionResult),
                "Agent "+username+" Result Listener").start();
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
        new Thread(new MissionTaker(alliesWorkQueue, workQueue, resultQueue, missionAmountPull),
                "Agent "+username+" Mission Taker").start();
        decipher(transferMissionResult);


    }
}
