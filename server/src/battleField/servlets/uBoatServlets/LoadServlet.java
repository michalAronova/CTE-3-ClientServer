package battleField.servlets.uBoatServlets;

import DTO.MachineDetails;
import battleField.utils.ServletUtils;
import battleField.utils.SessionUtils;
import com.google.gson.Gson;
import engine.entity.UBoat;
import engine.users.UBoatUserManager;
import exceptions.XMLException.InvalidXMLException;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
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
            resp.setStatus(SC_UNAUTHORIZED);
            out.println("you are not logged in");
            return;
        }

        UBoatUserManager uBoatUserManager = ServletUtils.getUBoatUserManager(getServletContext());
        UBoat boatFromSession = (UBoat) uBoatUserManager.getUser(usernameFromSession);
        if(boatFromSession == null){
            resp.setStatus(SC_UNAUTHORIZED);
            out.println("no boat by name "+ usernameFromSession);
            return;
        }

        Collection<Part> parts = req.getParts();

        for (Part part : parts) {
            boatFromSession.getEngine().loadDataFromXML(part.getInputStream());
            try{
                if(!uBoatUserManager.addBattleFieldName(boatFromSession.getBattleFieldName())){
                    //returns TRUE if the name didn't already exist
                    //hence, if this returns FALSE - the name existed, and should handle! (ERROR)
                    boatFromSession.getEngine().unloadEngine();
                    throw new InvalidXMLException(INVALIDFILE, "Battle field name already exists in server!");
                }
                resp.setContentType("application/json");
                MachineDetails details = boatFromSession.getEngine().getDetails();
                Gson gson = new Gson();
                String detailsJson = gson.toJson(details);
                out.println(detailsJson);
                out.flush();
            }
            catch(InvalidXMLException e){
                resp.setStatus(SC_BAD_REQUEST);
                out.print(e.getMessage());
                System.out.println("ERROR 400");
                System.out.println(e.getMessage());
            }
            finally{
                out.close();
            }
        }
    }
}
