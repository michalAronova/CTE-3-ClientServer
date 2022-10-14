package battleField.servlets.uBoatServlets;

import DTO.codeObj.CodeObj;
import battleField.utils.ServletUtils;
import battleField.utils.SessionUtils;
import com.google.gson.Gson;
import engine.users.UBoatUserManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

import static jakarta.servlet.http.HttpServletResponse.SC_OK;

@WebServlet(name = "ResetMachineServlet", urlPatterns = {"/uboat/code/reset"})
public class ResetMachineServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        try (PrintWriter out = response.getWriter()) {
            Gson gson = new Gson();
            //get uboat from ses and reset code
            String usernameFromSession = SessionUtils.getUsername(request);
            UBoatUserManager uBoatUserManager = ServletUtils.getUBoatUserManager(getServletContext());
            uBoatUserManager.getEntityObject(usernameFromSession).getEngine().resetMachine();
            CodeObj code = uBoatUserManager.getEntityObject(usernameFromSession).getEngine().getInitialCode();

            //parse code
            String json = gson.toJson(code);

            //put in body
            response.setStatus(SC_OK);
            out.println(json);
            out.flush();
        }
    }
}
