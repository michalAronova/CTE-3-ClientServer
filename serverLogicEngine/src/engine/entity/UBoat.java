package engine.entity;

import DTO.codeObj.CodeObj;
import DTO.contest.Contest;
import DTO.missionResult.MissionResult;
import DTO.team.Team;
import engine.Engine;
import engine.TheEngine;
import engine.decipherManager.Difficulty;
import javafx.beans.property.*;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.concurrent.Task;
import javafx.util.Pair;

import java.util.*;

public class UBoat implements Entity{
    private final String username;
    private final Engine engine;
    private final Map<String,Allies> participants;
    private final Map<String, Boolean> name2ready;
    private final List<MissionResult> candidatesList;
    private String input;
    private String output;

    private final BooleanProperty competitionOn;

    private final IntegerProperty counterReady;

    private final StringProperty winner;
    private final BooleanProperty winnerFound;

    public UBoat(String username){
        this.username = username;
        this.engine = new TheEngine();
        participants = new HashMap<>();
        name2ready = new HashMap<>();

        counterReady = new SimpleIntegerProperty(0);
        competitionOn = new SimpleBooleanProperty();
        counterReady.addListener((observable, oldValue, newValue) -> {
            if(newValue.intValue() == engine.getAlliesRequired()){ //all allies ready to start
                updateCompetitionStart();
            }
        });
        candidatesList = new LinkedList<>();
        winner = new SimpleStringProperty("");
        winnerFound = new SimpleBooleanProperty(false);
    }

    @Override
    public String getUsername() {
        return username;
    }

    public Contest getAsDTO(){
        return new Contest(getBattleFieldName(), username, isCompetitionOn().get(),
                engine.getDifficulty().toString(), engine.getAlliesRequired(), participants.size());
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
    }

    public Map<String, Boolean> getName2ready() {
        return name2ready;
    }

    public synchronized void addCandidates(MissionResult ac){
        candidatesList.add(ac);
    }
    public synchronized void removeParticipant(String username){
        participants.remove(username);
        name2ready.remove(username);
    }

    public synchronized void removeAllParticipants(){
        participants.clear();
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
        participants.forEach((username, ally) -> {
            if(name2ready.get(username)){ //only return READY allies to display...
                teams.add(ally.asTeamDTO());
            }
        });
        return teams;
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
        competitionOn.set(false);
        counterReady.set(0);
        participants.forEach((name, ally) -> {
            ally.setIsWinner(name.equals(winner));
        });
    }

    public void addResult(MissionResult result) {
        synchronized (candidatesList){
            candidatesList.add(result);
            for (Pair<String, CodeObj> candidate: result.getCandidates()) {
                if(candidate.getKey().equals(input)){
                    winner.set(result.getAllyName());
                    winnerFound.set(true);
                    updateVictory(result.getAllyName());
                }
            }
        }
    }

    public void updateAllyReady(String ally){
        name2ready.replace(ally, true);
        counterReady.set(counterReady.get() + 1);
    }

    private void start() {
        for (Allies ally: participants.values()) {
            setAlliesParams(ally); //created the DM

            new Thread(() -> ally.start(output), "Allies "+ally.getUsername()+" starting thread");
        }
    }

    private void clearForNewContest() {
        winnerFound.set(false);
        winner.set("");
        setCompetitionOn(true);
    }

    public void updateCompetitionStart() {
        clearForNewContest();
        start();
    }

    public boolean isWinnerFound() {
        return winnerFound.get();
    }

    public BooleanProperty winnerFoundProperty() {
        return winnerFound;
    }

    public String getWinner() {
        return winner.get();
    }

    public StringProperty winnerProperty() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner.set(winner);
    }

    public int getResultVersion(){
        return candidatesList.size();
    }

    public List<MissionResult> getResults(int fromIndex){
        List<MissionResult> ret;
        synchronized (candidatesList){
            if (fromIndex < 0 || fromIndex > candidatesList.size()) {
                fromIndex = 0;
            }
            ret = candidatesList.subList(fromIndex, candidatesList.size());
        }
        return ret;
    }

    public String encryptMsg(String msg){
        input = msg;
        output = engine.processMsg(msg);
        return output;
    }

    public boolean isFull(){
        return participants.size() == engine.getAlliesRequired();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UBoat)) return false;
        UBoat uBoat = (UBoat) o;
        return username.equals(uBoat.username);
    }
}
