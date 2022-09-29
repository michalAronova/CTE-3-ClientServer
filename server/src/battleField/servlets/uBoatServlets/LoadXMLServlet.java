package battleField.servlets.uBoatServlets;

import battleField.utils.ServletUtils;
import battleField.utils.SessionUtils;
import engine.Engine;
import engine.TheEngine;
import engine.users.UserManager;
import exceptions.XMLException.InvalidXMLException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Scanner;

import static jakarta.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static jakarta.servlet.http.HttpServletResponse.SC_OK;

@WebServlet(name = "LoadXMLServlet", urlPatterns = {"/signup/uboat/upload-file"})
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 5, maxRequestSize = 1024 * 1024 * 5 * 5)
public class LoadXMLServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String usernameFromSession = SessionUtils.getUsername(request);
        UserManager userManager = ServletUtils.getUBoatUserManager(getServletContext());

        response.setStatus(SC_OK);
        response.setContentType("text/plain");
        PrintWriter out = response.getWriter();

        Collection<Part> parts = request.getParts();

        for (Part part : parts) {

            try{
                userManager.getEntityObject(usernameFromSession).getEngine().loadDataFromXML(part.getInputStream());
                System.out.println("woohoo, engine loaded!");
                out.println("engine loaded");
            }
            catch(InvalidXMLException e){
                response.sendError(SC_BAD_REQUEST, e.getMessage());
                System.out.println("bad xml!");
                out.println(e.getMessage());
            }
        }

    }

}
