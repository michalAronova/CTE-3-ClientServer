package battleField.servlets;

import battleField.utils.ServletUtils;
import battleField.utils.SessionUtils;
import engine.entity.Allies;
import engine.entity.EntityEnum;
import engine.entity.UBoat;
import engine.users.UsernameManager;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import parameters.ConstantParams;

import java.io.IOException;

@WebServlet(name = "LogoutServlet", urlPatterns = {"/logout"})
public class LogoutServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain;charset=UTF-8");
        System.out.println("in logout servlet...");
        String usernameFromSession = SessionUtils.getUsername(request);
        EntityEnum entityFromSession = SessionUtils.getEntity(request);
        System.out.println("username: "+usernameFromSession+ " | entity: "+entityFromSession);

        UsernameManager usernameManager = ServletUtils.getUsernameManager(getServletContext());
        System.out.println("usernameFromSession logging out: "+ usernameFromSession);
        response.getWriter().println("usernameFromSession logging out: "+ usernameFromSession);

        if (usernameFromSession != null) { //user is logged in
            synchronized (this) {
                //SessionUtils.clearSession(request);
                request.getSession().removeAttribute(ConstantParams.USERNAME);
                request.getSession().removeAttribute(ConstantParams.ENTITY);
                request.getSession().invalidate();
                System.out.println("AFTER INVALIDATE: ");
                System.out.println(SessionUtils.getUsername(request));
                usernameManager.removeUser(usernameFromSession);
                removeUserByEntity(usernameFromSession, entityFromSession);
            }
        }
    }

    private void removeUserByEntity(String usernameFromSession, EntityEnum entityFromSession) {
        switch (entityFromSession){
            case UBOAT:
                ServletUtils.getUBoatUserManager(getServletContext()).removeUser(usernameFromSession);
            case ALLIES:
                UBoat uboat = (UBoat) ServletUtils.getUBoatUserManager(getServletContext()).getUser(usernameFromSession);
                uboat.removeParticipant(usernameFromSession);
                ServletUtils.getAlliesUserManager(getServletContext()).removeUser(usernameFromSession);
            case AGENT:
                String ally = ServletUtils.getAgentUserManager(getServletContext()).getAllyName(usernameFromSession);
                ((Allies)ServletUtils.getAlliesUserManager(getServletContext()).getUser(ally)).removeAgent(usernameFromSession);
                ServletUtils.getAgentUserManager(getServletContext()).removeUser(usernameFromSession);
        }
    }
}
