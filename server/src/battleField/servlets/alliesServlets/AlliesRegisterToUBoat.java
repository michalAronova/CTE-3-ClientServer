package battleField.servlets.alliesServlets;

import battleField.utils.ServletUtils;
import battleField.utils.SessionUtils;
import engine.entity.Allies;
import engine.entity.Entity;
import engine.entity.UBoat;
import engine.users.UserManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import static battleField.constants.Constants.DESIRED_UBOAT;
import static battleField.constants.Constants.USERNAME;

@WebServlet(name = "AlliesRegisterToUBoat", urlPatterns = {"/allies/registerToUBoat"})
public class AlliesRegisterToUBoat extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        PrintWriter out = response.getWriter();
        UserManager alliesUserManager = ServletUtils.getAlliesUserManager(getServletContext());
        out.println(SessionUtils.getEntity(request));

        //get the ally entity from session
        String usernameFromSession = SessionUtils.getUsername(request);
        Allies ally  = (Allies) alliesUserManager.getEntityObject(usernameFromSession);

        //get uBoat name from parameter
        String uBoatName = request.getParameter(DESIRED_UBOAT);
        if (uBoatName == null || uBoatName.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_CONFLICT);
        } else {
            synchronized (this) {
                uBoatName = uBoatName.trim();
                UserManager uBoatUserManager = ServletUtils.getUBoatUserManager(getServletContext());
                if (uBoatUserManager.isUserExists(uBoatName)) {
                    UBoat uBoat = (UBoat) uBoatUserManager.getEntityObject(uBoatName);
                    uBoat.addParticipant(ally);
                    ally.setUBoat(uBoat);
                    out.println(ally.getUsername() + " registered to " + uBoat.getUsername());
                    response.setStatus(HttpServletResponse.SC_OK);
                } else {
                    out.println("no such uBoat exists");
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                }
            }
        }
    }
}

