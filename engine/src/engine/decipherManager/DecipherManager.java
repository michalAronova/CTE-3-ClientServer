package engine.decipherManager;

import DTO.codeObj.CodeObj;
import DTO.missionResult.MissionResult;
import engine.decipherManager.dictionary.Dictionary;
import engine.decipherManager.mission.Mission;
import engine.decipherManager.permuter.Permuter;
import engine.decipherManager.resultListener.ResultListener;
import engine.stock.Stock;
import enigmaMachine.Machine;
import enigmaMachine.reflector.Reflecting;
import enigmaMachine.rotor.Rotor;
import exceptions.taskExceptions.TaskIsCancelledException;
import javafx.beans.property.BooleanProperty;
import javafx.concurrent.Task;
import javafx.util.Pair;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.apache.commons.math3.util.CombinatoricsUtils;
import org.paukov.combinatorics3.Generator;
import schema.generated.CTEDecipher;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class DecipherManager {

    private final int WORK_QUEUE_LIMIT = 1000;
    private int agentCount;
    private Dictionary dictionary;
    private int missionSize;
    private Difficulty difficulty;
    private Map<Difficulty, Double> diff2totalWork;
    private Machine machine;
    private Stock stock;
    private CodeObj machineCode;
    private String stockEncoded;

    private double possiblePositionPermutations;
    private String machineEncoded;
    private String updatingMachineEncoding;
    private int agentCountChosen;

    private BlockingQueue<MissionResult> resultQueue;
    private BlockingQueue<Runnable>  workQueue;

    private String encryption;
    private BooleanProperty isPaused;
    private BooleanProperty isCancelled;
    private Task<Boolean> currentRunningTask;

    private Consumer<MissionResult> transferMissionResult;

    private BiConsumer<Integer, Long> updateTotalMissionDone;


    public DecipherManager(CTEDecipher cteDecipher, Machine machine,
                           Stock stock){
        dictionary = new Dictionary(cteDecipher.getCTEDictionary());
        this.machine = machine;
        this.stock = stock;
        try{
            stockEncoded = serializableToString(this.stock);
        }
        catch(IOException e){
            e.printStackTrace();
        }
        possiblePositionPermutations = calculatePermutationsCount();
    }

    private double calculatePermutationsCount() {
        return Math.pow(stock.getKeyBoard().length(), machine.getRotorCount());
    }

    public Difficulty getDifficulty() { return difficulty; }
    public int getAgentCount() { return agentCount; }
    public Dictionary getDictionary() { return dictionary; }
    public void setMissionSize(int missionSize) {
        this.missionSize = missionSize;
        createDiff2MissionAmountMap();
    }
    public void setDifficulty(Difficulty difficulty) { this.difficulty = difficulty; }
    public void setDifficulty(String difficulty) {
        switch (difficulty.toUpperCase()) {
            case "EASY":
                this.difficulty = Difficulty.EASY;
                break;
            case "MEDIUM":
                this.difficulty = Difficulty.MEDIUM;
                break;
            case "HARD":
                this.difficulty = Difficulty.HARD;
                break;
            case "NOT POSSIBLE":
                this.difficulty = Difficulty.IMPOSSIBLE;
                break;
            default:
                break;
        }
    }

    public void serializeMachine(Machine machine){
        try{
            machineEncoded = serializableToString(machine);
            updatingMachineEncoding = machineEncoded;
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    public void setIsPaused(BooleanProperty isPaused) {
        this.isPaused = isPaused;
    }

    public void setIsCancelled(BooleanProperty isCancelled) {
        this.isCancelled = isCancelled;
    }

    public void setCurrentRunningTask(Task<Boolean> task){
        currentRunningTask = task;
    }

    public void setTransferMissionResult(Consumer<MissionResult> transferMissionResult){
        this.transferMissionResult = transferMissionResult;
    }

    public void setUpdateTotalMissionDone(BiConsumer<Integer, Long> updateTotalMissionDone){
        this.updateTotalMissionDone = updateTotalMissionDone;
    }

    public Machine deepCopyMachine(String encoding){
        try{
            return (Machine)objectFromString(encoding);
        }
        catch(ClassNotFoundException | IOException e){
            e.printStackTrace();
            return null;
        }
    }

    public void manageAgents(String encryption) {
        updatingMachineEncoding = machineEncoded;
        this.encryption = encryption;

        //create result queue and register listener thread
        resultQueue = new LinkedBlockingQueue<>();
        new Thread(new ResultListener(resultQueue, transferMissionResult), "Result Listener").start();

        //create work queue and thread pool of agents
        BasicThreadFactory factory = new BasicThreadFactory.Builder()
                .namingPattern("Agent %d")
                .build();
        workQueue = new ArrayBlockingQueue<>(WORK_QUEUE_LIMIT);
        ThreadPoolExecutor threadExecutor = new ThreadPoolExecutor(agentCountChosen, agentCountChosen,
                                            Long.MAX_VALUE, TimeUnit.NANOSECONDS, workQueue , factory);

        //new Thread(() -> initiateWork(threadExecutor), "Mission Distributor").start();
        initiateWork(threadExecutor);
    }

    private void initiateWork(ThreadPoolExecutor threadExecutor) {
        threadExecutor.prestartAllCoreThreads();

        switch (difficulty){
            case EASY:
                runEasy();
                break;
            case MEDIUM:
                runMedium();
                break;
            case HARD:
                runHard();
                break;
            case IMPOSSIBLE:
                runImpossible();
                break;
            default:
                break;
        }

        threadExecutor.shutdown();
    }

    private List<Character> initializeRotorsPositions(List<Pair<Integer,Character>> origin) {
        List<Character> initialized = new ArrayList<>();
        for (Pair<Integer, Character> p: origin) {
            initialized.add(stock.getKeyBoard().charAt(0));
        }
        return initialized;
    }

    public double calcTotalMissionAmountEasy(){
        return possiblePositionPermutations / missionSize +
                (possiblePositionPermutations % missionSize !=0 ? 1 : 0);
    }

    public double calcTotalMissionAmountByDifficulty(){
        double result = calcTotalMissionAmountEasy();
        if(difficulty.equals(Difficulty.EASY)){
            return result;
        }
        result *= stock.getReflectorMap().size();
        if(difficulty.equals(Difficulty.MEDIUM)){
            return result;
        }
        result *= CombinatoricsUtils.factorialDouble(stock.getRotorsCount());
        if(difficulty.equals(Difficulty.HARD)){
            return result;
        }
        return result * CombinatoricsUtils
                .binomialCoefficientDouble(stock.getRotorMap().size(), stock.getRotorsCount());
    }

    public double getTotalMissionAmount(){
        return diff2totalWork.get(difficulty);
    }

    public void createDiff2MissionAmountMap(){
        diff2totalWork = new HashMap<>();
        double result = calcTotalMissionAmountEasy();
        diff2totalWork.put(Difficulty.EASY, result);

        result *= stock.getReflectorMap().size();
        diff2totalWork.put(Difficulty.MEDIUM, result);

        result *= CombinatoricsUtils.factorialDouble(stock.getRotorsCount());
        diff2totalWork.put(Difficulty.HARD, result);

        result *= CombinatoricsUtils
                .binomialCoefficientDouble(stock.getRotorMap().size(), stock.getRotorsCount());
        diff2totalWork.put(Difficulty.IMPOSSIBLE, result);
    }

    private void runEasy(){
        int totalMissions = 0;
        try {
            double totalMissionsAmountForPositions = possiblePositionPermutations / missionSize;
            double leftoverMissionsAmountForPositions = possiblePositionPermutations % missionSize;
            List<Character> nextRotorsPositions = initializeRotorsPositions(machineCode.getID2PositionList());

            for (int i = 0; i <= totalMissionsAmountForPositions; i++) {
                synchronized (isPaused){
                    while(isPaused.getValue()){
                        try{
                           isPaused.wait();
                        }
                        catch(InterruptedException e){
                            break;
                        }
                    }
                }
                if(isCancelled.getValue()){
                    throw new TaskIsCancelledException();
                }

                if(i != 0) {
                    nextRotorsPositions = getNextRotorsPositions(nextRotorsPositions, missionSize);
                }
                if(i == totalMissionsAmountForPositions){
                    workQueue.put(new Mission(deepCopyMachine(updatingMachineEncoding), nextRotorsPositions, leftoverMissionsAmountForPositions,
                            encryption, dictionary, this::speedometer, resultQueue, updateTotalMissionDone));
                }
                else{
                    workQueue.put(new Mission(deepCopyMachine(updatingMachineEncoding), nextRotorsPositions, missionSize,
                            encryption, dictionary, this::speedometer, resultQueue, updateTotalMissionDone));
                }
                totalMissions++;

            }
        }
        catch (InterruptedException e) {
            System.out.println(Thread.currentThread().getName() + " was interrupted.");
        }
    }

    private void runMedium() {
        for (Reflecting reflector: stock.getReflectorMap().values()) {
            Machine machine = deepCopyMachine(updatingMachineEncoding);
            machine.setReflector(reflector);
            serializeUpdatingMachine(machine);
            runEasy();
        }
    }

    private void runHard() {
        Machine machine = deepCopyMachine(updatingMachineEncoding);
        Permuter permuter =  new Permuter(stock.getRotorsCount());
        List<Rotor> rotors = machine.getRotors();
        List<Rotor> newPermutation = new ArrayList<>(); 
        int[] perms;
        
        while((perms = permuter.getNext()) != null){
            for (int perm : perms) {
                newPermutation.add(rotors.get(perm));
            }
            machine.setRotors(newPermutation);
            serializeUpdatingMachine(machine);
            runMedium();
            newPermutation.clear();
        }
    }

    private void runImpossible() {
        List<List<Integer>> permutations = new LinkedList<>();

        Generator.combination(stock.getRotorMap().keySet())
                .simple(stock.getRotorsCount())
                .stream()
                .forEach(permutations::add);

        for (List<Integer> perm: permutations) {
            Machine machine = deepCopyMachine(updatingMachineEncoding);
            machine.setRotors(getRotorListFromIDList(perm));
            serializeUpdatingMachine(machine);
            runHard();
        }
    }

    private List<Rotor> getRotorListFromIDList(List<Integer> perm) {
        List<Rotor> result = new LinkedList<>();
        for (Integer id: perm) {
            result.add(stock.getRotorMap().get(id));
        }
        return result;
    }

    private void serializeUpdatingMachine(Machine machine) {
        try {
            updatingMachineEncoding = serializableToString(machine);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private List<Character> getNextRotorsPositions(List<Character> start, int missionSize){
        List<Character> characters = new ArrayList<>(start);
        for(int i = 0; i < missionSize ; i++){
            characters = speedometer(characters);
        }
        return characters;
    }

    private List<Pair<Integer, Character>> setNewCharacters(List<Pair<Integer, Character>> start, List<Character> characters) {
        List<Pair<Integer, Character>> updated = new ArrayList<>();
        int i = 0;
        for (Pair<Integer, Character> p: start) {
            updated.add(new Pair<>(p.getKey(), characters.get(i)));
            ++i;
        }
        return updated;
    }

    private List<Character> speedometer(List<Character> characters) {
        List<Character> updated = new ArrayList<>(characters);
        List<Character> keys = stock.getKeyBoard().getAsCharList();
        String keysString = stock.getKeyBoard().getKeyBoardString();

        for(int i = 0; i < characters.size(); i++){
            if(updated.get(i).equals(keys.get(keys.size() - 1))){
                updated.set(i, keys.get(0));
            }
            else{
                updated.set(i, keys.get(keysString
                                            .indexOf(
                                                updated.get(i)) + 1));
                break;
            }
        }
        return updated;
    }

    public void setMachineCode(CodeObj code) { machineCode = code; }

    private static String serializableToString( Serializable o ) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(o);
        oos.close();
        return Base64.getEncoder().encodeToString(baos.toByteArray());
    }

    private static Object objectFromString(String s) throws IOException, ClassNotFoundException
    {
        byte [] data = Base64.getDecoder().decode(s);
        ObjectInputStream ois = new ObjectInputStream(
                new ByteArrayInputStream(data));
        Object o  = ois.readObject();
        ois.close();
        return o;
    }

    public void setAgentCountChosen(int agentCountChosen) {
        this.agentCountChosen = agentCountChosen;
    }
}

