package enigmaMachine.reflector;

import java.io.Serializable;

public interface Reflecting extends Serializable, Cloneable {
    int passThroughReflector(int toReflect);

    String getID();

    String getLeftPermutation();

    String getRightPermutation();

    //String[] getPermutations();
}
