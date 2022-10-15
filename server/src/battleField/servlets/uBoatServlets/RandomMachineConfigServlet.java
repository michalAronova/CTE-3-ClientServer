package battleField.servlets.uBoatServlets;

import DTO.codeObj.CodeObj;
import battleField.utils.ServletUtils;
import battleField.utils.SessionUtils;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import engine.entity.UBoat;
import engine.users.UBoatUserManager;
import engine.users.UserManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;

import static jakarta.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static jakarta.servlet.http.HttpServletResponse.SC_OK;

@WebServlet(name = "RandomMachineConfigServlet", urlPatterns = {"/uboat/code/configuration/random"})
public class RandomMachineConfigServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        try (PrintWriter out = response.getWriter()) {
            Gson gson = new Gson();
            //get uboat from ses and auto generate code
            String usernameFromSession = SessionUtils.getUsername(request);
            UBoatUserManager uBoatUserManager = ServletUtils.getUBoatUserManager(getServletContext());
            uBoatUserManager.getEntityObject(usernameFromSession).getEngine().setByAutoGeneratedCode();
            CodeObj code = uBoatUserManager.getEntityObject(usernameFromSession).getEngine().getInitialCode();

            //parse code
            String json = gson.toJson(code);

            //put in body
            response.setStatus(SC_OK);
            System.out.println("server code:" + code);
            out.println(json);
            out.flush();
        }
    }
}
