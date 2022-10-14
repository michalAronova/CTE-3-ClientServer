package battleField.servlets.uBoatServlets;

import DTO.codeObj.CodeObj;
import battleField.utils.ServletUtils;
import battleField.utils.SessionUtils;
import engine.entity.Allies;
import engine.entity.UBoat;
import engine.users.UBoatUserManager;
import engine.users.UserManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import javafx.util.Pair;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import static battleField.constants.Constants.DESIRED_UBOAT;
import static battleField.constants.Constants.MSG_TO_DECIPHER;
import static jakarta.servlet.http.HttpServletResponse.SC_OK;

@WebServlet(name = "DecipherMsgServlet", urlPatterns = {"/uBoat/DecipherMsgServlet"})
public class DecipherMsgServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        //get the uBoat entity from session
        String usernameFromSession = SessionUtils.getUsername(request);
        UBoatUserManager uBoatUserManager = ServletUtils.getUBoatUserManager(getServletContext());
        UBoat uBoat  = (UBoat) uBoatUserManager.getEntityObject(usernameFromSession);
        response.getOutputStream().print(uBoat.getUsername());

        String message = request.getParameter(MSG_TO_DECIPHER);
        response.getOutputStream().print(message);
        if (message == null || message.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_CONFLICT);
            response.getOutputStream().print("error");
        } else {
            synchronized (this) {
                //if words not in dictionary
                if(!uBoat.getEngine().getDictionary().areAllWordsInDictionary(message)){
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                }
                //message = uBoat.getEngine().getDictionary().removeExcludedChars(message);
                response.getOutputStream().print(message);
                String output = uBoat.getEngine().processMsg(message);
                response.getOutputStream().print("output: " + output);
                response.setStatus(SC_OK);
            }
        }
    }
}
