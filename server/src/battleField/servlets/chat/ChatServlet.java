package battleField.servlets.chat;

import battleField.utils.ServletUtils;
import battleField.utils.SessionUtils;
import com.google.gson.Gson;
import engine.chat.ChatManager;
import engine.chat.SingleChatEntry;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import static parameters.ConstantParams.CHAT_VERSION_PARAMETER;
import static parameters.ConstantParams.INT_PARAMETER_ERROR;

@WebServlet(name = "ChatServlet", urlPatterns = {"/chat"})
public class ChatServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        
        response.setContentType("application/json");
        ChatManager chatManager = ServletUtils.getChatManager(getServletContext());
        String username = SessionUtils.getUsername(request);
        if (username == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }

        /*
        verify chat version given from the user is a valid number. if not it is considered an error and nothing is returned back
        Obviously the UI should be ready for such a case and handle it properly
         */
        int chatVersion = ServletUtils.getIntParameter(request, CHAT_VERSION_PARAMETER);
        if (chatVersion == INT_PARAMETER_ERROR) {
            return;
        }

        /*
        Synchronizing as minimum as I can to fetch only the relevant information from the chat manager and then only processing and sending this information onward
        Note that the synchronization here is on the ServletContext, and the one that also synchronized on it is the chat servlet when adding new chat lines.
         */
        int chatManagerVersion = 0;
        List<SingleChatEntry> chatEntries;
        synchronized (getServletContext()) {
            chatManagerVersion = chatManager.getVersion();
            chatEntries = chatManager.getChatEntries(chatVersion);
        }

        // log and create the response json string
        ChatAndVersion cav = new ChatAndVersion(chatEntries, chatManagerVersion);
        Gson gson = new Gson();
        String jsonResponse = gson.toJson(cav);

        try (PrintWriter out = response.getWriter()) {
            out.print(jsonResponse);
            out.flush();
        }
    }
    
    private static class ChatAndVersion {

        final private List<SingleChatEntry> entries;
        final private int version;

        public ChatAndVersion(List<SingleChatEntry> entries, int version) {
            this.entries = entries;
            this.version = version;
        }
    }
}