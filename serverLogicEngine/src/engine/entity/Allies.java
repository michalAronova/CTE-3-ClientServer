package engine.entity;

import engine.Engine;

import java.util.Map;
import java.util.Objects;

public class Allies implements Entity {
    private String username;
    private Engine engine;
    private UBoat uBoat;
    private Map<String, Agent> name2Agent;
    Boolean isCompeting;

    public Allies(String username){
        this.username = username;
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
        return username;
    }

    @Override
    public Engine getEngine() {
        return engine;
    }

    public EntityEnum getEntity(){
        return EntityEnum.ALLIES;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Allies)) return false;
        Allies allies = (Allies) o;
        return username.equals(allies.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }

    @Override
    public String toString() {
        return "Allies: "+ username;
    }

    public Map<String, Agent> getMyAgents() {
        return name2Agent;
    }
}