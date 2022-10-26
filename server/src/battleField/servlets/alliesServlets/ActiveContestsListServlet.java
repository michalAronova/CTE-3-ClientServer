package battleField.servlets.alliesServlets;

import DTO.contest.Contest;
import battleField.utils.ServletUtils;
import battleField.utils.SessionUtils;
import com.google.gson.Gson;
import engine.entity.Allies;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@WebServlet(name = "ActiveContestsListServlet", urlPatterns = {"/allies/active-contests"})
public class ActiveContestsListServlet extends HttpServlet{
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String usernameFromSession = SessionUtils.getUsername(req);
        PrintWriter out = resp.getWriter();

        if(usernameFromSession == null){
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            out.println("You are not logged in...");
            return;
        }
        if(!SessionUtils.getEntity(req).equals(EntityEnum.ALLIES)){
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            out.println("You are not an ally!");
            return;
        }
        resp.setContentType("application/json");

        Gson gson = new Gson();
        //get all uBoats
        UBoatUserManager uBoatUserManager = ServletUtils.getUBoatUserManager(getServletContext());

        //create contestDTO for each
        List<Contest> allContests = generateContestListFromUBoats(uBoatUserManager.getReadyUBoats());
        //pass json of list of contest DTOs
        String json = gson.toJson(allContests);
        resp.setStatus(HttpServletResponse.SC_OK);
        System.out.println(json);
        out.println(json);
        out.flush();
    }

    private List<Contest> generateContestListFromUBoats(Map<String, UBoat> readyUBoats) {
        List<Contest> contests = new ArrayList<>();
        readyUBoats.forEach((name, uBoat) -> {
                    Contest c = new Contest(uBoat.getBattleFieldName(), uBoat.getUsername(),
                            uBoat.isCompetitionOn().getValue(), uBoat.getEngine().getDifficulty().name(),
                            uBoat.getEngine().getAlliesRequired(), uBoat.getParticipants().size(), uBoat.getOutput());
                    contests.add(c);
        });

        return contests;
    }
}
