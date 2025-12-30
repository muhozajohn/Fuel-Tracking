package com.aem.academy.backend.http;

import com.aem.academy.backend.api.RestApiHandler;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HttpRequestHandler extends AbstractHandler {
    private final RestApiHandler restApiHandler;
    private static final Pattern CAR_ID_PATTERN = Pattern.compile("/cars/(\\d+)/fuel");
    private static final Pattern CAR_ID_STATS_PATTERN = Pattern.compile("/cars/(\\d+)/fuel/stats");

    public HttpRequestHandler(RestApiHandler restApiHandler) {
        this.restApiHandler = restApiHandler;
    }

    @Override
    public void handle(String target, Request baseRequest, 
                      HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        String method = request.getMethod();
        String path = request.getRequestURI();

        // Handle all requests (context is already /api)
        handleRestApi(method, path, request, response);
        baseRequest.setHandled(true);
    }

    private void handleRestApi(String method, String path, 
                               HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        RestApiHandler.Response apiResponse;

        // Path is relative to /api context, so remove /api prefix if present
        String relativePath = path.startsWith("/api") ? path.substring(4) : path;
        if (relativePath.isEmpty()) relativePath = "/";

        if ("POST".equals(method) && "/cars".equals(relativePath)) {
            String body = readRequestBody(request);
            apiResponse = restApiHandler.handleCreateCar(body);
        } else if ("GET".equals(method) && "/cars".equals(relativePath)) {
            apiResponse = restApiHandler.handleListCars();
        } else if ("POST".equals(method)) {
            Matcher matcher = CAR_ID_PATTERN.matcher(relativePath);
            if (matcher.matches()) {
                Long carId = Long.parseLong(matcher.group(1));
                String body = readRequestBody(request);
                apiResponse = restApiHandler.handleAddFuel(carId, body);
            } else {
                apiResponse = new RestApiHandler.Response(404, "application/json", 
                        "{\"error\":\"Not found\"}");
            }
        } else if ("GET".equals(method)) {
            Matcher matcher = CAR_ID_STATS_PATTERN.matcher(relativePath);
            if (matcher.matches()) {
                Long carId = Long.parseLong(matcher.group(1));
                apiResponse = restApiHandler.handleGetFuelStats(carId);
            } else {
                apiResponse = new RestApiHandler.Response(404, "application/json", 
                        "{\"error\":\"Not found\"}");
            }
        } else {
            apiResponse = new RestApiHandler.Response(405, "application/json", 
                    "{\"error\":\"Method not allowed\"}");
        }

        response.setStatus(apiResponse.getStatus());
        response.setContentType(apiResponse.getContentType());
        response.getWriter().write(apiResponse.getBody());
    }

    private String readRequestBody(HttpServletRequest request) throws IOException {
        StringBuilder body = new StringBuilder();
        try (BufferedReader reader = request.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                body.append(line);
            }
        }
        return body.toString();
    }
}

