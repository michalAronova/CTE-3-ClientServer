package battleField.servlets.agentServlets;

import DTO.missionResult.MissionResult;
import battleField.utils.ServletUtils;
import battleField.utils.SessionUtils;
import com.google.gson.Gson;
import engine.entity.Agent;
import engine.entity.Allies;
import engine.users.AgentUserManager;
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

import static engine.ServerEngine.getStringFromInputStream;
import static jakarta.servlet.http.HttpServletResponse.SC_OK;

@WebServlet(name = "UploadCandidatesServlet", urlPatterns = {"/agent/upload/candidates"})
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 5, maxRequestSize = 1024 * 1024 * 5 * 5)
public class UploadCandidatesServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String usernameFromSession = SessionUtils.getUsername(req);
        AgentUserManager agentUserManager = ServletUtils.getAgentUserManager(getServletContext());
        String allyName = agentUserManager.getAllyName(usernameFromSession);
        UserManager allyUserManager = ServletUtils.getAlliesUserManager(getServletContext());
        Allies ally = (Allies) allyUserManager.getEntityObject(allyName);

        resp.setStatus(SC_OK);
        resp.setContentType("text/plain");
        PrintWriter out = resp.getWriter();

        Collection<Part> parts = req.getParts();

        for (Part part : parts) {
            Gson gson = new Gson();
            String json = getStringFromInputStream(part.getInputStream());
            MissionResult result = gson.fromJson(json, MissionResult.class);
            ally.addResult(result);
        }
    }
}
