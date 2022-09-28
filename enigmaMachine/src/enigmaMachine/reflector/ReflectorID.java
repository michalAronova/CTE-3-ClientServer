package enigmaMachine.reflector;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public enum ReflectorID implements Serializable {
    I(1),
    II(2),
    III(3),
    IV(4),
    V(5);
    private final int id;

    private ReflectorID(int id) {
        this.id = id;
    }
    public int getId() {
        return id;
    }
    public static String getRomeByInteger(int id){
        for(ReflectorID ID: ReflectorID.values()){
            if(ID.getId() == id){
                return ID.name();
            }
        }
        return null;
    }

    public static List<String> getListToLimit(int limit){
        List<String> IDs = new ArrayList<>();
        for (int i = 0; i < limit; i++) {
            IDs.add(ReflectorID.values()[i].toString());
        }
        return IDs;
    }

    @Override
    public String toString() {
        return name();
    }
}
