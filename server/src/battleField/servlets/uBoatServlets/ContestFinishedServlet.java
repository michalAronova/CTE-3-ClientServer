package battleField.servlets.uBoatServlets;

import DTO.contestStatus.ContestStatus;
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

@WebServlet(name = "ContestFinishedServlet", urlPatterns = {"/uboat/contest/finish"})
public class ContestFinishedServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");

        //get the uBoat entity from session
        String usernameFromSession = SessionUtils.getUsername(req);
        UBoatUserManager uBoatUserManager = ServletUtils.getUBoatUserManager(getServletContext());
        UBoat uBoat = (UBoat) uBoatUserManager.getEntityObject(usernameFromSession);
        resp.setStatus(HttpServletResponse.SC_OK);
        synchronized (this) {
            try(PrintWriter out = resp.getWriter()) {
                ContestStatus status = new ContestStatus(uBoat.isCompetitionOn().getValue(), uBoat.getWinner());
                out.print(new Gson().toJson(status));
            }
        }
    }
}
