package battleField.servlets.agentServlets;

import DTO.mission.MissionDTO;
import battleField.utils.ServletUtils;
import battleField.utils.SessionUtils;
import com.google.gson.Gson;
import engine.entity.Allies;
import engine.users.AgentUserManager;
import engine.users.UserManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "PullMissionsServlet", urlPatterns = {"/agent/pull"})
public class PullMissionsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/plain;charset=UTF-8");

        String usernameFromSession = SessionUtils.getUsername(req);
        AgentUserManager agentUserManager = ServletUtils.getAgentUserManager(getServletContext());
        UserManager alliesUserManager = ServletUtils.getAlliesUserManager(getServletContext());

        Allies myAllies = (Allies) alliesUserManager.getEntityObject
                                    (agentUserManager.getAllyName(usernameFromSession));

        List<MissionDTO> missions = myAllies.pullMissions
                (agentUserManager
                        .getSimpleAgent(usernameFromSession)
                        .getDTO()
                        .getMissionPull());

        try (PrintWriter out = resp.getWriter()) {
            Gson gson = new Gson();
            String missionsToJson = gson.toJson(missions);
            out.println(missionsToJson);
            resp.setStatus(HttpServletResponse.SC_OK);
        }
    }
}
