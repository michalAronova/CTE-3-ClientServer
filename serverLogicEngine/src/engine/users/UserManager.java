package engine.users;

import engine.Engine;
import engine.entity.Entity;

import java.util.*;

public class UserManager {

    protected final Set<String> usersSet;
    protected final Map<String, Entity> user2entity;

    public UserManager() {
        usersSet = new HashSet<>();
        user2entity = new HashMap<>();
    }

    //public synchronized void addUser(String username) { usersSet.add(username); }

    //public synchronized void removeUser(String username) { usersSet.remove(username); }

    //public synchronized Set<String> getUsers() {return Collections.unmodifiableSet(usersSet);}

    //public boolean isUserExists(String username) {return usersSet.contains(username);}


    // below are methods for when this changes to map --------------------------------------------
    public synchronized Entity getEntityObject(String username){
        return user2entity.get(username);
    }

    public synchronized void addUser(String username, Entity entity) { user2entity.put(username, entity); }

    public synchronized void removeUser(String username) { user2entity.remove(username);}

    public synchronized Map<String, Entity> getUsers() { return Collections.unmodifiableMap(user2entity); }

    public boolean isUserExists(String username) { return user2entity.containsKey(username); }

    public Entity getUser(String username) {
        return user2entity.get(username);
    }
}

