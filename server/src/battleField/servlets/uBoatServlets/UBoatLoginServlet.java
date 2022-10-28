package battleField.servlets.uBoatServlets;

import engine.entity.EntityEnum;
import engine.entity.UBoat;
import engine.users.UserManager;
import engine.users.UsernameManager;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import parameters.ConstantParams;
import battleField.utils.ServletUtils;
import battleField.utils.SessionUtils;

import java.io.IOException;

import static parameters.ConstantParams.USERNAME;

import java.io.PrintWriter;

@WebServlet(name = "UBoatLoginServlet", urlPatterns = {"/signup/uboat/login"})
public class UBoatLoginServlet  extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain;charset=UTF-8");

        String usernameFromSession = SessionUtils.getUsername(request);
        UsernameManager usernameManager = ServletUtils.getUsernameManager(getServletContext());
        UserManager userManager = ServletUtils.getUBoatUserManager(getServletContext());
        PrintWriter out = response.getWriter();

        out.println("trying to login. usernameFromSession: "+ usernameFromSession);
        System.out.println("trying to login. usernameFromSession: "+ usernameFromSession);

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
                        out.println(errorMessage);
                        System.out.println(errorMessage);
                    }
                    else {
                        usernameManager.addUser(usernameFromParameter);
                        //add the new user to the users list
                        //userManager.addUser(usernameFromParameter);
                        userManager.addUser(usernameFromParameter, new UBoat(usernameFromParameter));
                        request.getSession(true).setAttribute(ConstantParams.USERNAME, usernameFromParameter);
                        request.getSession().setAttribute(ConstantParams.ENTITY, EntityEnum.UBOAT);
                        response.setStatus(HttpServletResponse.SC_OK);
                        out.println(String.format("logged in as %s (%s)",
                                usernameFromParameter, EntityEnum.UBOAT));

                        System.out.printf("logged in as %s (%s)%n",
                                usernameFromParameter, EntityEnum.UBOAT);
                    }
                }
            }
        } else {
            //user is already logged in
            response.setStatus(HttpServletResponse.SC_OK);
            out.println("user is already logged in");
            System.out.println("user is already logged in");
        }
    }
}