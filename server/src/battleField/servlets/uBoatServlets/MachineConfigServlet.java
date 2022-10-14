package battleField.servlets.uBoatServlets;

import DTO.codeObj.CodeObj;
import battleField.utils.ServletUtils;
import battleField.utils.SessionUtils;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import engine.users.UBoatUserManager;
import exceptions.XMLException.InvalidXMLException;
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

import static exceptions.XMLException.XMLExceptionMsg.INVALIDFILE;
import static jakarta.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static jakarta.servlet.http.HttpServletResponse.SC_OK;

@WebServlet(name = "MachineConfigServlet", urlPatterns = {"/uboat/code/configuration"})
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 5, maxRequestSize = 1024 * 1024 * 5 * 5)
public class MachineConfigServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String usernameFromSession = SessionUtils.getUsername(request);
        UBoatUserManager uBoatUserManager = ServletUtils.getUBoatUserManager(getServletContext());

        response.setStatus(SC_OK);
        response.setContentType("text/plain");
        PrintWriter out = response.getWriter();

        Gson gson = new Gson();
        Collection<Part> parts = request.getParts();

        for (Part part : parts) {
            try{
                CodeObj code = gson.fromJson(part.getInputStream().toString(), CodeObj.class);
                uBoatUserManager.getEntityObject(usernameFromSession).getEngine().setMachine(code);
                System.out.println("woohoo, code configured!");
                out.println("code configured");
            }
            catch(JsonSyntaxException e){
                response.sendError(SC_BAD_REQUEST);
                System.out.println("Json syntax exception");
                out.println(e.getMessage());
            }
            finally{
                out.close();
            }
        }
    }
}
