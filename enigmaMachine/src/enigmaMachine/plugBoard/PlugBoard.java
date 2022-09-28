package enigmaMachine.plugBoard;

import enigmaMachine.keyBoard.KeyBoard;
import javafx.util.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class PlugBoard implements Plugs {

    private Map<Character, Character> plugBoard;
    private KeyBoard keyBoard;

    public PlugBoard(@NotNull List<Pair<Character, Character>> plugs, KeyBoard keyBoard) {
        this.keyBoard = keyBoard;
        plugBoard = new HashMap<>();
        clearAllPlugs();
        setAllPlugs(plugs);
    }

    @Override
    public Character passThroughPlugBoard(Character input) {
        return plugBoard.get(input);
    }

    @Override
    public void clearAllPlugs() {
        keyBoard.getAsCharList().forEach(c -> plugBoard.put(c, c));
    }

    @Override
    public void setAllPlugs(List<Pair<Character, Character>> plugs) {
        plugs.forEach(p -> {
            plugBoard.replace(p.getKey(), p.getValue());
            plugBoard.replace(p.getValue(), p.getKey());
        });
    }

    public List<Pair<Character, Character>> getPlugsForCode(){
        Map<Character, Character> singleMap = new HashMap<>();
        plugBoard.forEach((k, v) -> {
            if(!k.equals(v)){
                if(!singleMap.containsKey(v)){
                    singleMap.put(k,v);
                }
            }
        });

        List<Pair<Character, Character>> plugs = new ArrayList<>();
        singleMap.forEach((c1, c2) -> {
            if(!c1.equals(c2)){
                plugs.add(new Pair<>(c1, c2));
            }
        });
        return plugs;
    }

    @Override
    public String toString() {
        return "PlugBoard{" + plugBoard + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlugBoard plugBoard1 = (PlugBoard) o;
        return Objects.equals(plugBoard, plugBoard1.plugBoard) && Objects.equals(keyBoard, plugBoard1.keyBoard);
    }

    @Override
    public int hashCode() {
        return Objects.hash(plugBoard, keyBoard);
    }
}
