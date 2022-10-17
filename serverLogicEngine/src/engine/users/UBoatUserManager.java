package engine.users;

import engine.entity.Entity;
import engine.entity.UBoat;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class UBoatUserManager extends UserManager{
    private final Map<String, UBoat> readyUBoats;
    private final Set<String> battleFieldNames;

    public UBoatUserManager() {
        super();
        readyUBoats = new HashMap<>();
        battleFieldNames = new HashSet<>();
    }

    @Override
    public synchronized void removeUser(String username) {
        battleFieldNames.remove(super.getUser(username).getEngine().getBattleFieldName());
        super.removeUser(username);
        readyUBoats.remove(username);
    }

    public synchronized boolean isReady(String username){
        return readyUBoats.containsKey(username);
    }

    public synchronized void addReadyUBoat(String username){
        if(super.getUsers().containsKey(username)){
            readyUBoats.put(username, (UBoat) super.getEntityObject(username));
        }
        else{
            System.out.println("trying to ready a non registered uboat!");
        }
    }

    public synchronized boolean addBattleFieldName(String battleFieldName){
        //returns TRUE if the name didn't already exist
        //hence, if this returns false - the name existed, and should handle! (ERROR)
        return battleFieldNames.add(battleFieldName.toUpperCase());
    }

    public synchronized boolean isBattleFieldExist(String battleFieldName) {
        return battleFieldNames.contains(battleFieldName.toUpperCase());
    }
    public Map<String, UBoat> getReadyUBoats() {
        return readyUBoats;
    }
}
