package battleField.servlets.alliesServlets;

import battleField.constants.Constants;
import battleField.utils.ServletUtils;
import battleField.utils.SessionUtils;
import engine.entity.Allies;
import engine.entity.EntityEnum;
import engine.users.UserManager;
import engine.users.UsernameManager;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import static battleField.constants.Constants.USERNAME;

    @WebServlet(name = "AlliesLoginServlet", urlPatterns = {"/signup/allies/login"})
public class AlliesLoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain;charset=UTF-8");

        String usernameFromSession = SessionUtils.getUsername(request);
        UsernameManager usernameManager = ServletUtils.getUsernameManager(getServletContext());
        UserManager userManager = ServletUtils.getAlliesUserManager(getServletContext());

        if (usernameFromSession == null) { //user is not logged in yet

            String usernameFromParameter = request.getParameter(USERNAME);
            if (usernameFromParameter == null || usernameFromParameter.isEmpty()) {
                //no username in session and no username in parameter - not standard situation. it's a conflict

                // stands for conflict in server state
                response.setStatus(HttpServletResponse.SC_CONFLICT);
            } else {
                //normalize the username value
                usernameFromParameter = usernameFromParameter.trim();
                synchronized (this) {
                    if (usernameManager.isUserExists(usernameFromParameter)) {
                        String errorMessage = "Username " + usernameFromParameter + " already exists. Please enter a different username.";

                        // stands for unauthorized as there is already such user with this name
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        response.getOutputStream().print(errorMessage);
                        response.getOutputStream()
                                .print("here1111");
                    }
                    else {
                        usernameManager.addUser(usernameFromParameter);
                        //add the new user to the users list
                        //userManager.addUser(usernameFromParameter);
                        userManager.addUser(usernameFromParameter, new Allies(usernameFromParameter));
                        request.getSession(true).setAttribute(Constants.USERNAME, usernameFromParameter);
                        request.getSession().setAttribute(Constants.ENTITY, EntityEnum.ALLIES);

                        response.setStatus(HttpServletResponse.SC_OK);
                        response.getOutputStream()
                                .print(String.format("logged in as %s (%s)",
                                        usernameFromParameter, EntityEnum.ALLIES));
                    }
                }
            }
        } else {
            //user is already logged in
            response.setStatus(HttpServletResponse.SC_OK);
        }
    }

}