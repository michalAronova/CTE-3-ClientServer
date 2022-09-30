package battleField.utils;

import battleField.servlets.chat.ChatManager;
import engine.users.UserManager;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;

import static battleField.constants.Constants.INT_PARAMETER_ERROR;

public class ServletUtils {

    private static final String UBOAT_USER_MANAGER_ATTRIBUTE_NAME = "uBoatUserManager";
    private static final String ALLIES_USER_MANAGER_ATTRIBUTE_NAME = "alliesUserManager";
    private static final String AGENT_USER_MANAGER_ATTRIBUTE_NAME = "agentUserManager";
    private static final String CHAT_MANAGER_ATTRIBUTE_NAME = "chatManager";

    /*
    Note how the synchronization is done only on the question and\or creation of the relevant managers and once they exists -
    the actual fetch of them is remained un-synchronized for performance POV
     */
    private static final Object uboatUserManagerLock = new Object();
    private static final Object alliesUserManagerLock = new Object();
    private static final Object agentUserManagerLock = new Object();
    private static final Object chatManagerLock = new Object();

    public static UserManager getUBoatUserManager(ServletContext servletContext) {

        synchronized (uboatUserManagerLock) {
            if (servletContext.getAttribute(UBOAT_USER_MANAGER_ATTRIBUTE_NAME) == null) {
                servletContext.setAttribute(UBOAT_USER_MANAGER_ATTRIBUTE_NAME, new UserManager());
            }
        }
        return (UserManager) servletContext.getAttribute(UBOAT_USER_MANAGER_ATTRIBUTE_NAME);
    }

    public static UserManager getAlliesUserManager(ServletContext servletContext) {

        synchronized (alliesUserManagerLock) {
            if (servletContext.getAttribute(ALLIES_USER_MANAGER_ATTRIBUTE_NAME) == null) {
                servletContext.setAttribute(ALLIES_USER_MANAGER_ATTRIBUTE_NAME, new UserManager());
            }
        }
        return (UserManager) servletContext.getAttribute(ALLIES_USER_MANAGER_ATTRIBUTE_NAME);
    }

    public static UserManager getAgentUserManager(ServletContext servletContext) {

        synchronized (agentUserManagerLock) {
            if (servletContext.getAttribute(AGENT_USER_MANAGER_ATTRIBUTE_NAME) == null) {
                servletContext.setAttribute(AGENT_USER_MANAGER_ATTRIBUTE_NAME, new UserManager());
            }
        }
        return (UserManager) servletContext.getAttribute(AGENT_USER_MANAGER_ATTRIBUTE_NAME);
    }

    public static ChatManager getChatManager(ServletContext servletContext) {
        synchronized (chatManagerLock) {
            if (servletContext.getAttribute(CHAT_MANAGER_ATTRIBUTE_NAME) == null) {
                servletContext.setAttribute(CHAT_MANAGER_ATTRIBUTE_NAME, new ChatManager());
            }
        }
        return (ChatManager) servletContext.getAttribute(CHAT_MANAGER_ATTRIBUTE_NAME);
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

