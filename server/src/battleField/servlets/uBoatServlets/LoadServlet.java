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

@WebServlet(name = "LoadServlet", urlPatterns = {"/uboat/upload"})
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 5, maxRequestSize = 1024 * 1024 * 5 * 5)
public class LoadServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setStatus(200);
        PrintWriter out = resp.getWriter();

        String usernameFromSession = SessionUtils.getUsername(req);

        if(usernameFromSession == null){
            resp.sendError(SC_UNAUTHORIZED, "you are not logged in");
            out.println("you are not logged in");
            return;
        }

        UBoatUserManager uBoatUserManager = ServletUtils.getUBoatUserManager(getServletContext());
        UBoat boatFromSession = (UBoat) uBoatUserManager.getUser(usernameFromSession);
        if(boatFromSession == null){
            resp.sendError(SC_UNAUTHORIZED, "no boat by this name");
            out.println("no boat by name "+ usernameFromSession);
            return;
        }

        Collection<Part> parts = req.getParts();

        for (Part part : parts) {
            boatFromSession.getEngine().loadDataFromXML(part.getInputStream());
            try{
                if(uBoatUserManager.isBattleFieldExist(boatFromSession.getBattleFieldName())){
                    boatFromSession.getEngine().unloadEngine();
                    throw new InvalidXMLException(INVALIDFILE, "Battle field name already exists in server!");
                }
                out.println("engine loaded");
            }
            catch(InvalidXMLException e){
                resp.sendError(SC_BAD_REQUEST, e.getMessage());
                out.println(e.getMessage());
            }
            finally{
                out.close();
            }
        }
    }
}
