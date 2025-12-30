package com.aem.academy.backend;

import com.aem.academy.backend.api.RestApiHandler;
import com.aem.academy.backend.http.HttpRequestHandler;
import com.aem.academy.backend.service.CarService;
import com.aem.academy.backend.servlet.FuelStatsServlet;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

public class Server {
    private static final int PORT = 8080;

    public static void main(String[] args) throws Exception {
        // Create shared service instance
        CarService carService = new CarService();

        // Create REST API handler
        RestApiHandler restApiHandler = new RestApiHandler(carService);

        // Create Jetty server
        org.eclipse.jetty.server.Server server = new org.eclipse.jetty.server.Server(PORT);

        // Setup REST API handler (handles /api/*)
        HttpRequestHandler apiHandler = new HttpRequestHandler(restApiHandler);
        ContextHandler apiContext = new ContextHandler("/api");
        apiContext.setHandler(apiHandler);

        // Setup servlet context for servlet endpoint (only handles /servlet/*)
        ServletContextHandler servletContext = new ServletContextHandler(ServletContextHandler.SESSIONS);
        servletContext.setContextPath("/servlet");
        servletContext.addServlet(new ServletHolder(new FuelStatsServlet(carService)), "/fuel-stats");

        // HandlerList processes in order - API handler first, then servlet
        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[]{apiContext, servletContext});

        server.setHandler(handlers);

        System.out.println("Server starting on port " + PORT);
        System.out.println("REST API endpoints:");
        System.out.println("  POST   /api/cars");
        System.out.println("  GET    /api/cars");
        System.out.println("  POST   /api/cars/{id}/fuel");
        System.out.println("  GET    /api/cars/{id}/fuel/stats");
        System.out.println("Servlet endpoint:");
        System.out.println("  GET    /servlet/fuel-stats?carId={id}");

        server.start();
        server.join();
    }
}

