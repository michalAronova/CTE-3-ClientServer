package battleField.servlets.agentServlets;

import DTO.contestStatus.ContestStatus;
import battleField.utils.ServletUtils;
import battleField.utils.SessionUtils;
import com.google.gson.Gson;
import engine.entity.Allies;
import engine.entity.EntityEnum;
import engine.users.AgentUserManager;
import engine.users.UserManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "IsContestFinishedServlet", urlPatterns = {"/agent/contest/finished"})
public class IsContestFinishedServlet extends HttpServlet {
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
        Allies myAllies = (Allies) alliesUserManager
                .getEntityObject(agentUserManager.getAllyName(usernameFromSession));

        synchronized (this) {
            ContestStatus status = new ContestStatus(myAllies.getUBoat().isCompetitionOn().getValue(),
                                                        myAllies.getUBoat().getWinner());
            out.print(new Gson().toJson(status));
        }
    }
}
