package enigmaMachine.plugBoard;

import javafx.util.Pair;

import java.io.Serializable;
import java.util.List;

public interface Plugs extends Serializable, Cloneable {
    Character passThroughPlugBoard(Character input);
    void clearAllPlugs();
    void setAllPlugs(List<Pair<Character, Character>> plugs);
    List<Pair<Character, Character>> getPlugsForCode();
}
