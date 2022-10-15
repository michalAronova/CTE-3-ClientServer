package engine.users;

import engine.entity.Entity;
import engine.entity.SimpleAgent;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class AgentUserManager {

    protected final Map<String, SimpleAgent> username2allyName;

    public AgentUserManager() {
        username2allyName = new HashMap<>();
    }

    public synchronized String getAllyName(String username){
        return username2allyName.get(username).getMyAlly();
    }

    public synchronized SimpleAgent getSimpleAgent(String username){ return username2allyName.get(username); }
    public synchronized void addUser(String username, SimpleAgent simpleAgent) { username2allyName.put(username, simpleAgent); }

    public synchronized void removeUser(String username) { username2allyName.remove(username);}


    public synchronized Map<String, SimpleAgent> getUsers() { return Collections.unmodifiableMap(username2allyName); }

    public boolean isUserExists(String username) { return username2allyName.containsKey(username); }
}
