package battleField.servlets.uBoatServlets;

import battleField.constants.Constants;
import battleField.utils.ServletUtils;
import battleField.utils.SessionUtils;
import engine.entity.Allies;
import engine.entity.EntityEnum;
import engine.entity.UBoat;
import engine.users.UBoatUserManager;
import engine.users.UserManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "UBoatReadyServlet", urlPatterns = {"/uboat/uBoat-ready"})
public class UBoatReadyServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //get ally from ses
        UBoatUserManager uBoatUserManager = ServletUtils.getUBoatUserManager(getServletContext());
        String usernameFromSession = SessionUtils.getUsername(request);
        UBoat uBoat  = (UBoat) uBoatUserManager.getEntityObject(usernameFromSession);
        synchronized (this) {
            if (uBoatUserManager.isReady(usernameFromSession)) {
                String errorMessage = "Username " + usernameFromSession + " already ready..";
                // stands for unauthorized as there is already such user with this name
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getOutputStream().print(errorMessage);
                System.out.println(errorMessage);
            } else {
                uBoatUserManager.addReadyUBoat(usernameFromSession);
            }
        }
    }
}
