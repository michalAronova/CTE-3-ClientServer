package battleField.servlets.uBoatServlets;

import DTO.missionResult.AlliesCandidates;
import battleField.utils.ServletUtils;
import battleField.utils.SessionUtils;
import com.google.gson.Gson;
import engine.entity.Entity;
import engine.entity.UBoat;
import engine.users.UserManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.Set;

public class CandidatesListServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        try (PrintWriter out = response.getWriter()) {
            Gson gson = new Gson();
            //get uboat from ses
            String usernameFromSession = SessionUtils.getUsername(request);
            UserManager userManager = ServletUtils.getUBoatUserManager(getServletContext());
            UBoat uBoat = (UBoat) userManager.getEntityObject(usernameFromSession);

            //parse candidates list
            String json = gson.toJson(uBoat.getCandidates());

            //put in body
            out.println(json);
            out.flush();
        }
    }
}
