package engine.entity;

import engine.Engine;

public class Agent implements Entity {

    private final String userName;
    private Engine engine;
    private final Allies myAllies;
    private final int threadCount;
    private final int missionAmountPull;

    public Agent(String userName, Allies myAllies, int threadCount, int missionAmountPull){
        this.userName = userName;
        this.myAllies = myAllies;
        this.threadCount = threadCount;
        this.missionAmountPull = missionAmountPull;
    }

    @Override
    public String getUsername() {
        return userName;
    }

    @Override
    public Engine getEngine() {
        return engine;
    }
}
