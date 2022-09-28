package battleField.servlets;

import battleField.constants.Constants;
import battleField.utils.ServletUtils;
import battleField.utils.SessionUtils;
import engine.entity.EntityEnum;
import engine.users.UserManager;
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

        UserManager userManager = getUserManagerByEntity(entityFromSession);

        if (usernameFromSession != null) { //user is logged in
            synchronized (this) {
                userManager.removeUser(usernameFromSession);
                SessionUtils.clearSession(request);
            }
        }
    }

    private UserManager getUserManagerByEntity(EntityEnum entityFromSession) {
        switch (entityFromSession){
            case UBOAT:
                return ServletUtils.getUBoatUserManager(getServletContext());
            case ALLIES:
                return ServletUtils.getAlliesUserManager(getServletContext());
            case AGENT:
                return ServletUtils.getAgentUserManager(getServletContext());
        }
        return null;
    }
}
