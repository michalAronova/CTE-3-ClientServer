package battleField.client.util;

//import com.google.gson.Gson;

public class Constants {

    // global constants
    public final static String LINE_SEPARATOR = System.getProperty("line.separator");
    public final static String JOHN_DOE = "<Anonymous>";
    public final static int REFRESH_RATE = 2000;
    public final static String CHAT_LINE_FORMATTING = "%tH:%tM:%tS | %.10s: %s%n";

    // fxml locations
    public final static String MAIN_PAGE_FXML_RESOURCE_LOCATION = "/battleField/client/component/global/chat-app-main.fxml";

    public final static String UBOAT_LOGIN_FXML_RESOURCE_LOCATION = "/battleField/client/component/uBoat/uBoatLogin/UBoatLogin.fxml";
    public final static String ALLIES_LOGIN_FXML_RESOURCE_LOCATION = "/battleField/client/component/allies/alliesLogin/alliesLogin.fxml";
    public final static String AGENT_LOGIN_FXML_RESOURCE_LOCATION = "/battleField/client/component/agent/agentLogin/agentLogin.fxml";

    public final static String UBOAT_MAIN_FXML_RESOURCE_LOCATION = "/battleField/client/component/uBoat/uBoatLogin/UBoatMain.fxml";
    public final static String ALLIES_MAIN_FXML_RESOURCE_LOCATION = "/battleField/client/component/allies/alliesLogin/alliesMain.fxml";
    public final static String AGENT_MAIN_FXML_RESOURCE_LOCATION = "/battleField/client/component/agent/agentLogin/agentMain.fxml";

    //public final static String LOGIN_PAGE_FXML_RESOURCE_LOCATION = "/chat/client/component/login/login.fxml";
    //public final static String CHAT_ROOM_FXML_RESOURCE_LOCATION = "/chat/client/component/chatroom/chat-room-main.fxml";

    // Server resources locations
    public final static String BASE_DOMAIN = "localhost";
    private final static String BASE_URL = "http://" + BASE_DOMAIN + ":8080";
    private final static String CONTEXT_PATH = "/battlefield";
    private final static String FULL_SERVER_PATH = BASE_URL + CONTEXT_PATH;

    public final static String UBOAT_LOGIN_PAGE = FULL_SERVER_PATH + "/signup/uboat/login";
    public final static String USERS_LIST = FULL_SERVER_PATH + "/userslist";
    public final static String LOGOUT = FULL_SERVER_PATH + "/logout";
    public final static String UPLOAD_XML = FULL_SERVER_PATH + "/signup/uboat/upload-file";
    public final static String SEND_CHAT_LINE = FULL_SERVER_PATH + "/pages/chatroom/sendChat";
    public final static String CHAT_LINES_LIST = FULL_SERVER_PATH + "/chat";

    // GSON instance
    //public final static Gson GSON_INSTANCE = new Gson();
}
