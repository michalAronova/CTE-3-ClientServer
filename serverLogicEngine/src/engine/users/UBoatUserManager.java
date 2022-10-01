package engine.users;

import engine.entity.Entity;
import engine.entity.UBoat;

import java.util.HashMap;
import java.util.Map;

public class UBoatUserManager extends UserManager{
    private final Map<String, UBoat> readyUBoats;

    public UBoatUserManager() {
        super();
        readyUBoats = new HashMap<>();
    }

    @Override
    public synchronized void removeUser(String username) {
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
    }
}
