package battleField.servlets.agentServlets;

import DTO.contest.Contest;
import battleField.utils.ServletUtils;
import battleField.utils.SessionUtils;
import com.google.gson.Gson;
import engine.entity.Allies;
import engine.entity.EntityEnum;
import engine.entity.UBoat;
import engine.users.AgentUserManager;
import engine.users.UserManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "IsRegisterToUBoatServlet", urlPatterns = {"/agent/check-uboat-registered"})
public class IsRegisterToUBoatServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();

        //get agent from ses
        String usernameFromSession = SessionUtils.getUsername(req);
        AgentUserManager agentUserManager = ServletUtils.getAgentUserManager(getServletContext());

        //validation
        if(usernameFromSession == null){
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            out.println("You are not logged in...");
            return;
        }
        if(!SessionUtils.getEntity(req).equals(EntityEnum.AGENT)){
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            out.println("You are not an agent!");
            return;
        }

        //get agent's ally
        UserManager alliesUserManager = ServletUtils.getAlliesUserManager(getServletContext());
        Allies myAllies = (Allies) alliesUserManager.getEntityObject
                (agentUserManager.getAllyName(usernameFromSession));
        //if agent's ally not registered to any uboat yet.. sent nothing on response
        if(myAllies.getUBoat() == null){
            return;
        }
        //yay my ally registered to competition! return contest details on body
        else {
            UBoat boat = myAllies.getUBoat();
            Contest contest = new Contest(boat.getBattleFieldName(), boat.getUsername(),
                                            myAllies.isCompetitionOnProperty().getValue(),
                                            myAllies.getDM().getDifficulty().name(),
                                            boat.getAlliesRequired(), boat.getParticipants().size());
            Gson gson = new Gson();
            String json = gson.toJson(contest);
            resp.setStatus(HttpServletResponse.SC_OK);
            out.println(json);
            out.flush();
        }
    }
}