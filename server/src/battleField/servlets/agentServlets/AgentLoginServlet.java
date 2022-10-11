package battleField.servlets.agentServlets;

import DTO.agent.SimpleAgentDTO;
import battleField.constants.Constants;
import battleField.utils.ServletUtils;
import battleField.utils.SessionUtils;
import engine.entity.Agent;
import engine.entity.Allies;
import engine.entity.EntityEnum;
import engine.entity.SimpleAgent;
import engine.users.AgentUserManager;
import engine.users.UserManager;
import engine.users.UsernameManager;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import static battleField.constants.Constants.*;

@WebServlet(name = "AgentLoginServlet", urlPatterns = {"/signup/agent/login"})
public class AgentLoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain;charset=UTF-8");

        String usernameFromSession = SessionUtils.getUsername(request);
        AgentUserManager agentUserManager = ServletUtils.getAgentUserManager(getServletContext());
        UsernameManager usernameManager = ServletUtils.getUsernameManager(getServletContext());

        if (usernameFromSession == null) { //user is not logged in yet

            String usernameFromParameter = request.getParameter(USERNAME);
            if (usernameFromParameter == null || usernameFromParameter.isEmpty()) {
                //no username in session and no username in parameter - not standard situation. it's a conflict

                // stands for conflict in server state
                response.setStatus(HttpServletResponse.SC_CONFLICT);
            } else {
                //normalize the username value
                usernameFromParameter = usernameFromParameter.trim();
                synchronized (this) {
                    if (usernameManager.isUserExists(usernameFromParameter)) {
                        String errorMessage = "Username " + usernameFromParameter + " already exists. Please enter a different username.";

                        // stands for unauthorized as there is already such user with this name
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        response.getOutputStream().print(errorMessage);
                    }
                    else {
                        //add the new user to the users list
                        usernameManager.addUser(usernameFromParameter);

                        //adding SimpleAgentDTO to allies with manager
                        String alliesUsername = request.getParameter(ALLIES_JOINED);
                        Allies allies = (Allies) ServletUtils.getAlliesUserManager(getServletContext())
                                                                .getEntityObject(alliesUsername);
                        int threadCount = Integer.parseInt(request.getParameter(THREAD_COUNT));
                        int missionAmountPull = Integer.parseInt(request.getParameter(MISSION_AMOUNT_PULL));
                        SimpleAgentDTO agentDTO = new SimpleAgentDTO(usernameFromParameter, threadCount, missionAmountPull);
                        allies.addAgentData(agentDTO);

                        //adding agent to agents manager
                        agentUserManager.addUser(usernameFromParameter, new SimpleAgent(alliesUsername, agentDTO));

                        request.getSession(true).setAttribute(Constants.USERNAME, usernameFromParameter);
                        request.getSession().setAttribute(Constants.ENTITY, EntityEnum.AGENT);

                        response.setStatus(HttpServletResponse.SC_OK);

                        response.getOutputStream()
                                .print(String.format("logged in as %s (%s)",
                                        usernameFromParameter, EntityEnum.AGENT));
                    }
                }
            }
        } else {
            //user is already logged in
            response.setStatus(HttpServletResponse.SC_OK);
        }
    }

}