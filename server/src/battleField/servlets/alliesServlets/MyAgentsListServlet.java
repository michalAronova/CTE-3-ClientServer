package battleField.servlets.alliesServlets;

import battleField.utils.ServletUtils;
import battleField.utils.SessionUtils;
import com.google.gson.Gson;
import engine.entity.Allies;
import engine.entity.EntityEnum;
import engine.entity.UBoat;
import engine.users.UserManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "MyAgentsListServlet", urlPatterns = {"/allies/myAgents"})
public class MyAgentsListServlet extends HttpServlet {
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

        Gson gson = new Gson();
        UserManager userManager = ServletUtils.getAlliesUserManager(getServletContext());
        Allies allies = (Allies) userManager.getEntityObject(usernameFromSession);

        String json = gson.toJson(allies.getMyAgents());
        resp.setStatus(HttpServletResponse.SC_OK);
        out.println(json);
        out.flush();
        resp.setStatus(HttpServletResponse.SC_OK);
    }
}
