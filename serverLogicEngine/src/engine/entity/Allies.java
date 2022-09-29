package engine.entity;

import engine.Engine;

import java.util.Map;

public class Allies implements Entity {
    private String userName;
    private Engine engine;
    private UBoat uBoat;
    private Map<String, Agent> name2Agent;
    Boolean isCompeting;

    public Allies(String userName){
        this.userName = userName;
    }

    public void setUBoat(UBoat uBoat){
        this.uBoat = uBoat;
    }

    public synchronized void addAgent(Agent agent){
        name2Agent.put(agent.getUsername(), agent);
    }

    public synchronized void removeAgent(String agentName){
        name2Agent.remove(agentName);
    }

    public synchronized void removeAllAgents(){
        name2Agent.clear();
    }

    public void setIsCompeting(boolean competing){
        isCompeting = competing;
    }

    public boolean getIsCompeting() { return isCompeting; }

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public Engine getEngine() {
        return engine;
    }
}