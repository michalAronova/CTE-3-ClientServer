package battleField.listener;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class ContextListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {

        System.out.println("My web app is being initialized");
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

        System.out.println("My web app is being destroyed ... :(");
    }
}