package engine.entity;

import engine.Engine;

import java.util.Objects;

public class Agent implements Entity {

    private final String username;
    private Engine engine;
    private final Allies myAllies;
    private final int threadCount;
    private final int missionAmountPull;

    public Agent(String username, Allies myAllies, int threadCount, int missionAmountPull){
        this.username = username;
        this.myAllies = myAllies;
        this.threadCount = threadCount;
        this.missionAmountPull = missionAmountPull;
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
}
