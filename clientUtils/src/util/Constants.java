package util;

import com.google.gson.Gson;

public class Constants {

    // global constants
    public final static String LINE_SEPARATOR = System.getProperty("line.separator");
    public final static String JOHN_DOE = "<Anonymous>";
    public final static int REFRESH_RATE = 2000;
    public final static int SMALL_REFRESH_RATE = 500;
    public final static int MEDIUM_REFRESH_RATE = 1000;
    public final static String CHAT_LINE_FORMATTING = "%tH:%tM:%tS | %.10s: %s%n";

    // fxml locations
    public final static String UBOAT_APP_FXML_RESOURCE_LOCATION = "/uBoatClient/uBoatApp/uBoatApp.fxml";
    public final static String ALLIES_APP_FXML_RESOURCE_LOCATION = "/alliesClient/alliesApp/alliesApp.fxml";
    public final static String AGENT_APP_FXML_RESOURCE_LOCATION = "/agentClient/agentApp/agentApp.fxml";
    public final static String UBOAT_MAIN_FXML_RESOURCE_LOCATION = "/uBoatClient/uBoatMain/uBoatMain.fxml";
    public final static String ALLIES_MAIN_FXML_RESOURCE_LOCATION = "/alliesClient/alliesMain/alliesMain.fxml";
    public final static String AGENT_MAIN_FXML_RESOURCE_LOCATION = "/agentClient/agentMain/agentMain.fxml";
    public final static String CHOOSE_NAME_FXML_RESOURCE_LOCATION = "../../../clientUtils/clientUtils/chooseNameComponent/chooseNameComponent.fxml";

    // Server resources locations
    public final static String BASE_DOMAIN = "localhost";
    private final static String BASE_URL = "http://" + BASE_DOMAIN + ":8080";
    private final static String CONTEXT_PATH = "/battlefield";
    private final static String FULL_SERVER_PATH = BASE_URL + CONTEXT_PATH;

    // GSON instance
    public final static Gson GSON_INSTANCE = new Gson();

    //servlets

    //uboat
    public final static String CONTEST_STARTED = FULL_SERVER_PATH + "/uboat/contest/start";
    public final static String UPLOAD_XML = FULL_SERVER_PATH + "/uboat/upload";
    public final static String MANUAL_CONFIG = FULL_SERVER_PATH + "/uboat/code/configuration";
    public final static String PARTICIPANTS = FULL_SERVER_PATH + "/uboat/participants";
    public final static String PROCESS = FULL_SERVER_PATH +"/uboat/process";
    public final static String RANDOM_CONFIG = FULL_SERVER_PATH + "/uboat/code/configuration/random";
    public final static String CODE_RESET = FULL_SERVER_PATH + "/uboat/code/reset";
    public final static String UBOAT_CANDIDATES = FULL_SERVER_PATH + "/uboat/candidates";
    public final static String UBOAT_LIST = FULL_SERVER_PATH + "/uboat/users-list";
    public final static String UBOAT_LOGIN_PAGE = FULL_SERVER_PATH + "/signup/uboat/login";
    public final static String UBOAT_READY = FULL_SERVER_PATH + "/uboat/ready";

    //allies
    public final static String ACTIVE_CONTESTS = FULL_SERVER_PATH + "/allies/active-contests";
    public final static String ALLIES_CANDIDATES = FULL_SERVER_PATH + "/allies/candidates";
    public final static String ALLIES_LIST = FULL_SERVER_PATH +"/allies/users-list";
    public final static String ALLIES_LOGIN_PAGE = FULL_SERVER_PATH + "/signup/allies/login";
    public final static String ALLIES_READY = FULL_SERVER_PATH + "/allies/ready";
    public final static String REGISTER_TO_UBOAT = FULL_SERVER_PATH + "/allies/register-to-uboat";
    public final static String DM_PROGRESS = FULL_SERVER_PATH + "/allies/DM-progress";
    public final static String MY_AGENTS = FULL_SERVER_PATH + "/allies/my-agents";
    public final static String RIVAL_ALLIES = FULL_SERVER_PATH + "/allies/rivals";
    public final static String IS_COMPETITION_ON = FULL_SERVER_PATH + "/allies/competition-on";

    //agent
    public final static String AGENT_LOGIN_PAGE = FULL_SERVER_PATH + "/signup/agent/login";
    public final static String PULL_MISSIONS = FULL_SERVER_PATH + "/agent/pull";
    public final static String UPDATE_WORK_STATUS = FULL_SERVER_PATH + "/agent/work-status";
    public final static String UPLOAD_CANDIDATES = FULL_SERVER_PATH + "/agent/upload/candidates";
    public final static String IS_UBOAT_REGISTERED = FULL_SERVER_PATH + "/agent/check-uboat-registered";

    //chat
    public final static String USERS_LIST = FULL_SERVER_PATH + "/userslist";
    public final static String LOGOUT = FULL_SERVER_PATH + "/logout";

    public final static String SEND_CHAT_LINE = FULL_SERVER_PATH + "/pages/chatroom/sendChat";
    public final static String CHAT_LINES_LIST = FULL_SERVER_PATH + "/chat";

}
