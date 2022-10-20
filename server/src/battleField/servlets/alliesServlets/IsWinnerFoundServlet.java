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

@WebServlet(name = "IsWinnerFoundServlet", urlPatterns = {"/allies/winner/found"})
public class IsWinnerFoundServlet extends HttpServlet {
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

        resp.setContentType("text/html;charset=UTF-8");
        UserManager alliesUserManager = ServletUtils.getAlliesUserManager(getServletContext());
        Allies ally = (Allies) alliesUserManager.getEntityObject(usernameFromSession);

        //get ally's uboat
        UBoat myBoat = ally.getUBoat();
        if(myBoat.isWinnerFound()){
            out.println(myBoat.getWinner());
        }
        else{
            out.println("");
        }
        resp.setStatus(HttpServletResponse.SC_OK);
        out.flush();
    }
}

