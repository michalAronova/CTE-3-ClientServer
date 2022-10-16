package battleField.servlets.alliesServlets;

import DTO.team.Team;
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
import sun.awt.image.ImageWatched;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;

@WebServlet(name = "RivalAlliesServlet", urlPatterns = {"/allies/rivals"})
public class RivalAlliesServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String usernameFromSession = SessionUtils.getUsername(req);
        PrintWriter out = resp.getWriter();

        if(usernameFromSession == null){
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            out.println("You are not logged in...");
            return;
        }

        if(!SessionUtils.getEntity(req).equals(EntityEnum.ALLIES)){
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            out.println("You are not an ally!");
            return;
        }

        resp.setContentType("application/json");
        UserManager alliesUserManager = ServletUtils.getAlliesUserManager(getServletContext());
        UBoat myBoat = ((Allies) alliesUserManager.getEntityObject(usernameFromSession)).getUBoat();
        Gson gson = new Gson();

        String json = gson.toJson(myBoat.getDTOParticipants());

        resp.setStatus(HttpServletResponse.SC_OK);
        out.println(json);
        out.flush();
    }
}
