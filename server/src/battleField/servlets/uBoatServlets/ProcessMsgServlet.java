package battleField.servlets.uBoatServlets;

import DTO.processResponse.ProcessResponse;
import battleField.utils.ServletUtils;
import battleField.utils.SessionUtils;
import com.google.gson.Gson;
import engine.entity.UBoat;
import engine.users.UBoatUserManager;
import exceptions.InputException.InputException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

import static parameters.ConstantParams.MSG_TO_PROCESS;
import static jakarta.servlet.http.HttpServletResponse.SC_OK;

@WebServlet(name = "ProcessMsgServlet", urlPatterns = {"/uboat/process"})
public class ProcessMsgServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/plain");
        PrintWriter out = response.getWriter();
        //get the uBoat entity from session
        String usernameFromSession = SessionUtils.getUsername(request);
        UBoatUserManager uBoatUserManager = ServletUtils.getUBoatUserManager(getServletContext());
        UBoat uBoat  = (UBoat) uBoatUserManager.getEntityObject(usernameFromSession);

        String message = request.getParameter(MSG_TO_PROCESS);
        if (message == null || message.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_CONFLICT);
            out.close();
            return;
        } else {
            if (!uBoat.getEngine().getDictionary().areAllWordsInDictionary(message)) {
                out.println("trying to process words not from dictionary");
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.close();
                return;
            }
            try {
                String output = uBoat.encryptMsg(message);
                Gson gson = new Gson();
                String processResponse = gson
                        .toJson(new ProcessResponse(output, uBoat.getEngine().getUpdatedCode()));

                out.println(processResponse);
                response.setStatus(SC_OK);
            }
            catch(InputException e){
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.println(e.getMessage());
            }
            finally {
                out.close();
            }
        }
    }
}
