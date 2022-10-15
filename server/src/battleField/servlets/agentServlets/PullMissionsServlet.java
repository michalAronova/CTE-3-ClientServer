package battleField.servlets.agentServlets;

import battleField.constants.Constants;
import battleField.utils.ServletUtils;
import battleField.utils.SessionUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import engine.decipherManager.mission.Mission;
import engine.entity.Allies;
import engine.entity.Entity;
import engine.users.AgentUserManager;
import engine.users.UserManager;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import static battleField.constants.Constants.*;

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

        int missionsDoneByAgent = ServletUtils.getIntParameter(req, MISSIONS_DONE_BY_AGENT);
        if (missionsDoneByAgent == Constants.INT_PARAMETER_ERROR) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        //add above integer to the missions done count of its ally

        try {
            List<Mission> missions = myAllies.pullMissions
                                                (agentUserManager
                                                    .getSimpleAgent(usernameFromSession)
                                                    .getDTO()
                                                    .getMissionPull());
            try (PrintWriter out = resp.getWriter()) {
                Gson gson = new Gson();
                Type missionListType = new TypeToken<List<Mission>>() { }.getType();
                String missionsToJson = gson.toJson(missions, missionListType);
                out.println(missionsToJson);
                resp.setStatus(HttpServletResponse.SC_OK);
            }

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
