package enigmaMachine.rotor;

import java.util.Objects;

public class TheRotor implements Rotor {
    private final int notch;
    private final int id;
    private int initialPosition;
    private int currentPosition;
    private final String leftPermutation;
    private final String rightPermutation;
    private AdvanceSelf next;

    public TheRotor(int id, int notch, String leftPermutation, String rightPermutation) {
        this.id = id;
        this.notch = notch;
        this.leftPermutation = leftPermutation;
        this.rightPermutation = rightPermutation;
    }

    public TheRotor(int id, int notch, String[] permutations) {
        this.id = id;
        this.notch = notch;
        this.leftPermutation = permutations[0];
        this.rightPermutation = permutations[1];
    }

    @Override
    public void setInitialPosition(int index) {
        this.initialPosition = index;
        this.currentPosition = index;
    }

    public String getRightPermutation() {
        return rightPermutation;
    }

    @Override
    public String getLeftPermutation() {
        return leftPermutation;
    }

    @Override
    public void setInitialPosition(Character let) {
        setInitialPosition(rightPermutation.indexOf(let));
    }

    public void reset() {
        this.currentPosition = this.initialPosition;
    }

    @Override
    public int getID() {
        return id;
    }

    @Override
    public void setNextRotor(AdvanceSelf nextRotor) {
        this.next = nextRotor;
    }

    /*
        rotor get index of previous value straiten to *starting position*
        therefore when search the char value of the input, starting position addition is needed
        now its fixed to base 0
        after finding the relavent index we can get the char, need to find its twin's index in the map
        map is staiten to base 0 so sub of starting position is needed
        index value returned straiten to *starting position*
         */
    @Override
    public int rightToLeftReturnIndex(int index) {
        //finds the char on base 0 of the char in the index location in the alphabet
        char curChar = rightPermutation.charAt(Math.floorMod((index + currentPosition), rightPermutation.length()));
        //finds the index of curChar in the permutation
        int permutatedIndex = leftPermutation.indexOf(curChar);
        //finds the index in currentPosition base of the char
        int indexOut = Math.floorMod((permutatedIndex - currentPosition), rightPermutation.length());
        return indexOut;
    }

    @Override
    public int leftToRightReturnIndex(int index) {
        char curChar = leftPermutation.charAt(Math.floorMod((index + currentPosition), rightPermutation.length()));
        int alphabetIndex = rightPermutation.indexOf(curChar);
        int indexOut = Math.floorMod((alphabetIndex - currentPosition), rightPermutation.length());
        return indexOut;
    }

    @Override
    public void advance() {
        currentPosition = Math.floorMod((currentPosition + 1), rightPermutation.length());
        if (currentPosition == notch && next != null) {
            next.advance();
        }
    }

    @Override
    public int getNotch() {
        return notch + 1;
    }

    @Override
    public int getRelativeNotch() {
        return Math.floorMod((notch - currentPosition), rightPermutation.length());
    }

    @Override
    public int getCurrentPosition() {
        return currentPosition;
    }

    @Override
    public char getCurrentPositionChar() {
        return rightPermutation.charAt(getCurrentPosition());
    }

    @Override
    public String toString() {
        return "Rotor Details{" +
                "id=" + id +
                ", notch=" + notch +
                ", initialPosition=" + initialPosition +
                ", currentPosition=" + currentPosition +
                ", leftPermutation='" + leftPermutation + '\'' +
                ", rightPermutation='" + rightPermutation + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TheRotor theRotor = (TheRotor) o;
        return notch == theRotor.notch && id == theRotor.id && initialPosition == theRotor.initialPosition && currentPosition == theRotor.currentPosition && Objects.equals(leftPermutation, theRotor.leftPermutation) && Objects.equals(rightPermutation, theRotor.rightPermutation) && Objects.equals(next, theRotor.next);
    }

    @Override
    public int hashCode() {
        return Objects.hash(notch, id, initialPosition, currentPosition, leftPermutation, rightPermutation, next);
    }

    @Override
    public TheRotor clone(){
        return new TheRotor(this.id, this.notch, new String[]{this.leftPermutation, this.rightPermutation});
    }
}
