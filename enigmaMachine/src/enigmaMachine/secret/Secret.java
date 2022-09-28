package enigmaMachine.secret;

import enigmaMachine.plugBoard.Plugs;
import enigmaMachine.reflector.Reflecting;
import enigmaMachine.rotor.Rotor;

import java.util.List;
import java.util.Objects;

public class Secret {
    private final List<Rotor> rotors;
    private final Reflecting reflector;
    private final Plugs plugBoard;

    public Secret(List<Rotor> rotors, Reflecting reflector, Plugs plugBoard) {
        this.rotors = rotors;
        this.reflector = reflector;
        this.plugBoard = plugBoard;
    }

    public Plugs getPlugBoard() {
        return plugBoard;
    }

    public Reflecting getReflector() {
        return reflector;
    }

    public List<Rotor> getRotors() {
        return rotors;
    }

    @Override
    public String toString() {
        return "Secret{" +
                "rotors=" + rotors +
                ", reflector=" + reflector +
                ", plugBoard=" + plugBoard +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Secret secret = (Secret) o;
        return Objects.equals(rotors, secret.rotors) && Objects.equals(reflector, secret.reflector) && Objects.equals(plugBoard, secret.plugBoard);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rotors, reflector, plugBoard);
    }
}
