package engine.entity;

import engine.Engine;
import engine.TheEngine;
import engine.decipherManager.Difficulty;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.util.Pair;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class UBoat implements Entity{
    private String username;
    private Engine engine;
    private Map<String, Allies> participants;
    private boolean isFull;
    private boolean engineLoaded;

    public UBoat(String username){
        this.username = username;
        this.engine = new TheEngine();
        participants = new HashMap<>();
        isFull = false;
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
        isFull = participants.size() == engine.getAlliesRequired();
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

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }

    @Override
    public String toString() {
        return "UBoat: "+username;
    }
}
