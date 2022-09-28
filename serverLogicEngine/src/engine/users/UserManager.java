package engine.users;

import engine.Engine;
import engine.TheEngine;

import java.util.*;

public class UserManager {

    protected final Set<String> usersSet;
    protected final Map<String, Engine> user2engine;

    public UserManager() {
        usersSet = new HashSet<>();
        user2engine = new HashMap<>();
    }

    public synchronized void addUser(String username) { usersSet.add(username); }

    public synchronized void removeUser(String username) {
        usersSet.remove(username);
    }

    public synchronized Set<String> getUsers() {
        return Collections.unmodifiableSet(usersSet);
    }

    public boolean isUserExists(String username) {
        return usersSet.contains(username);
    }

    public synchronized Engine getEngineFor(String username){
        return user2engine.get(username);
    }

    // below are methods for when this changes to map --------------------------------------------

    //public synchronized void addUser(String username) { user2engine.put(username, new TheEngine()); }

    //public synchronized void removeUser(String username) { user2engine.remove(username);}

    //public synchronized Map<String, Object> getUsersMap() { return Collections.unmodifiableMap(user2engine); }

    //public boolean isUserExists(String username) { return user2engine.containsKey(username); }
}

