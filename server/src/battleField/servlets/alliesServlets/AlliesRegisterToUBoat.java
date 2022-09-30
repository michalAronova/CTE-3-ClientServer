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
        UserManager userManager = ServletUtils.getAlliesUserManager(getServletContext());

        //get the ally entity from session
        String usernameFromSession = SessionUtils.getUsername(request);
        out.println("usesrname(ally) " + usernameFromSession);
        Allies ally  = (Allies) userManager.getEntityObject(usernameFromSession);

        //get uBoat name from parameter
        String uBoatName = request.getParameter(DESIRED_UBOAT);
        out.println("desired uBoat " + uBoatName);

        if (uBoatName == null || uBoatName.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_CONFLICT);
        } else {
            synchronized (this) {
                out.println("desired uBoat name" + uBoatName);
                uBoatName = uBoatName.trim();
                if (userManager.isUserExists(uBoatName)) {
                    out.println("user exist");
                    UBoat uBoat = (UBoat) userManager.getEntityObject(uBoatName);
                    out.println("manage to hold entity " + uBoat.getUsername());
                    uBoat.addParticipant(ally);
                    Map<String,Allies> map = uBoat.getParticipants();
                    out.println(map.containsKey(ally.getUsername()));
                    response.setStatus(HttpServletResponse.SC_OK);
                } else {
                    out.println("here");
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                }
            }
        }
        //response.getOutputStream().print(uBoatName);
        out.println(uBoatName);
        //String alliesNameFromSession = SessionUtils.getUsername(request);
        //response.getOutputStream().print(alliesNameFromSession);

        //UserManager userManager = ServletUtils.getAlliesUserManager(getServletContext());
    }
}

