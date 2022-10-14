package engine;

import DTO.codeHistory.CodeHistory;
import DTO.codeHistory.Translation;
import DTO.codeObj.CodeObj;
import DTO.techSpecs.TechSpecs;
import engine.decipherManager.DecipherManager;
import engine.decipherManager.Difficulty;
import engine.decipherManager.dictionary.Dictionary;
import engine.stock.Stock;
import enigmaMachine.Machine;
import enigmaMachine.secret.Secret;
import javafx.util.Pair;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.List;

public interface Engine extends Serializable {
    boolean loadDataFromXML(String path);

    boolean loadDataFromXML(InputStream inputStream);

    void unloadEngine();

    TechSpecs showTechSpecs();

    void setByAutoGeneratedCode();

    CodeObj getInitialCode(); //same as above...
    void setMachine(CodeObj machineCode);

    String processMsg(String msg);

    Character processCharacterWithoutHistory(Character character);

    void resetMachine();

    void validateAndSetReflector(CodeObj underConstructionCode, String wantedReflectorID);

    void validateAndSetPlugs(CodeObj underConstructionCode, String plugs);

    void validateAndSetRotors(CodeObj underConstructionCode, String rotors);

    void validateAndSetRotorPositions(CodeObj underConstructionCode, String positionsOfRotors);

    List<CodeHistory> showCodeHistory();

    Pair<CodeObj, Translation> getLastTranslationMade();

    int getProcessedMsgsCnt();

    List<Character> getKeyBoardList();

    CodeObj getUpdatedCode();

    int getRotorsCount();

    int getTotalRotorAmount();

    int getReflectorCount();

    boolean isStockLoaded();

    void enterManualHistory(String input, String output);

    String processWord(String word);

    public String getBattleFieldName();

    public Difficulty getBattleLevel();

    public int getAlliesRequired();

    Dictionary getDictionary();

    Machine getMachine();

    Stock getStock();

    Difficulty getDifficulty();
}