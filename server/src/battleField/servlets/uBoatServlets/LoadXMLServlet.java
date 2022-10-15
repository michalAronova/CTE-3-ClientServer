package battleField.servlets.uBoatServlets;

import battleField.utils.ServletUtils;
import battleField.utils.SessionUtils;
import engine.entity.UBoat;
import engine.users.UBoatUserManager;
import exceptions.XMLException.InvalidXMLException;
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

import static exceptions.XMLException.XMLExceptionMsg.INVALIDFILE;
import static jakarta.servlet.http.HttpServletResponse.*;

@WebServlet(name = "LoadXMLServlet", urlPatterns = {"/signup/uboat/upload-file"})
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 5, maxRequestSize = 1024 * 1024 * 5 * 5)
public class LoadXMLServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setStatus(SC_OK);
        String usernameFromSession = SessionUtils.getUsername(request);
        UBoatUserManager uBoatUserManager = ServletUtils.getUBoatUserManager(getServletContext());

        if(usernameFromSession == null){
            response.sendError(SC_UNAUTHORIZED, "you are not logged in");
            return;
        }

        UBoat boatFromSession = (UBoat) uBoatUserManager.getEntityObject(usernameFromSession);
        PrintWriter out = response.getWriter();

        Collection<Part> parts = request.getParts();

        for (Part part : parts) {
            try{
                boatFromSession.getEngine().loadDataFromXML(part.getInputStream());
                if(uBoatUserManager.isBattleFieldExist(boatFromSession.getEngine().getBattleFieldName())){
                    boatFromSession.getEngine().unloadEngine();
                    throw new InvalidXMLException(INVALIDFILE, "Battle field name already exists in server!");
                }
                System.out.println("woohoo, engine loaded!");
                out.println("engine loaded");
            }
            catch(InvalidXMLException e){
                response.sendError(SC_BAD_REQUEST, e.getMessage());
                System.out.println("bad xml!");
                out.println(e.getMessage());
            }
            finally{
                out.close();
            }
        }
    }
}
