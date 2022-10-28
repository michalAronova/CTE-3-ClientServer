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
@WebServlet(name = "UBoatUnReadyServlet", urlPatterns = {"/uboat/un-ready"})
public class UBoatUnReady extends HttpServlet{
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //get uboat from ses
        UBoatUserManager uBoatUserManager = ServletUtils.getUBoatUserManager(getServletContext());
        String usernameFromSession = SessionUtils.getUsername(request);

        synchronized (this) {
            try(PrintWriter out = response.getWriter()) {
                if (!uBoatUserManager.isReady(usernameFromSession)) {
                    String errorMessage = "Username " + usernameFromSession + " already NOT ready..";
                    // stands for unauthorized as there is already such user with this name
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    out.print(errorMessage);
                    System.out.println(errorMessage);
                } else {
                    response.setStatus(HttpServletResponse.SC_OK);
                    uBoatUserManager.unReadyUBoat(usernameFromSession);
                    ((UBoat)uBoatUserManager.getUser(usernameFromSession)).removeAllParticipants();
                    out.print("UBoat "+ usernameFromSession+" is NOT ready!");
                }
            }
        }
    }
}