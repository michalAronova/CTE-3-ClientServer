package battleField.servlets.alliesServlets;

import DTO.contest.Contest;
import battleField.utils.ServletUtils;
import battleField.utils.SessionUtils;
import com.google.gson.Gson;
import engine.entity.Allies;
import engine.entity.UBoat;
import engine.users.UserManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

import static jakarta.servlet.http.HttpServletResponse.SC_OK;
import static parameters.ConstantParams.DESIRED_UBOAT;

@WebServlet(name = "AlliesRegisterToUBoat", urlPatterns = {"/allies/register-to-uboat"})
public class AlliesRegisterToUBoat extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");

        PrintWriter out = response.getWriter();
        UserManager alliesUserManager = ServletUtils.getAlliesUserManager(getServletContext());

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
                    if(uBoat.isFull()) {
                        out.println("can't register to a full contest");
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    }
                    else if(uBoat.isCompetitionOn().getValue()){
                        out.println("can't register to a running contest");
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    }
                    else {
                        uBoat.addParticipant(ally);
                        ally.setUBoat(uBoat);
                        Gson gson = new Gson();
                        Contest contest = uBoat.getAsDTO();
                        System.out.println("chosen contest" + contest);
                        String json = gson.toJson(contest);
                        //put in body
                        response.setStatus(SC_OK);
                        out.println(json);
                        out.flush();
                    }
                } else {
                    out.println("no such uBoat exists");
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                }
            }
        }
    }
}

