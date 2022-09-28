package battleField.servlets.uBoatServlets;

import engine.entity.EntityEnum;
import engine.users.UserManager;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import battleField.constants.Constants;
import battleField.utils.ServletUtils;
import battleField.utils.SessionUtils;
import engine.users.UserManager;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import static battleField.constants.Constants.USERNAME;
import java.io.IOException;

@WebServlet(name = "UBoatLoginServlet", urlPatterns = {"/signup/uboat/login"})
public class UBoatLoginServlet  extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain;charset=UTF-8");

        String usernameFromSession = SessionUtils.getUsername(request);
        UserManager userManager = ServletUtils.getUBoatUserManager(getServletContext());

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
                    if (userManager.isUserExists(usernameFromParameter)) {
                        String errorMessage = "Username " + usernameFromParameter + " already exists. Please enter a different username.";

                        // stands for unauthorized as there is already such user with this name
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        response.getOutputStream().print(errorMessage);
                    }
                    else {
                        //add the new user to the users list
                        userManager.addUser(usernameFromParameter);
                        //userManager.addUser(usernameFromParameter, new TheEngine());
                        request.getSession(true).setAttribute(Constants.USERNAME, usernameFromParameter);
                        request.getSession().setAttribute(Constants.ENTITY, EntityEnum.UBOAT);
                        //redirect the request to the chat room - in order to actually change the URL
                        System.out.println("On login, request URI is: " + request.getRequestURI());
                        response.setStatus(HttpServletResponse.SC_OK);
                        //response.getOutputStream().print(String.format("logged in: %s as %s", usernameFromParameter, Constants.UBOAT));
                    }
                }
            }
        } else {
            //user is already logged in
            response.setStatus(HttpServletResponse.SC_OK);
            //response.getOutputStream().print("you are already logged in (same session)");
        }
    }
}