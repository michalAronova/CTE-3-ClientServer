package engine.decipherManager.speedometer;

import enigmaMachine.keyBoard.KeyBoard;

import java.util.List;

public interface Speedometer {
    List<Character> calculateNext(List<Character> value);
}
