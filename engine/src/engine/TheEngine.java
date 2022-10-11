package engine;

import DTO.codeHistory.CodeHistory;
import DTO.codeHistory.Translation;
import DTO.codeObj.CodeObj;
import engine.decipherManager.Difficulty;
import engine.decipherManager.dictionary.Dictionary;
import engine.stock.Stock;
import engine.validator.MachineValidator;
import engine.validator.Validator;
import enigmaMachine.Machine;
import enigmaMachine.keyBoard.KeyBoard;
import enigmaMachine.plugBoard.PlugBoard;
import enigmaMachine.plugBoard.Plugs;
import enigmaMachine.reflector.Reflecting;
import enigmaMachine.reflector.ReflectorID;
import enigmaMachine.rotor.Rotor;
import enigmaMachine.secret.Secret;
import exceptions.InputException.InputException;
import exceptions.InputException.ObjectInputException;
import exceptions.InputException.OutOfBoundInputException;
import exceptions.XMLException.InvalidXMLException;
import exceptions.XMLException.XMLExceptionMsg;
import javafx.util.Pair;
import schema.generated.CTEDecipher;
import schema.generated.CTEEnigma;
import schema.generated.CTEMachine;
import DTO.techSpecs.TechSpecs;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class TheEngine implements Engine {
    private Stock stock;
    private Machine machine;

    private Dictionary dictionary;
    private CodeObj initialCode;
    private int processedMsgsCnt;
    private Pair<CodeObj, Translation> lastTranslationMade;
    private final LinkedList<CodeHistory> codesHistories = new LinkedList<>();

    private String battleFieldName;
    private Difficulty battleLevel;

    private int alliesRequired;
    private final static String JAXB_XML_PACKAGE_NAME = "schema.generated";
    private final static int MIN_ROTOR_COUNT = 2;
    private final static int MAX_ROTOR_COUNT = 99;

    public TheEngine(){
        initialCode = new CodeObj();
    }

    @Override
    public boolean loadDataFromXML(InputStream inputStream){
        try {
            CTEEnigma enigma = deserializeFrom(inputStream);
            CTEDecipher cteDecipher = enigma.getCTEDecipher();
            CTEMachine cteMachine = enigma.getCTEMachine();
            Validator machineValidator = new MachineValidator(cteMachine, MIN_ROTOR_COUNT, MAX_ROTOR_COUNT);
            try{
                machineValidator.validate();
                this.battleFieldName = enigma.getCTEBattlefield().getBattleName();
                this.battleLevel = Difficulty.valueOf(enigma.getCTEBattlefield().getLevel().trim().toUpperCase());
                this.alliesRequired = enigma.getCTEBattlefield().getAllies();
                this.dictionary = new Dictionary(enigma.getCTEDecipher().getCTEDictionary());
                stock = new Stock(cteMachine.getCTERotors().getCTERotor(), cteMachine.getCTEReflectors().getCTEReflector(),
                        new KeyBoard(cteMachine.getABC().trim().toUpperCase()), cteMachine.getRotorsCount());
                this.machine = new Machine(stock.getKeyBoard(), stock.getRotorsCount());
                this.initialCode = null; //new machine - no code yet!
                this.processedMsgsCnt = 0; //new machine - new count!
                this.codesHistories.clear(); //new machine - new histories!
                return true;
            } catch (InvalidXMLException | OutOfBoundInputException e) {
                throw e;
            }
        } catch (JAXBException e) {
            throw new InvalidXMLException(XMLExceptionMsg.INVALIDFILE, "file not found");
        }
    }

    @Override
    public boolean loadDataFromXML(String path) {
        try {
            if(!path.endsWith(".xml")) {
                throw new InvalidXMLException(XMLExceptionMsg.INVALIDFILE, "not an XML");
            }
            File file = new File(path);
            InputStream inputStream = new FileInputStream(file);
            CTEEnigma enigma = deserializeFrom(inputStream);
            CTEDecipher cteDecipher = enigma.getCTEDecipher();
            CTEMachine cteMachine = enigma.getCTEMachine();
            Validator machineValidator = new MachineValidator(cteMachine, MIN_ROTOR_COUNT, MAX_ROTOR_COUNT);
            try{
                machineValidator.validate();
                this.battleFieldName = enigma.getCTEBattlefield().getBattleName();
                this.battleLevel = Difficulty.valueOf(enigma.getCTEBattlefield().getLevel().trim().toUpperCase());
                this.alliesRequired = enigma.getCTEBattlefield().getAllies();
                this.dictionary = new Dictionary(enigma.getCTEDecipher().getCTEDictionary());
                stock = new Stock(cteMachine.getCTERotors().getCTERotor(), cteMachine.getCTEReflectors().getCTEReflector(),
                        new KeyBoard(cteMachine.getABC().trim().toUpperCase()), cteMachine.getRotorsCount());
                this.machine = new Machine(stock.getKeyBoard(), stock.getRotorsCount());
                this.initialCode = null; //new machine - no code yet!
                this.processedMsgsCnt = 0; //new machine - new count!
                this.codesHistories.clear(); //new machine - new histories!
                return true;
            } catch (InvalidXMLException | OutOfBoundInputException e) {
                throw e;
            }
        } catch (JAXBException | FileNotFoundException e) {
            throw new InvalidXMLException(XMLExceptionMsg.INVALIDFILE, "file not found");
        }
    }

    @Override
    public void setMachine(CodeObj machineCode){
        codesHistories.add(new CodeHistory(machineCode));
        initialCode = machineCode;
        machine.updateBySecret(secretFromCodeObj(initialCode));
        initialCode.setNotchRelativeLocation(getRelativeNotchesMap(initialCode.getID2PositionList()));
    }

    private Secret secretFromCodeObj(CodeObj machineCode){
        List<Rotor> rotors = new ArrayList<>();
        for(Pair<Integer,Character> id2start: machineCode.getID2PositionList()){
            Rotor rotor = stock.getRotorMap().get(id2start.getKey());
            rotor.setInitialPosition(id2start.getValue());
            rotors.add(rotor);
        }

        Reflecting reflector = stock.getReflectorMap().get(machineCode.getReflectorID());
        Plugs plugBoard = new PlugBoard(machineCode.getPlugs(), stock.getKeyBoard());
        return new Secret(rotors, reflector, plugBoard);
    }

    @Override
    public String processMsg(String msg){
        msg = msg.toUpperCase();
        StringBuilder sb = new StringBuilder();
        List<Character> invalid = stock.getKeyBoard().findCharacterNotInKeyBoard(msg);
        if( invalid != null) {
            throw new ObjectInputException("Invalid input: not a recognized character",
                                                    stock.getKeyBoard().getAsObjList(), new ArrayList<>(invalid));
        }
        long startTime = System.nanoTime();
        for(Character c : msg.toCharArray()){
            sb.append(machine.process(c));
        }
        long endTime = System.nanoTime();
        long timeElapsed = endTime - startTime;
        processedMsgsCnt++;
        Translation translation = new Translation(msg, sb.toString(), timeElapsed);
        codesHistories.getLast().addTranslation(translation);
        lastTranslationMade = new Pair<>(codesHistories.getLast().getCode(), translation);
        return sb.toString();
    }
    @Override
    public Pair<CodeObj, Translation> getLastTranslationMade() {
        return lastTranslationMade;
    }
    @Override
    public Character processCharacterWithoutHistory(Character c){
        if (stock.getKeyBoard().getAsCharList().contains(c)){
            return machine.process(c);
        }
        return null;
    }

    private CodeObj autoGenerateCodeObj(){
        String reflectorID = raffleReflector();
        //List<Pair<Character, Character>> plugs = rafflePlugs();
        List<Pair<Character, Character>> plugs = new ArrayList<>();
        List<Pair<Integer, Character>> rotorsID2Position = raffleRotors();
        return new CodeObj(rotorsID2Position, reflectorID, plugs);
    }

    private Map<Integer, Integer> getRelativeNotchesMap(List<Pair<Integer, Character>> rotorsID2Position){
        Map<Integer,Integer> relativeNotchesMap = new HashMap<>();
        rotorsID2Position.forEach(p ->
                relativeNotchesMap.put(p.getKey(),
                stock.getRotorMap().get(p.getKey()).getRelativeNotch()));
        return relativeNotchesMap;
    }

    @Override
    public void setByAutoGeneratedCode(){
        CodeObj generatedCode = autoGenerateCodeObj();
        setMachine(generatedCode);
    }

    private String raffleReflector() {
        int reflectorCount = stock.getReflectorMap().size();
        Random random = new Random();
        int generated = random.nextInt(reflectorCount);
        return ReflectorID.values()[generated].toString();
    }

    private List<Pair<Character, Character>> rafflePlugs() {
        List<Pair<Character, Character>> plugs = new ArrayList<>();
        int maxPlugs = stock.getKeyBoard().length() / 2;
        Random random = new Random();
        int plugsAmount = random.nextInt(maxPlugs + 1); //how many plugs will there be? (0 - length/2)
        int curRandom;
        char first, second;
        List<Character> keys = stock.getKeyBoard().getAsCharList();
        for(int i = 0; i < plugsAmount; i++){
            curRandom = random.nextInt(keys.size());
            first = keys.get(curRandom);
            keys.remove(curRandom);
            curRandom = random.nextInt(keys.size());
            second = keys.get(curRandom);
            keys.remove(curRandom);
            plugs.add(new Pair<>(first, second));
        }
        return plugs;
    }

    private List<Pair<Integer,Character>> raffleRotors() {
        List<Pair<Integer,Character>> raffledRotors = new ArrayList<>();
        List<Integer> ids = new LinkedList<>(stock.getRotorMap().keySet());
        Random random = new Random();
        int startingIndex, rotorIDidx, rotorID;
        char startingChar;

        for(int i = 0; i < stock.getRotorsCount(); i++){
            rotorIDidx = random.nextInt(ids.size());
            rotorID = ids.get(rotorIDidx);
            ids.remove(rotorIDidx);
            startingIndex = random.nextInt(stock.getKeyBoard().length());
            startingChar = stock.getRotorMap().get(rotorID).getRightPermutation().charAt(startingIndex);
            raffledRotors.add(new Pair<>(rotorID, startingChar));
        }
        return raffledRotors;
    }


    private CTEEnigma deserializeFrom(InputStream in) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(JAXB_XML_PACKAGE_NAME);
        Unmarshaller u = jc.createUnmarshaller();
        return (CTEEnigma) u.unmarshal(in);
    }

    public TechSpecs showTechSpecs() {
        return new TechSpecs(stock.getRotorMap().size(), stock.getRotorsCount(),
                stock.getReflectorMap().size(), processedMsgsCnt, initialCode, getUpdatedCode());
    }

    @Override
    public void resetMachine(){
        machine.resetRotorsToInitial();
    }

    @Override
    public void validateAndSetReflector(CodeObj underConstructionCode, String wantedReflectorID){
        int wantedReflectorIDParseInt = Integer.parseInt(wantedReflectorID);
        String IDRome = ReflectorID.getRomeByInteger(wantedReflectorIDParseInt);
        if(stock.getReflector(IDRome) != null) {
            underConstructionCode.setReflectorID(IDRome);
        }
        else{
            throw new ObjectInputException("Invalid Reflector input ",
                    new ArrayList<>(stock.getReflectorMap().keySet()));
        }
    }

    @Override
    public void validateAndSetPlugs(CodeObj underConstructionCode, String plugs){
        if(plugs.length() % 2 != 0) {
            throw new InputException("Error: You entered an odd number of characters. Expecting two characters for each plug");
        }
        plugs = plugs.toUpperCase();
        List<Character> invalid = stock.getKeyBoard().findCharacterNotInKeyBoard(plugs);
        if(invalid != null) {
            throw new ObjectInputException("Error: invalid values for plugs. ",stock.getKeyBoard().getAsObjList(), new ArrayList<>(invalid));
        }
        Map<Character, Integer> char2count = new HashMap<>();
        for(Character c : plugs.toCharArray()){
            if(char2count.getOrDefault(c, 0) != 0){
                throw new InputException("Error: each character may appear once at most!");
            }
            else {
                char2count.put(c, 1);
            }
        }
        underConstructionCode.setPlugs(createPairsFromString(plugs));
    }

    private List<Pair<Character, Character>> createPairsFromString(String plugs){
        List<Pair<Character, Character>> plugsList = new ArrayList<>();
        Pair<Character, Character> pair;
        char first, second;
        for(int i = 0; i<plugs.length(); i += 2) {
            first = plugs.charAt(i);
            second = plugs.charAt(i + 1);
            pair = new Pair<>(first, second);
            plugsList.add(pair);
        }
        return plugsList;
    }
    @Override
    public void validateAndSetRotors(CodeObj underConstructionCode, String rotors) {
        if (!rotors.matches("[0-9, ]+")) {
            throw new InputException("Rotors IDs are only numbers");
        }
        List<Integer> elements = Arrays
                                    .stream(rotors.split(","))
                                    .filter(s -> !s.equals(""))
                                    .map(String::trim)
                                    .map(Integer::valueOf)
                                    .collect(Collectors.toList());

        if(elements.size() != stock.getRotorsCount()){
            throw new ObjectInputException("Error: invalid rotor amount",stock.getRotorsCount());
        }

        Map<Integer, Integer> element2count = new HashMap<>();
        for (Integer id: elements) {
            if(id < 1 || id > stock.getRotorMap().size()){
                throw new OutOfBoundInputException("Rotor id", 1, stock.getRotorMap().size()); //maybe boundException?
            }
            if(element2count.getOrDefault(id, 0) != 0) {
                throw new InputException("Can't use same rotor more than once!");
            }
            element2count.put(id,1);
        }
        Collections.reverse(elements);
        underConstructionCode.setRotorIDs(elements);
    }

    @Override
    public void validateAndSetRotorPositions(CodeObj underConstructionCode, String positionsOfRotors) {
        if(positionsOfRotors.length() != stock.getRotorsCount()) {
            throw new ObjectInputException("Error: invalid rotor positions amount", stock.getRotorsCount());
        }
        positionsOfRotors = positionsOfRotors.toUpperCase();
        List<Character> invalid = stock.getKeyBoard().findCharacterNotInKeyBoard(positionsOfRotors);
        if(invalid != null) {
            throw new ObjectInputException("Error: invalid values for positions. ",
                    stock.getKeyBoard().getAsObjList(), new ArrayList<>(invalid));
        }
        List<Character> rotorPositions = positionsOfRotors
                                                    .chars()
                                                    .mapToObj(e->(char)e)
                                                    .collect(Collectors.toList());

        Collections.reverse(rotorPositions);
        underConstructionCode.setRotorPos(rotorPositions);
    }

    @Override
    public List<CodeHistory> showCodeHistory() { return codesHistories; }
    @Override
    public int getProcessedMsgsCnt() {
        return processedMsgsCnt;
    }

    @Override
    public CodeObj getInitialCode() {
        return initialCode;
    }
    @Override
    public CodeObj getUpdatedCode() {
        if(initialCode == null){
            return null;
        }
        List<Pair<Integer, Character>> updatedPosList = new LinkedList<>();
        Integer ID;
        char pos;
        for(Pair<Integer, Character> p : initialCode.getID2PositionList()){
            ID = p.getKey();
            pos = stock.getRotorMap().get(ID).getCurrentPositionChar();
            updatedPosList.add(new Pair<>(ID, pos));
        }
        return new CodeObj(updatedPosList, initialCode,
                getRelativeNotchesMap(initialCode.getID2PositionList()));
    }

    public int getRotorsCount(){
        return stock.getRotorsCount();
    }
    public int getTotalRotorAmount(){ return stock.getRotorMap().size(); }
    public int getReflectorCount(){ return stock.getReflectorMap().size(); }
    public boolean isStockLoaded(){ return stock != null; }

    public void enterManualHistory(String input, String output){
        if(!initialCode.toString().equals(codesHistories.getLast().getCode().toString())){
            codesHistories.addLast(new CodeHistory(initialCode));
        }
        Random rand = new Random();
        Translation newTranslation = new Translation(input, output, rand.nextInt(140000) + 110000);
        codesHistories.getLast().addTranslation(newTranslation);
        lastTranslationMade = new Pair<>(codesHistories.getLast().getCode(), newTranslation);
    }

    public List<Character> getKeyBoardList(){ return stock.getKeyBoard().getAsCharList(); }

    @Override
    public String processWord(String word) {
        return machine.processWord(word);
    }

    @Override
    public String toString() {
        return "TheEngine{" +
                "stock=" + stock +
                ", machine=" + machine +
                ", initialCode=" + initialCode +
                ", processedMsgsCnt=" + processedMsgsCnt +
                ", codesHistories=" + codesHistories +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TheEngine theEngine = (TheEngine) o;
        return processedMsgsCnt == theEngine.processedMsgsCnt && Objects.equals(stock, theEngine.stock) && Objects.equals(machine, theEngine.machine) && Objects.equals(initialCode, theEngine.initialCode) && Objects.equals(codesHistories, theEngine.codesHistories);
    }

    public String getBattleFieldName() {
        return battleFieldName;
    }

    public Difficulty getBattleLevel() {
        return battleLevel;
    }

    public int getAlliesRequired() {
        return alliesRequired;
    }

    public Dictionary getDictionary(){
        return dictionary;
    }

    public Machine getMachine(){
        return machine;
    }

    public Stock getStock(){
        return stock;
    }

    public Difficulty getDifficulty(){
        return battleLevel;
    }

    @Override
    public int hashCode() {
        return Objects.hash(stock, machine, initialCode, processedMsgsCnt, codesHistories);
    }
}