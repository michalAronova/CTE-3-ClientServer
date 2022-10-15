package battleField.servlets.agentServlets;

import battleField.utils.ServletUtils;
import battleField.utils.SessionUtils;
import engine.entity.Agent;
import engine.entity.Allies;
import engine.users.AgentUserManager;
import engine.users.UserManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "IsAssignedToContestServlet", urlPatterns = {"/agent/is-assigned-to-contest"})
public class IsAssignedToContestServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/plain;charset=UTF-8");

        //get agent from ses
//        String usernameFromSession = SessionUtils.getUsername(request);
//        AgentUserManager agentUserManager = ServletUtils.getAgentUserManager(getServletContext());
//        Agent agent = agentUserManager.
//        synchronized (this){
//            ally.getUBoat().updateAllyReady(usernameFromSession);
//        }
    }
}
