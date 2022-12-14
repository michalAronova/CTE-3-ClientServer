package battleField.servlets.uBoatServlets;

import DTO.missionResult.MissionResult;
import parameters.ConstantParams;
import battleField.utils.ServletUtils;
import battleField.utils.SessionUtils;
import com.google.gson.Gson;
import engine.entity.EntityEnum;
import engine.entity.UBoat;
import engine.users.UBoatUserManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import static parameters.ConstantParams.CANDIDATES_VERSION;

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
        if (clientCandidatesVersion == ConstantParams.INT_PARAMETER_ERROR) {
            return;
        }

        int serverCandidatesVersion = 0;
        List<MissionResult> results;
        synchronized (getServletContext()) {
            serverCandidatesVersion = boat.getResultVersion();
            results = boat.getResults(clientCandidatesVersion);
        }

        Gson gson = new Gson();
        String jsonResponse = gson.toJson(results);

        try (PrintWriter out = response.getWriter()) {
            out.print(jsonResponse);
            out.flush();
        }
    }
}