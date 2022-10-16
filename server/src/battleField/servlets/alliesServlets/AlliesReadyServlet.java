package battleField.servlets.alliesServlets;

import parameters.ConstantParams;
import battleField.utils.ServletUtils;
import battleField.utils.SessionUtils;
import engine.entity.Allies;
import engine.users.UserManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import static parameters.ConstantParams.MISSION_SIZE;

@WebServlet(name = "AlliesReadyServlet", urlPatterns = {"/allies/ready"})
public class AlliesReadyServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //get ally from ses
        UserManager alliesUserManager = ServletUtils.getAlliesUserManager(getServletContext());
        String usernameFromSession = SessionUtils.getUsername(request);

        int missionSizeFromParameter = ServletUtils.getIntParameter(request, MISSION_SIZE);
        if (missionSizeFromParameter == ConstantParams.INT_PARAMETER_ERROR) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        Allies ally  = (Allies) alliesUserManager.getEntityObject(usernameFromSession);
        ally.setMissionSize(missionSizeFromParameter);

        synchronized (this){
            ally.getUBoat().updateAllyReady(usernameFromSession);
        }
    }
}
