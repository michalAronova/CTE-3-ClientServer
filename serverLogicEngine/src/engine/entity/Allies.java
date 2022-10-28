package engine.entity;

import DTO.agent.SimpleAgentDTO;
import DTO.agent.WorkStatusDTO;
import DTO.codeObj.CodeObj;
import DTO.dmInfo.DMInfo;
import DTO.mission.MissionDTO;
import DTO.missionResult.MissionResult;
import DTO.team.Team;
import engine.Engine;
import engine.decipherManager.DecipherManager;
import engine.decipherManager.Difficulty;
import engine.decipherManager.dictionary.Dictionary;
import engine.stock.Stock;
import enigmaMachine.Machine;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import java.util.*;

public class Allies implements Entity {
    private final String username;
    private DecipherManager DM;
    private UBoat uBoat;
    //private final Map<String, Agent> name2Agent;

    private final Map<String, SimpleAgentDTO> agentName2data;

    // wait-list for agents joining during a contest here
    private final Map<String, SimpleAgentDTO> waitingAgents;
    private Boolean isWinner;
    private String theWinner;
    private final BooleanProperty isCompetitionOn;
    private final BooleanProperty okClicked;

    private final List<MissionResult> resultList;
    private final Object resultListLock = new Object();
    private int missionSize;

    private final DMInfo noCompetitionDMInfo = new DMInfo(false, null, null, null);
    private DMInfo dmInfo = noCompetitionDMInfo;

    private final Object DMLock = new Object();

    public Allies(String username){
        this.username = username;
        //name2Agent = new HashMap<>();
        agentName2data = new HashMap<>();
        waitingAgents = new HashMap<>();
        resultList = new LinkedList<>();
        uBoat = null;
        okClicked = new SimpleBooleanProperty(false);
        okClicked.addListener((observable, oldValue, newValue) -> {
            if(newValue){ //only clear when user clicked OK
                onCompetitionFinished();
            }
        });

        isCompetitionOn = new SimpleBooleanProperty(false);
        isCompetitionOn.addListener(((observable, oldValue, newValue) -> {
            if(!newValue){ //competition finished //TODO
                dmInfo = noCompetitionDMInfo;
                //method to be called upon end of game
                //need to know if im the winner
                //things that need to happen when the game ends
                //MOVED TO - WHEN USER CLICKED OK!
            }
            else{ //competition started
                dmInfo = new DMInfo(true, uBoat.getAsDTO(), uBoat.getEngine().getKeyBoardList(),
                        uBoat.getEngine().getDictionary().getWords());
                //now when agents request to know if competition started, they can request this DTO
                //note that the method START is called from the uboat's START method!
            }
        }));
    }

    public DMInfo getDmInfo() {
        return dmInfo;
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
        isCompetitionOn.bind(uBoat.isCompetitionOn());
        dmInfo = new DMInfo(isCompetitionOn.getValue(), uBoat.getAsDTO(),
                uBoat.getKeys(), uBoat.getDictionary());
    }

    public synchronized void removeUBoat(){
        uBoat.removeParticipant(username);
        isCompetitionOn.unbind();
        isCompetitionOn.set(false);
        dmInfo = noCompetitionDMInfo;
        this.uBoat = null;
    }

    public void start(String encryption){
        System.out.println("ally start method was called");
        DM.manageAgents(encryption);
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
        DM.setMissionSize(this.missionSize);
        DM.setIsGameOn(this.isCompetitionOn);
    }

    public List<MissionDTO> pullMissions(int missionPullAmount) {
        List<MissionDTO> missions = new ArrayList<>();
        if(DM.getWorkQueue().isEmpty()) { // if the queue is empty, don't block it by entering synchronized.
                                          // simply return an empty list.
            return missions;
        }

        synchronized (DM.getWorkQueue()){
            while(!DM.getWorkQueue().isEmpty() && missionPullAmount > 0){
                --missionPullAmount;
                try {
                    MissionDTO mission = DM.getWorkQueue().take();
                    missions.add(mission);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        return missions;
    }

    public synchronized void removeAllAgents(){
        agentName2data.clear();
        waitingAgents.clear();
    }

    public synchronized void drainWaiters(){
        waitingAgents.forEach((name, DTO) -> agentName2data.put(name, new SimpleAgentDTO(DTO)));
        waitingAgents.clear();
    }

    private void onCompetitionFinished(){
        agentName2data.forEach((name, agent) -> agent.resetWorkStatus());
        drainWaiters();
        resultList.clear();
        removeUBoat();
        dmInfo = noCompetitionDMInfo;
        DM = null;
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

    public Map<String, SimpleAgentDTO> getAllAgents() {
        Map<String, SimpleAgentDTO> allAgents = new HashMap<>();
        agentName2data.forEach((name, DTO) -> allAgents.put(name, new SimpleAgentDTO(DTO)));
        waitingAgents.forEach((name, DTO) -> allAgents.put(name, new SimpleAgentDTO(DTO)));
        return allAgents;
    }
    public Map<String, SimpleAgentDTO> getMyActiveAgents() {
        return agentName2data;
    }


    public UBoat getUBoat() {
        return uBoat;
    }

    public void addResult(MissionResult result) {
        synchronized (resultListLock){
            System.out.println("trying to add results...");
            resultList.add(result);
            System.out.println("result added to "+username);
            uBoat.addResult(result);
            System.out.println("result added to "+uBoat.getUsername());
        }
    }

    public int getResultVersion(){
        return resultList.size();
    }

    public List<MissionResult> getResults(int fromIndex){
        List<MissionResult> ret;
        synchronized (resultListLock){
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

    public void setOKClicked(boolean clicked) {
        okClicked.set(clicked);
    }

    public boolean isOkClicked() {
        return okClicked.get();
    }

    public void setTheWinner(String theWinner) {
        this.theWinner = theWinner;
    }

    public String getTheWinner(){
        return theWinner;
    }
}