package engine.entity;

import DTO.missionResult.AlliesCandidates;
import com.sun.org.apache.xpath.internal.operations.Bool;
import engine.Engine;
import engine.TheEngine;
import engine.decipherManager.Difficulty;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.util.Pair;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

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

    private boolean competitionOn;
    private IntegerProperty counterReady;
    public UBoat(String username){
        this.username = username;
        this.engine = new TheEngine();
        participants = new HashMap<>();
        isFull = false;
        counterReady = new SimpleIntegerProperty(0);
        counterReady.addListener((observable, oldValue, newValue) -> {
            if(newValue.intValue() == engine.getAlliesRequired()){ //all allies ready to start
                start();
            }
        });
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

    public synchronized void addCandidates(AlliesCandidates ac){
        candidates.add(ac);
    }
    public synchronized void removeParticipant(String username){
        participants.remove(username);
        isFull = participants.size() == engine.getAlliesRequired();
    }

    public synchronized void removeAllParticipants(){
        participants.clear();
        isFull = false;
    }

    public Map<String, Allies> getParticipants() {
        return participants;
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

    public boolean isCompetitionOn() {
        return competitionOn;
    }

    public void setCompetitionOn(boolean competitionOn) {
        this.competitionOn = competitionOn;
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
        competitionOn = false;
    }
    public void updateAllyReady(String ally){
        name2ready.replace(ally, true);
        counterReady.set(counterReady.get() + 1);
    }

    private void start() {
        //create DM for all allies
        //start manage agents
    }
}
