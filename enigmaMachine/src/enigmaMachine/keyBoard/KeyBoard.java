package enigmaMachine.keyBoard;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class KeyBoard implements Serializable, Cloneable {
    private final String keyBoard;

    public KeyBoard(String abc) {
        keyBoard = abc;
    }

    public boolean isInKeyBoard(Character c) {
        return keyBoard.contains(c.toString());
    }

    public boolean isInKeyBoard(String s) {
        s = s.toUpperCase();
        for(Character c : s.toCharArray()) {
            if(!isInKeyBoard(c)) {
                return false;
            }
        }
        return true;
    }

    public List<Character> findCharacterNotInKeyBoard(String s){
        List<Character> invalids = new ArrayList<>();
        s = s.toUpperCase();
        for(Character c : s.toCharArray()) {
            if(!isInKeyBoard(c)) {
                invalids.add(c);
            }
        }
        if(invalids.isEmpty()) {
            return null;
        }
        return invalids;
    }

    public int indexOf(char c){
        return keyBoard.indexOf(c);
    }

    public char charAt(int index) {
        return keyBoard.charAt(index);
    }

    public int length() {
        return keyBoard.length();
    }

    public List<Character> getAsCharList() {
        return keyBoard.chars()
                .mapToObj(e->(char)e).collect(Collectors.toList());
    }
    public List<Object> getAsObjList() {
        return keyBoard.chars()
                .mapToObj(e->(char)e).collect(Collectors.toList());
    }
    public String getKeyBoardString() {
        return keyBoard;
    }

    @Override
    public String toString() {
        return "KeyBoard{" + keyBoard + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        KeyBoard keyBoard1 = (KeyBoard) o;
        return Objects.equals(keyBoard, keyBoard1.keyBoard);
    }

    @Override
    public int hashCode() {
        return Objects.hash(keyBoard);
    }

    @Override
    public KeyBoard clone(){
        return new KeyBoard(this.keyBoard);
    }
}

