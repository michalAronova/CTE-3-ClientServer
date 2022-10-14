package battleField.servlets.uBoatServlets;

import DTO.missionResult.AlliesCandidates;
import battleField.utils.ServletUtils;
import battleField.utils.SessionUtils;
import com.google.gson.Gson;
import engine.entity.Allies;
import engine.entity.UBoat;
import engine.users.UBoatUserManager;
import engine.users.UserManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import javafx.util.Pair;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.LinkedList;
import java.util.stream.Stream;

public class CandidatesFromAlliesServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/plain");
        //get uBoat from session
        String usernameFromSession = SessionUtils.getUsername(request);
        UBoatUserManager userManager = ServletUtils.getUBoatUserManager(getServletContext());
        UBoat uBoat = (UBoat) userManager.getEntityObject(usernameFromSession);

        //synchronized add list of candidates to set
        PrintWriter out = response.getWriter();
        Collection<Part> parts = request.getParts();

        out.println("Total parts : " + parts.size());
        Gson gson = new Gson();

        synchronized (this){
            for (Part part : parts) {
                AlliesCandidates alliesCandidates = gson.fromJson(String.valueOf(part.getInputStream()), AlliesCandidates.class);
                uBoat.addCandidates(alliesCandidates);

                //check each one for match
                for (Pair candidate : alliesCandidates.getCandidates()){
                    if(uBoat.getInput().equals(candidate.getKey())){ //check if the candidate is the real input
                        uBoat.updateVictory(alliesCandidates.getAllyName());
                    }
                }
                //invoke servlet of updating victory boolean
            }
        }
    }
}