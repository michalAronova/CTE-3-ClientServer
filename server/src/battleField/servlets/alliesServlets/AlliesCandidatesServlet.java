package battleField.servlets.alliesServlets;

import DTO.missionResult.MissionResult;
import battleField.constants.Constants;
import battleField.utils.ServletUtils;
import battleField.utils.SessionUtils;
import com.google.gson.Gson;
import engine.entity.Allies;
import engine.entity.EntityEnum;
import engine.users.UserManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import static battleField.constants.Constants.CANDIDATES_VERSION;

@WebServlet(name = "AlliesCandidatesServlet", urlPatterns = {"/allies/candidates"})
public class AlliesCandidatesServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        UserManager alliesUserManager = ServletUtils.getAlliesUserManager(getServletContext());
        String username = SessionUtils.getUsername(request);
        Allies ally  = (Allies) alliesUserManager.getEntityObject(username);

        if (username == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
        int clientCandidatesVersion = ServletUtils.getIntParameter(request, CANDIDATES_VERSION);
        if (clientCandidatesVersion == Constants.INT_PARAMETER_ERROR) {
            return;
        }

        int serverCandidatesVersion = 0;
        List<MissionResult> results;
        synchronized (getServletContext()) {
            serverCandidatesVersion = ally.getResultVersion();
            results = ally.getResults(clientCandidatesVersion);
        }

        ResultAndVersion rav = new ResultAndVersion(results, serverCandidatesVersion);
        Gson gson = new Gson();
        String jsonResponse = gson.toJson(rav);
        logServerMessage("Server Candidate version: " + serverCandidatesVersion +
                ", User '" + username + " ("+ EntityEnum.ALLIES +
                ")' Client Candidate version: " + clientCandidatesVersion);
        logServerMessage(jsonResponse);

        try (PrintWriter out = response.getWriter()) {
            out.print(jsonResponse);
            out.flush();
        }
    }

    private void logServerMessage(String message){
        System.out.println(message);
    }

    private static class ResultAndVersion {
        final private List<MissionResult> results;
        final private int version;

        public ResultAndVersion(List<MissionResult> results, int version) {
            this.results = results;
            this.version = version;
        }
    }
}