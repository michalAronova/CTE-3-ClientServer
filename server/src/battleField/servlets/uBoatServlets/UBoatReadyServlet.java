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
import java.io.PrintWriter;

@WebServlet(name = "UBoatReadyServlet", urlPatterns = {"/uboat/ready"})
public class UBoatReadyServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //get ally from ses
        UBoatUserManager uBoatUserManager = ServletUtils.getUBoatUserManager(getServletContext());
        String usernameFromSession = SessionUtils.getUsername(request);

        synchronized (this) {
            try(PrintWriter out = response.getWriter()) {
                if (uBoatUserManager.isReady(usernameFromSession)) {
                    String errorMessage = "Username " + usernameFromSession + " already ready..";
                    // stands for unauthorized as there is already such user with this name
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    out.print(errorMessage);
                } else {
                    uBoatUserManager.addReadyUBoat(usernameFromSession);
                    out.print("UBoat "+ usernameFromSession+" is ready!");
                }
            }
        }
    }
}
