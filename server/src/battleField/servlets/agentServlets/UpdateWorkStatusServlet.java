package battleField.servlets.agentServlets;

import DTO.agent.WorkStatusDTO;
import parameters.ConstantParams;
import battleField.utils.ServletUtils;
import battleField.utils.SessionUtils;
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

import static parameters.ConstantParams.*;

@WebServlet(name = "UpdateWorkStatusServlet", urlPatterns = {"/agent/work-status"})
public class UpdateWorkStatusServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //get agent from ses
        resp.setContentType("text/plain;charset=UTF-8");

        String usernameFromSession = SessionUtils.getUsername(req);
        AgentUserManager agentUserManager = ServletUtils.getAgentUserManager(getServletContext());

        PrintWriter out = resp.getWriter();
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

        int candidatesProduced = ServletUtils.getIntParameter(req, CANDIDATES_PRODUCED);
        int missionsLeft = ServletUtils.getIntParameter(req, MISSIONS_LEFT);
        int missionsDoneByAgent = ServletUtils.getIntParameter(req, MISSIONS_DONE_BY_AGENT);
        if (candidatesProduced == ConstantParams.INT_PARAMETER_ERROR ||
                missionsLeft == ConstantParams.INT_PARAMETER_ERROR ||
                missionsDoneByAgent == ConstantParams.INT_PARAMETER_ERROR) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.println("Wrong parameter type inserted");
            return;
        }

        UserManager alliesUserManager = ServletUtils.getAlliesUserManager(getServletContext());
        Allies myAllies = (Allies) alliesUserManager.getEntityObject
                (agentUserManager.getAllyName(usernameFromSession));
        WorkStatusDTO workStatus = new WorkStatusDTO (candidatesProduced, missionsDoneByAgent, missionsLeft);
        myAllies.updateAgentWorkStatus(usernameFromSession, workStatus);
        resp.setStatus(HttpServletResponse.SC_OK);
    }
}
