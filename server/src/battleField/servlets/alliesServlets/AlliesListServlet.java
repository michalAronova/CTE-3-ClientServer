package battleField.servlets.alliesServlets;

import battleField.utils.ServletUtils;
import com.google.gson.Gson;
import engine.entity.Entity;
import engine.users.UserManager;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.Set;

@WebServlet(name = "AlliesListServlet", urlPatterns = {"/allies/users-list"})
public class AlliesListServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //returning JSON objects
        response.setContentType("application/json");
        try (PrintWriter out = response.getWriter()) {
            Gson gson = new Gson();
            UserManager userManager = ServletUtils.getAlliesUserManager(getServletContext());
            Map<String, Entity> usersList = userManager.getUsers();
            //returning only the usernames for allies
            //for use in choosing an ally on agent register
            String json = gson.toJson(usersList.keySet());
            out.println(json);
            out.flush();
        }
    }
}
