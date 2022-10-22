package battleField.servlets.alliesServlets;

import battleField.utils.ServletUtils;
import battleField.utils.SessionUtils;
import engine.entity.Allies;
import engine.users.UserManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import static jakarta.servlet.http.HttpServletResponse.SC_OK;

@WebServlet(name = "AlliesOKClicked", urlPatterns = {"/allies/ok/clicked"})
public class AlliesOKClicked extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //get ally from ses
        response.setContentType("text/plain;charset=UTF-8");

        UserManager alliesUserManager = ServletUtils.getAlliesUserManager(getServletContext());
        String usernameFromSession = SessionUtils.getUsername(request);

        Allies ally = (Allies) alliesUserManager.getEntityObject(usernameFromSession);
        synchronized (this){
            ally.setOKClicked(true);
        }
        response.setStatus(SC_OK);
    }
}
