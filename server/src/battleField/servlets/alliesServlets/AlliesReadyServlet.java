package battleField.servlets.alliesServlets;

import battleField.utils.ServletUtils;
import battleField.utils.SessionUtils;
import engine.entity.Allies;
import engine.users.UserManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class AlliesReadyServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse resp) throws ServletException, IOException {
        //get ally from ses
        UserManager alliesUserManager = ServletUtils.getAlliesUserManager(getServletContext());
        String usernameFromSession = SessionUtils.getUsername(request);
        Allies ally  = (Allies) alliesUserManager.getEntityObject(usernameFromSession);
        synchronized (this){
            ally.getUBoat().updateAllyReady(usernameFromSession);
        }
    }
}
