package battleField.utils;

import battleField.servlets.chat.ChatManager;
import engine.ServerEngine;
import engine.users.AgentUserManager;
import engine.users.UBoatUserManager;
import engine.users.UserManager;

import engine.users.UsernameManager;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;

import static parameters.ConstantParams.INT_PARAMETER_ERROR;

public class ServletUtils {

    private static final String USERNAME_MANAGER_ATTRIBUTE_NAME = "usernameManager";
    private static final String UBOAT_USER_MANAGER_ATTRIBUTE_NAME = "uBoatUserManager";
    private static final String ALLIES_USER_MANAGER_ATTRIBUTE_NAME = "alliesUserManager";
    private static final String AGENT_USER_MANAGER_ATTRIBUTE_NAME = "agentUserManager";
    private static final String CHAT_MANAGER_ATTRIBUTE_NAME = "chatManager";

    private static final String SERVER_ENGINE_ATTRIBUTE_NAME ="serverEngine";

    /*
    Note how the synchronization is done only on the question and\or creation of the relevant managers and once they exists -
    the actual fetch of them is remained un-synchronized for performance POV
     */
    private static final Object usernameManagerLock = new Object();
    private static final Object uboatUserManagerLock = new Object();
    private static final Object alliesUserManagerLock = new Object();
    private static final Object agentUserManagerLock = new Object();
    private static final Object chatManagerLock = new Object();

    private static final Object serverEngineLock = new Object();

    public static UsernameManager getUsernameManager(ServletContext servletContext) {

        synchronized (usernameManagerLock) {
            if (servletContext.getAttribute(USERNAME_MANAGER_ATTRIBUTE_NAME) == null) {
                servletContext.setAttribute(USERNAME_MANAGER_ATTRIBUTE_NAME, new UsernameManager());
            }
        }
        return (UsernameManager) servletContext.getAttribute(USERNAME_MANAGER_ATTRIBUTE_NAME);
    }

    public static UBoatUserManager getUBoatUserManager(ServletContext servletContext) {

        synchronized (uboatUserManagerLock) {
            if (servletContext.getAttribute(UBOAT_USER_MANAGER_ATTRIBUTE_NAME) == null) {
                servletContext.setAttribute(UBOAT_USER_MANAGER_ATTRIBUTE_NAME, new UBoatUserManager());
            }
        }
        return (UBoatUserManager) servletContext.getAttribute(UBOAT_USER_MANAGER_ATTRIBUTE_NAME);
    }


    public static UserManager getAlliesUserManager(ServletContext servletContext) {

        synchronized (alliesUserManagerLock) {
            if (servletContext.getAttribute(ALLIES_USER_MANAGER_ATTRIBUTE_NAME) == null) {
                servletContext.setAttribute(ALLIES_USER_MANAGER_ATTRIBUTE_NAME, new UserManager());
            }
        }
        return (UserManager) servletContext.getAttribute(ALLIES_USER_MANAGER_ATTRIBUTE_NAME);
    }

    public static AgentUserManager getAgentUserManager(ServletContext servletContext) {

        synchronized (agentUserManagerLock) {
            if (servletContext.getAttribute(AGENT_USER_MANAGER_ATTRIBUTE_NAME) == null) {
                servletContext.setAttribute(AGENT_USER_MANAGER_ATTRIBUTE_NAME, new AgentUserManager());
            }
        }
        return (AgentUserManager) servletContext.getAttribute(AGENT_USER_MANAGER_ATTRIBUTE_NAME);
    }

    public static ChatManager getChatManager(ServletContext servletContext) {
        synchronized (chatManagerLock) {
            if (servletContext.getAttribute(CHAT_MANAGER_ATTRIBUTE_NAME) == null) {
                servletContext.setAttribute(CHAT_MANAGER_ATTRIBUTE_NAME, new ChatManager());
            }
        }
        return (ChatManager) servletContext.getAttribute(CHAT_MANAGER_ATTRIBUTE_NAME);
    }

    public static ServerEngine getServerEngine(ServletContext servletContext){
        synchronized (serverEngineLock) {
            if (servletContext.getAttribute(SERVER_ENGINE_ATTRIBUTE_NAME) == null) {
                servletContext.setAttribute(SERVER_ENGINE_ATTRIBUTE_NAME, new ServerEngine());
            }
        }
        return (ServerEngine) servletContext.getAttribute(SERVER_ENGINE_ATTRIBUTE_NAME);
    }

    public static int getIntParameter(HttpServletRequest request, String name) {
        String value = request.getParameter(name);
        if (value != null) {
            try {
                return Integer.parseInt(value);
            }
            catch (NumberFormatException numberFormatException) {
                numberFormatException.printStackTrace();
            }
        }
        return INT_PARAMETER_ERROR;
    }
}

