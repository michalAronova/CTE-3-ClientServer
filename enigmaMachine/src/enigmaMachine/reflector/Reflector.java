package enigmaMachine.reflector;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Reflector implements Reflecting {
    private final Map<Integer, Integer> reflectionMap;
    private final ReflectorID id;

    public Reflector(String id, Map<Integer, Integer> reflectionMap) {
        this.id = ReflectorID.valueOf(id);
        this.reflectionMap = reflectionMap;
    }

    @Override
    public int  passThroughReflector(int toReflect) {
        return reflectionMap.get(toReflect);
    }

    @Override
    public String getID() {
        return id.toString();
    }

    @Override
    public String getLeftPermutation() {
        return null;
    }

    @Override
    public String getRightPermutation() {
        return null;
    }

//    @Override
//    public String[] getPermutations() {
//        String[] permutations = new String[2];
//        StringBuilder left = new StringBuilder();
//        StringBuilder right = new StringBuilder();
//        for(int i = 1; i <= reflectionMap.size(); i++){
//            right.append(i);
//            reflectionMap.get(i);
//        }
//    }

    @Override
    public String toString() {
        return "Reflector{" +
                "id=" + id +
                ", reflectionMap=" + reflectionMap +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reflector reflector = (Reflector) o;
        return Objects.equals(reflectionMap, reflector.reflectionMap) && id == reflector.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(reflectionMap, id);
    }

    @Override
    public Reflector clone(){
        Map<Integer, Integer> clonedMap = new HashMap<>(this.reflectionMap);
        return new Reflector(this.id.toString(), clonedMap);
    }
}
