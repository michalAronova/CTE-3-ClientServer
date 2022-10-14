package battleField.servlets.uBoatServlets;

import battleField.utils.ServletUtils;
import battleField.utils.SessionUtils;
import com.google.gson.Gson;
import engine.entity.Allies;
import engine.entity.EntityEnum;
import engine.entity.UBoat;
import engine.users.UBoatUserManager;
import engine.users.UserManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "ParticipantsListServlet", urlPatterns = {"/uboat/participants"})
public class ParticipantsListServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String usernameFromSession = SessionUtils.getUsername(req);
        PrintWriter out = resp.getWriter();

        if(usernameFromSession == null){
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            out.println("You are not logged in...");
            return;
        }

        if(!SessionUtils.getEntity(req).equals(EntityEnum.UBOAT)){
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            out.println("You are not a UBoat!");
            return;
        }
        resp.setContentType("application/json");
        UBoatUserManager uBoatUserManager = ServletUtils.getUBoatUserManager(getServletContext());
        Gson gson = new Gson();

        String json = gson.toJson(((UBoat) uBoatUserManager
                                            .getUser(usernameFromSession))
                                            .getDTOParticipants());

        resp.setStatus(HttpServletResponse.SC_OK);
        out.println(json);
        out.flush();
    }
}
