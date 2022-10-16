package battleField.servlets.uBoatServlets;

import DTO.contest.Contest;
import battleField.utils.ServletUtils;
import com.google.gson.Gson;
import engine.entity.Entity;
import engine.entity.UBoat;
import engine.users.UserManager;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@WebServlet(name = "UBoatListServlet", urlPatterns = {"/uboat/users-list"})
public class UBoatListServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //returning JSON objects
        response.setContentType("application/json");
        try (PrintWriter out = response.getWriter()) {
            Gson gson = new Gson();
            UserManager userManager = ServletUtils.getUBoatUserManager(getServletContext());
            Map<String, Entity> usersList = userManager.getUsers();

            List<Contest> contests = new LinkedList<>();
            for (Entity boat: usersList.values()) {
                contests.add(((UBoat)boat).getAsDTO());
            }

            String json = gson.toJson(contests);
            out.println(json);
            out.flush();
        }
    }
}
