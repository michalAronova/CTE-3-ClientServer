package engine.entity;

import DTO.codeObj.CodeObj;
import DTO.missionResult.AlliesCandidates;
import DTO.missionResult.MissionResult;
import DTO.team.Team;
import com.sun.org.apache.xpath.internal.operations.Bool;
import engine.Engine;
import engine.TheEngine;
import engine.decipherManager.Difficulty;
import engine.decipherManager.dictionary.Dictionary;
import engine.stock.Stock;
import enigmaMachine.Machine;
import javafx.beans.property.*;
import javafx.util.Pair;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.util.*;
import java.util.concurrent.BlockingQueue;

public class UBoat implements Entity{
    private String username;
    private Engine engine;
    private Map<String,Allies> participants;
    private Map<String, Boolean> name2ready;
    private Set<AlliesCandidates> candidates;
    private String input;
    private String output;
    private boolean isFull;
    private boolean engineLoaded;
    private BlockingQueue<MissionResult> resultQueue;

    private BooleanProperty competitionOn;

    private IntegerProperty counterReady;

    public UBoat(String username){
        this.username = username;
        this.engine = new TheEngine();
        participants = new HashMap<>();
        isFull = false;
        counterReady = new SimpleIntegerProperty(0);
        competitionOn = new SimpleBooleanProperty();
        counterReady.addListener((observable, oldValue, newValue) -> {
            if(newValue.intValue() == engine.getAlliesRequired()){ //all allies ready to start
                start();
            }
        });
        name2ready = new HashMap<>();
    }

    public void setEngineLoaded(boolean engineLoaded) {
        this.engineLoaded = engineLoaded;
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
        return EntityEnum.UBOAT;
    }

    public synchronized void addParticipant(Allies ally){
        participants.put(ally.getUsername(), ally);
        name2ready.put(ally.getUsername(), false);
        isFull = participants.size() == engine.getAlliesRequired();
    }

    public Map<String, Boolean> getName2ready() {
        return name2ready;
    }

    public synchronized void addCandidates(AlliesCandidates ac){
        candidates.add(ac);
    }
    public synchronized void removeParticipant(String username){
        participants.remove(username);
        name2ready.remove(username);
        isFull = participants.size() == engine.getAlliesRequired();
    }

    public synchronized void removeAllParticipants(){
        participants.clear();
        isFull = false;
    }

    public void setAlliesParams(Allies ally){
        ally.createDM(engine.getDictionary(), engine.getMachine(),
                engine.getStock(), engine.getDifficulty(), engine.getUpdatedCode(), input);
    }

    public synchronized Map<String, Allies> getParticipants() {
        return participants;
    }

    public synchronized List<Team> getDTOParticipants(){
        List<Team> teams = new LinkedList<>();
        participants.forEach((username, ally) -> teams.add(ally.asTeamDTO()));
        return teams;
    }
    public Set<AlliesCandidates> getCandidates() {
        return candidates;
    }

    public boolean isFull() {
        return isFull;
    }

    public String getBattleFieldName(){
        return engine.getBattleFieldName();
    }

    public int getAlliesRequired(){
        return engine.getAlliesRequired();
    }

    public Difficulty getBattleLevel(){
        return engine.getBattleLevel();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UBoat)) return false;
        UBoat uBoat = (UBoat) o;
        return username.equals(uBoat.username);
    }

    public BooleanProperty isCompetitionOn() {
        return competitionOn;
    }

    public void setCompetitionOn(boolean competitionOn) {
        this.competitionOn.set(competitionOn);
    }
    @Override
    public int hashCode() {
        return Objects.hash(username);
    }

    @Override
    public String toString() {
        return "UBoat: "+username;
    }

    public String getInput() {
        return input;
    }

    public void updateVictory(String winner) {
        participants.forEach((name, ally) -> {
            ally.setIsCompetitionOn(false);
            ally.setIsWinner(name.equals(winner));
        });
        competitionOn.set(false);
    }
    public void updateAllyReady(String ally){
        name2ready.replace(ally, true);
        counterReady.set(counterReady.get() + 1);
    }


    private void start() {
        for (Allies ally: participants.values()) {
            setAlliesParams(ally);

            new Thread(() -> ally.start(input, (result) -> {
                try {
                    resultQueue.put(result);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }), "Allies "+ally.getUsername()+" starting thread");
        }
    }

    public void updateCompetitionStart() {
        setCompetitionOn(true);
        participants.forEach((allyName, ally) -> ally.setIsCompetitionOn(true));
    }
}
