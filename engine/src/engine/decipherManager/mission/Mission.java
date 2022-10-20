package engine.decipherManager.mission;

import DTO.codeObj.CodeObj;
import DTO.mission.MissionDTO;
import DTO.missionResult.AlliesCandidates;
import DTO.missionResult.MissionResult;
import engine.decipherManager.dictionary.Dictionary;
import engine.decipherManager.speedometer.Speedometer;
import enigmaMachine.Machine;
import enigmaMachine.keyBoard.KeyBoard;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.util.Pair;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class Mission implements Runnable {
    private final IntegerProperty missionsDone;
    private final Machine machine;
    private final double missionSize;
    private final String toDecrypt;
    private final Set<String> dictionary;
    private List<Character> currentPositions;
    private final Speedometer speedometer;

    private final List<Pair<String, CodeObj>> candidates;

    private final BlockingQueue<MissionResult> resultQueue;

    private final BlockingQueue<Runnable> workQueue;
    private final BooleanProperty isEmptyQueue;
    private final String allyName;
    private final String agentName;

    /*public Mission(Machine machine, List<Character> startRotorsPositions, double missionSize,
                   String toDecrypt, Dictionary dictionary, Speedometer speedometer,
                   BlockingQueue<MissionResult> resultQueue, BiConsumer<Integer, Long> updateTotalMissionDone) {
        this.machine = machine;
        this.missionSize = missionSize;
        this.toDecrypt = toDecrypt;
        this.dictionary = dictionary;
        this.currentPositions = startRotorsPositions;
        this.resultQueue = resultQueue;
        machine.updateByPositionsList(currentPositions);
        this.speedometer = speedometer;
        candidates = new LinkedList<>();
    }*/

    public Mission(Machine machine, List<Character> startPositions, double missionSize,
                   String toDecrypt, Set<String> dictionary, Speedometer speedometer,
                   IntegerProperty missionsDone, BlockingQueue<MissionResult> resultQueue,
                   BlockingQueue<Runnable> workQueue, BooleanProperty isEmptyQueue,
                   String allyName, String agentName){
        this.machine = machine;
        this.missionSize = missionSize;
        this.toDecrypt = toDecrypt;
        this.dictionary = dictionary;
        this.currentPositions = startPositions;
        this.resultQueue = resultQueue;
        this.missionsDone = missionsDone;
        machine.updateByPositionsList(currentPositions);
        this.speedometer = speedometer;
        candidates = new LinkedList<>();
        this.workQueue = workQueue;
        this.isEmptyQueue = isEmptyQueue;
        this.allyName = allyName;
        this.agentName = agentName;
    }

//    public void setResultQueue(BlockingQueue<MissionResult> resultQueue){ this.resultQueue = resultQueue; }
//
//    public void setWorkQueueAndEmptyProperty(BlockingQueue<Runnable> workQueue, BooleanProperty isEmptyQueue){
//        this.workQueue = workQueue;
//        this.isEmptyQueue = isEmptyQueue;
//    }
    @Override
    public void run() {
        if(workQueue != null){
            isEmptyQueue.set(workQueue.isEmpty());
        }
//        long startTime = System.currentTimeMillis();
        for (int i = 0; i < missionSize; i++){
            //do thing:
            //  1. machine.process(toDecrypt);
            CodeObj currentCode = machine.getMachineCode();
            String processed = machine.processWord(toDecrypt);
            String[] words = processed.split(" ");
            boolean isCandidate = true;
            for (String word: Arrays.stream(words).collect(Collectors.toList())) {
            //  2. compare to dictionary
                if(!dictionary.contains(word)){
                    isCandidate = false;
                    break;
                }
            }
            if(isCandidate){
                candidates.add(new Pair<>(processed, currentCode));
            //      2.1 if yes - put in the blockingQueue of decryption candidates
            }
            currentPositions = speedometer.calculateNext(currentPositions);
            machine.updateByPositionsList(currentPositions);
        }
        if(missionSize != 0) {
            synchronized (missionsDone){
                missionsDone.set(missionsDone.get() + 1);
            }
        }
        if(!candidates.isEmpty()){
            try {
                resultQueue.put(new MissionResult(candidates, agentName, allyName));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(machine);
        sb.append(System.lineSeparator());
        sb.append(missionSize);
        sb.append(System.lineSeparator());
        sb.append(toDecrypt);
        sb.append(System.lineSeparator());
        sb.append(currentPositions);
        sb.append(System.lineSeparator());
        sb.append(missionSize);
        return sb.toString();
    }
}