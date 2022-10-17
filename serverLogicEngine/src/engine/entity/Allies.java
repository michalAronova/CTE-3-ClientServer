package engine.entity;

import DTO.agent.SimpleAgentDTO;
import DTO.agent.WorkStatusDTO;
import DTO.codeObj.CodeObj;
import DTO.mission.MissionDTO;
import DTO.missionResult.MissionResult;
import DTO.team.Team;
import engine.Engine;
import engine.decipherManager.DecipherManager;
import engine.decipherManager.Difficulty;
import engine.decipherManager.dictionary.Dictionary;
import engine.decipherManager.mission.Mission;
import engine.decipherManager.resultListener.ResultListener;
import engine.stock.Stock;
import enigmaMachine.Machine;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;

public class Allies implements Entity {
    private String username;
    private DecipherManager DM;
    private UBoat uBoat;
    //private final Map<String, Agent> name2Agent;

    private final Map<String, SimpleAgentDTO> agentName2data;

    // wait-list for agents joining during a contest here
    private final Map<String, SimpleAgentDTO> waitingAgents;

    private Boolean isWinner;
    private final BooleanProperty isCompetitionOn;

    private final BlockingQueue<MissionResult> resultQueue;
    private final List<MissionResult> resultList;
    private int missionSize;

    private final Object DMLock = new Object();

    public Allies(String username){
        this.username = username;
        //name2Agent = new HashMap<>();
        agentName2data = new HashMap<>();
        waitingAgents = new HashMap<>();
        resultQueue = new LinkedBlockingQueue<>();
        resultList = new LinkedList<>();
        uBoat = null;
        isCompetitionOn = new SimpleBooleanProperty(false);
        isCompetitionOn.addListener(((observable, oldValue, newValue) -> {
            if(!newValue){ //competition finished //TODO
                //method to be called upon end of game
                //need to know if im the winner
                //things that need to happen when the game ends
            }
            else{ //competition started
                //send info to agents: dictionary and keyboard (and that competition started)
                //active servlet?
            }
        }));
    }

    public Team asTeamDTO(){
        return new Team(username, agentName2data.size() + waitingAgents.size(), missionSize);
    }

    public int getMissionSize() {
        return missionSize;
    }

    public void setMissionSize(int missionSize) {
        this.missionSize = missionSize;
    }

    public synchronized void setUBoat(UBoat uBoat){
        this.uBoat = uBoat;
        uBoat.addParticipant(this);
        isCompetitionOn.bind(uBoat.isCompetitionOn());
    }

    public synchronized void removeUBoat(){
        uBoat.removeParticipant(username);
        isCompetitionOn.unbind();
        this.uBoat = null;
    }

    public void start(String encryption, Consumer<MissionResult> transferMissionResultToUBoat){
//        new Thread(new ResultListener(resultQueue, transferMissionResultToUBoat),
//                "Allies "+username+" Result Listener").start();
//        DM.isWorkQueueCreated().addListener((observable, oldValue, newValue) -> {
////            try {
////                Thread.sleep(3000);
////            } catch (InterruptedException e) {
////                throw new RuntimeException(e);
////            }
//            if(newValue){
//                addWorkQueueToAgents();
//            }
//        });
        DM.manageAgents(encryption); //start creating missions
    }

    public synchronized void addAgentData(SimpleAgentDTO simpleAgentDTO) {
        if(isCompetitionOn.getValue()){
            waitingAgents.put(simpleAgentDTO.getName(), simpleAgentDTO);
        }
        else {
            agentName2data.put(simpleAgentDTO.getName(), simpleAgentDTO);
        }
    }

    public synchronized void removeAgent(String agentName) {
        if(agentName2data.remove(agentName) == null) {
            waitingAgents.remove(agentName);
        }
    }

    public void createDM(Dictionary dictionary, Machine machine,
                         Stock stock, Difficulty difficulty, CodeObj code, String input){
        DM = new DecipherManager(dictionary, machine, stock, difficulty, code, input);
    }

    private void addWorkQueueToAgents() {
        //method will need to change as Allies will no longer hold reference to its Agents!
//        for(Agent agent: name2Agent.values()){
//            agent.decipher((result) -> {
//                try {
//                    System.out.println(result.getEntityName()+" found result *********************************************");
//                    resultQueue.put(result);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            });
//        }
    }

    public List<MissionDTO> pullMissions(int missionPullAmount) {
        List<MissionDTO> missions = new ArrayList<>();
        synchronized (DM.getWorkQueue()){
            while(!DM.getWorkQueue().isEmpty() && missionPullAmount > 0){
                --missionPullAmount;
                try {
                    missions.add(DM.getWorkQueue().take());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        return missions;
    }

    public synchronized void removeAllAgents(){
        agentName2data.clear();
    }

    public synchronized void drainWaiters(){
        waitingAgents.forEach((name, DTO) -> agentName2data.put(name, new SimpleAgentDTO(DTO)));
        waitingAgents.clear();
    }

    public List<MissionResult> getResultList(){
        synchronized (resultQueue){
            resultQueue.drainTo(resultList);
        }
        return resultList;
    }

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
    public BooleanProperty isCompetitionOnProperty() {
        return isCompetitionOn;
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }

    @Override
    public String toString() {
        return "Allies: "+ username;
    }

    public Map<String, SimpleAgentDTO> getMyAgents() {
        return agentName2data;
    }

    public UBoat getUBoat() {
        return uBoat;
    }

    public void addResult(MissionResult result) {
        synchronized (resultList){
            resultList.add(result);
            uBoat.addResult(result);
        }
    }

    public int getResultVersion(){
        return resultList.size();
    }

    public List<MissionResult> getResults(int fromIndex){
        List<MissionResult> ret;
        synchronized (resultList){
            if (fromIndex < 0 || fromIndex > resultList.size()) {
                fromIndex = 0;
            }
            ret = resultList.subList(fromIndex, resultList.size());
        }
        return ret;
    }

    public void updateAgentWorkStatus(String agentName, WorkStatusDTO workStatus) {
        agentName2data.get(agentName).setWorkStatus(workStatus);

    }

    public double getTotalMissionDone() {
        double totalMission = 0;
        synchronized (this){
            for (SimpleAgentDTO agent: agentName2data.values()) {
                totalMission += agent.getWorkStatus().getMissionDone();
            }
        }
        return totalMission;
    }
}