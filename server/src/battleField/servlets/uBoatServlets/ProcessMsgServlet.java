package battleField.servlets.uBoatServlets;

import battleField.utils.ServletUtils;
import battleField.utils.SessionUtils;
import engine.entity.UBoat;
import engine.users.UBoatUserManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

import static battleField.constants.Constants.MSG_TO_PROCESS;
import static jakarta.servlet.http.HttpServletResponse.SC_OK;

@WebServlet(name = "ProcessMsgServlet", urlPatterns = {"/uboat/process"})
public class ProcessMsgServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/plain");
        PrintWriter out = response.getWriter();
        //get the uBoat entity from session
        String usernameFromSession = SessionUtils.getUsername(request);
        UBoatUserManager uBoatUserManager = ServletUtils.getUBoatUserManager(getServletContext());
        UBoat uBoat  = (UBoat) uBoatUserManager.getEntityObject(usernameFromSession);

        String message = request.getParameter(MSG_TO_PROCESS);
        if (message == null || message.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_CONFLICT);
            out.close();
            return;
        } else {
            if (!uBoat.getEngine().getDictionary().areAllWordsInDictionary(message)) {
                out.println("trying to process words not from dictionary");
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.close();
                return;
            }
            String output = uBoat.getEngine().processMsg(message);
            out.println(output);
            response.setStatus(SC_OK);
            out.close();
        }
    }
}
