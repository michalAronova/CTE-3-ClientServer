package engine.entity;

import engine.Engine;
import engine.TheEngine;
import engine.decipherManager.Difficulty;

import java.util.HashMap;
import java.util.Map;

public class UBoat implements Entity{
    private String username;
    private Engine engine;
    private Map<String,Allies> participants;

    private boolean isFull;

    public UBoat(String username){
        this.username = username;
        this.engine = new TheEngine();
        participants = new HashMap<>();
        isFull = false;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public Engine getEngine() {
        return engine;
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
}
