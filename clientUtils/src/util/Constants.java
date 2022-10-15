package util;

//import com.google.gson.Gson;

import com.google.gson.Gson;

public class Constants {

    //this is a change 14/10/22

    // global constants
    public final static String LINE_SEPARATOR = System.getProperty("line.separator");
    public final static String JOHN_DOE = "<Anonymous>";
    public final static int REFRESH_RATE = 2000;
    public final static String CHAT_LINE_FORMATTING = "%tH:%tM:%tS | %.10s: %s%n";

//    public final static String UBOAT_LOGIN_FXML_RESOURCE_LOCATION = "/uBoatClient/uBoatLogin/uBoatLogin.fxml";
//    public final static String ALLIES_LOGIN_FXML_RESOURCE_LOCATION = "/alliesClient/alliesLogin/alliesLogin.fxml";
//    public final static String AGENT_LOGIN_FXML_RESOURCE_LOCATION = "/agentClient/agentLogin/agentLogin.fxml";

    // fxml locations
    public final static String UBOAT_MAIN_FXML_RESOURCE_LOCATION = "/uBoatClient/uBoatMain/uBoatMain.fxml";
    public final static String ALLIES_MAIN_FXML_RESOURCE_LOCATION = "/alliesClient/alliesMain/alliesMain.fxml";
    public final static String AGENT_MAIN_FXML_RESOURCE_LOCATION = "/agentClient/agentMain/agentMain.fxml";

    // Server resources locations
    public final static String BASE_DOMAIN = "localhost";
    private final static String BASE_URL = "http://" + BASE_DOMAIN + ":8080";
    private final static String CONTEXT_PATH = "/battlefield";
    private final static String FULL_SERVER_PATH = BASE_URL + CONTEXT_PATH;

    public final static String UBOAT_LOGIN_PAGE = FULL_SERVER_PATH + "/signup/uboat/login";
    public final static String ALLIES_LOGIN_PAGE = FULL_SERVER_PATH + "/signup/allies/login";

    public final static String USERS_LIST = FULL_SERVER_PATH + "/userslist";
    public final static String LOGOUT = FULL_SERVER_PATH + "/logout";
    public final static String UPLOAD_XML = FULL_SERVER_PATH + "/signup/uboat/upload-file";
    public final static String SEND_CHAT_LINE = FULL_SERVER_PATH + "/pages/chatroom/sendChat";
    public final static String CHAT_LINES_LIST = FULL_SERVER_PATH + "/chat";
    public final static String CONTEST_STARTED = FULL_SERVER_PATH + "/uBoat/ContestStartedServlet";


    // GSON instance
    public final static Gson GSON_INSTANCE = new Gson();
}
