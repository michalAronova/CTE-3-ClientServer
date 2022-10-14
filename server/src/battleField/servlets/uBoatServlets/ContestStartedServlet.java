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
import java.util.Map;

@WebServlet(name = "ContestStartedServlet", urlPatterns = {"/uBoat/ContestStartedServlet"})
public class ContestStartedServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        //get the uBoat entity from session
        String usernameFromSession = SessionUtils.getUsername(request);
        UBoatUserManager uBoatUserManager = ServletUtils.getUBoatUserManager(getServletContext());
        UBoat uBoat = (UBoat) uBoatUserManager.getEntityObject(usernameFromSession);
        response.getOutputStream().print(uBoat.getUsername());
        synchronized (this) {
            if (countReadyAllies(uBoat) == uBoat.getAlliesRequired()) {
                uBoat.updateCompetitionStart();
            }
        }
    }

    private int countReadyAllies(UBoat uBoat) {
        int count = 0;
        for (boolean isReady : uBoat.getName2ready().values()) {
            if (isReady) { count++; }
        }
        return count;
    }
}