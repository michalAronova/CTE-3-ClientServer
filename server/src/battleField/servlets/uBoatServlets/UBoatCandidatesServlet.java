package battleField.servlets.uBoatServlets;

import DTO.missionResult.MissionResult;
import battleField.constants.Constants;
import battleField.servlets.alliesServlets.AlliesCandidatesServlet;
import battleField.utils.ServletUtils;
import battleField.utils.SessionUtils;
import com.google.gson.Gson;
import engine.entity.Allies;
import engine.entity.EntityEnum;
import engine.entity.UBoat;
import engine.users.UBoatUserManager;
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

@WebServlet(name = "UBoatCandidatesServlet", urlPatterns = {"/uboat/candidates"})
public class UBoatCandidatesServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        UBoatUserManager uBoatUserManager = ServletUtils.getUBoatUserManager(getServletContext());
        String username = SessionUtils.getUsername(request);
        if (username == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
        UBoat boat = (UBoat) uBoatUserManager.getEntityObject(username);
        int clientCandidatesVersion = ServletUtils.getIntParameter(request, CANDIDATES_VERSION);
        if (clientCandidatesVersion == Constants.INT_PARAMETER_ERROR) {
            return;
        }

        int serverCandidatesVersion = 0;
        List<MissionResult> results;
        synchronized (getServletContext()) {
            serverCandidatesVersion = boat.getResultVersion();
            results = boat.getResults(clientCandidatesVersion);
        }

        ResultAndVersion rav = new ResultAndVersion(results, serverCandidatesVersion);
        Gson gson = new Gson();
        String jsonResponse = gson.toJson(rav);
        logServerMessage("Server Candidate version: " + serverCandidatesVersion +
                ", User '" + username + " ("+ EntityEnum.UBOAT +
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