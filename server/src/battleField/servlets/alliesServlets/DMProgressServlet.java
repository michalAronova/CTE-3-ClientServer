package battleField.servlets.alliesServlets;

import DTO.dmProgress.DMProgress;
import battleField.utils.ServletUtils;
import battleField.utils.SessionUtils;
import com.google.gson.Gson;
import engine.decipherManager.DecipherManager;
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

@WebServlet(name = "DMProgressServlet", urlPatterns = {"/allies/DM-progress"})
public class DMProgressServlet extends HttpServlet {
    //get the ally entity from session
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        try (PrintWriter out = response.getWriter()) {
            Gson gson = new Gson();
            //get ally from ses
            String usernameFromSession = SessionUtils.getUsername(request);
            UserManager alliesUserManager = ServletUtils.getAlliesUserManager(getServletContext());
            Allies ally = (Allies) alliesUserManager.getEntityObject(usernameFromSession);
            //parse current DM stat
            DecipherManager dm = ally.getDM();
            DMProgress progress = new DMProgress(dm.getTotalMissionAmount(),
                                            dm.getTotalMissionProduced(),
                                            ally.getTotalMissionDone());
            String json = gson.toJson(progress);

            //put in body
            response.setStatus(SC_OK);
            out.println(json);
            out.flush();
        }
    }
}
