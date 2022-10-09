package engine.entity;

import DTO.codeObj.CodeObj;
import DTO.missionResult.MissionResult;
import engine.Engine;
import engine.decipherManager.DecipherManager;
import engine.decipherManager.Difficulty;
import engine.decipherManager.dictionary.Dictionary;
import engine.decipherManager.resultListener.ResultListener;
import engine.stock.Stock;
import enigmaMachine.Machine;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;

public class Allies implements Entity {
    private String username;
    private DecipherManager DM;
    private UBoat uBoat;
    private Map<String, Agent> name2Agent;

    // wait-list for agents joining during a contest here
    private Boolean isCompeting;

    private Boolean isWinner;
    private Boolean isCompetitionOn;

    private BlockingQueue<MissionResult> resultQueue;

    public Allies(String username){ this.username = username; }


    public synchronized void setUBoat(UBoat uBoat){
        this.uBoat = uBoat;
        uBoat.addParticipant(this);
    }

    public void start(String encryption, Consumer<MissionResult> transferMissionResultToUBoat){
        new Thread(new ResultListener(resultQueue, transferMissionResultToUBoat),
                "Allies "+username+" Result Listener").start();
        addWorkQueueToAgents(DM.getWorkQueue());
        DM.manageAgents(encryption); //start creating missions
    }

    public synchronized void addAgent(Agent agent){
        name2Agent.put(agent.getUsername(), agent);
    }

    public void createDM(Dictionary dictionary, Machine machine,
                         Stock stock, Difficulty difficulty, CodeObj code, String input){
        DM = new DecipherManager(dictionary, machine, stock, difficulty, code, input);
        resultQueue = new LinkedBlockingQueue<>();
    }

    private void addWorkQueueToAgents(BlockingQueue<Runnable> alliesWorkQueue) {
        for(Agent agent: name2Agent.values()){
            agent.bindWorkAndResultQueues(alliesWorkQueue, (result) -> {
                try {
                    resultQueue.put(result);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
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
        return null;
    }
    public DecipherManager getDM(){ return DM;}
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
    public Boolean getIsWinner() {
        return isWinner;
    }
    public void setIsWinner(Boolean winner) {
        isWinner = winner;
    }
    public Boolean getIsCompetitionOn() {
        return isCompetitionOn;
    }
    public void setIsCompetitionOn(Boolean competitionOn) {
        isCompetitionOn = competitionOn;
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

    public UBoat getUBoat() {
        return uBoat;
    }


}