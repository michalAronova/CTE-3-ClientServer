package battleField.servlets.uBoatServlets;

import battleField.utils.ServletUtils;
import battleField.utils.SessionUtils;
import com.google.gson.Gson;
import engine.entity.UBoat;
import engine.users.UBoatUserManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

@WebServlet(name = "ContestStartedServlet", urlPatterns = {"/uboat/contest/start"})
public class ContestStartedServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");

        //get the uBoat entity from session
        String usernameFromSession = SessionUtils.getUsername(request);
        UBoatUserManager uBoatUserManager = ServletUtils.getUBoatUserManager(getServletContext());
        UBoat uBoat = (UBoat) uBoatUserManager.getEntityObject(usernameFromSession);
        synchronized (this) {
            try(PrintWriter out = response.getWriter()) {
                out.print(new Gson().toJson(uBoat.isCompetitionOn().getValue()));
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