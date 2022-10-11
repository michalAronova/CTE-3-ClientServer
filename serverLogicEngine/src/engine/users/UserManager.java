package engine.users;

import engine.Engine;
import engine.entity.Entity;

import java.util.*;

public class UserManager {
    protected final Map<String, Entity> user2entity;

    public UserManager() {
        user2entity = new HashMap<>();
    }

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

