package enigmaMachine.rotor;

import java.io.Serializable;

public interface Rotor extends AdvanceSelf, Serializable, Cloneable {
    int getID();

    String getRightPermutation();

    void setNextRotor(AdvanceSelf nextRotor);

    int rightToLeftReturnIndex(int index);

    int leftToRightReturnIndex(int index);

    void setInitialPosition(int index);

    void setInitialPosition(Character let);

    void reset();

    int getNotch();

    int getRelativeNotch();

    int getCurrentPosition();

    char getCurrentPositionChar();

    String getLeftPermutation();
}