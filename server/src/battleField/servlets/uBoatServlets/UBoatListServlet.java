package battleField.servlets.uBoatServlets;

import battleField.utils.ServletUtils;
import engine.users.UserManager;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;

@WebServlet(name = "UBoatListServlet", urlPatterns = {"/uboat/users-list"})
public class UBoatListServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //returning JSON objects
        response.setContentType("application/json");
        try (PrintWriter out = response.getWriter()) {
            //Gson gson = new Gson();
            UserManager userManager = ServletUtils.getUBoatUserManager(getServletContext());
            Set<String> usersList = userManager.getUsers();
            //String json = gson.toJson(usersList);
            //out.println(json);
            //out.flush();

            //debug below
            out.println(usersList.toString());
        }
    }
}
