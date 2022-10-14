package uBoatClient.Utils;

public class Constants {

    public final static String BASE_DOMAIN = "localhost";
    private final static String BASE_URL = "http://" + BASE_DOMAIN + ":8080";
    private final static String CONTEXT_PATH = "/battleField";
    private final static String FULL_SERVER_PATH = BASE_URL + CONTEXT_PATH;
    private final static String CONTEST_STARTED = FULL_SERVER_PATH + "/uBoat/ContestStartedServlet";
}
