package battleField.servlets.agentServlets;

import battleField.utils.ServletUtils;
import battleField.utils.SessionUtils;
import com.google.gson.Gson;
import engine.decipherManager.mission.Mission;
import engine.entity.Allies;
import engine.entity.Entity;
import engine.users.UserManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import static battleField.constants.Constants.MISSION_AMOUNT_PULL;
import static battleField.constants.Constants.MY_ALLY;

@WebServlet(name = "PullMissionsServlet", urlPatterns = {"/agent/pull"})
public class PullMissionsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/plain;charset=UTF-8");

        PrintWriter out = resp.getWriter();

        String usernameFromSession = SessionUtils.getUsername(req);
        UserManager agentUserManager = ServletUtils.getAgentUserManager(getServletContext());
        UserManager alliesUserManager = ServletUtils.getAlliesUserManager(getServletContext());

        String alliesFromParameter = req.getParameter(MY_ALLY);
        String missionPullFromParameter = req.getParameter(MISSION_AMOUNT_PULL);

        int missionPullAmount;
        try{
            missionPullAmount = Integer.parseInt(missionPullFromParameter);
        }
        catch(NumberFormatException e){
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.println("Mission pull amount must be a number");
            return;
        }

        List<Runnable> missions;
        try {
            missions = ((Allies) alliesUserManager.getUser(alliesFromParameter)).pullMissions(missionPullAmount);
        } catch (InterruptedException e) {
            resp.setStatus(HttpServletResponse.SC_REQUEST_TIMEOUT);
            out.println("Pull was interrupted!");
            return;
        }

        Gson gson = new Gson();
        //String missionListJson = gson.toJson(missions, )

        try (PrintWriter yo = resp.getWriter()) {
            //Gson gson = new Gson();
            UserManager userManager = ServletUtils.getAgentUserManager(getServletContext());
            Map<String, Entity> usersList = userManager.getUsers();
            //String json = gson.toJson(usersList);
            //out.println(json);
            //out.flush();

            //debug below
            out.println(usersList.toString());
        }
    }
}
