package battleField.servlets;

import battleField.constants.Constants;
import battleField.utils.ServletUtils;
import battleField.utils.SessionUtils;
import engine.entity.Allies;
import engine.entity.Entity;
import engine.entity.EntityEnum;
import engine.entity.UBoat;
import engine.users.UserManager;
import engine.users.UsernameManager;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import static battleField.constants.Constants.USERNAME;

@WebServlet(name = "LogoutServlet", urlPatterns = {"/logout"})
public class LogoutServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain;charset=UTF-8");

        String usernameFromSession = SessionUtils.getUsername(request);
        EntityEnum entityFromSession = SessionUtils.getEntity(request);

        UsernameManager usernameManager = ServletUtils.getUsernameManager(getServletContext());

        if (usernameFromSession != null) { //user is logged in
            synchronized (this) {
                usernameManager.removeUser(usernameFromSession);
                removeUserByEntity(usernameFromSession, entityFromSession);
                SessionUtils.clearSession(request);
            }
        }
    }

    private void removeUserByEntity(String usernameFromSession, EntityEnum entityFromSession) {
        switch (entityFromSession){
            case UBOAT:
                ServletUtils.getUBoatUserManager(getServletContext()).removeUser(usernameFromSession);
            case ALLIES:
                UBoat uboat = (UBoat) ServletUtils.getUBoatUserManager(getServletContext()).getUser(usernameFromSession);
                uboat.removeParticipant(usernameFromSession);
                ServletUtils.getAlliesUserManager(getServletContext()).removeUser(usernameFromSession);
            case AGENT:
                String ally = ServletUtils.getAgentUserManager(getServletContext()).getAllyName(usernameFromSession);
                ((Allies)ServletUtils.getAlliesUserManager(getServletContext()).getUser(ally)).removeAgent(usernameFromSession);
                ServletUtils.getAgentUserManager(getServletContext()).removeUser(usernameFromSession);
        }
    }
}
