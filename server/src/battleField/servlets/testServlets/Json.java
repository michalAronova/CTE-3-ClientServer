package battleField.servlets.testServlets;

import battleField.utils.ServletUtils;
import com.google.gson.Gson;
import engine.decipherManager.dictionary.Dictionary;
import engine.decipherManager.mission.Mission;
import engine.entity.Entity;
import engine.users.UserManager;
import enigmaMachine.Machine;
import enigmaMachine.keyBoard.KeyBoard;
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

@WebServlet(name="jsonServlet", urlPatterns = {"/json"})
public class Json extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        List<Character> chars = new ArrayList<>();
//        chars.add('a');
//        chars.add('a');
//        chars.add('a');
        Machine machine = new Machine(new KeyBoard("abc"), 3);
        //Mission mission = new Mission(machine, chars,
        //        100, "abba", new Dictionary(), (list) -> list, null, null);

        resp.setContentType("application/json");
        try (PrintWriter out = resp.getWriter()) {
            Gson gson = new Gson();
            String json = gson.toJson(machine);
            out.println(json);
            out.flush();
        }
    }
}
