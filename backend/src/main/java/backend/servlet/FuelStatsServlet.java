package backend.servlet;

import backend.model.FuelStats;
import backend.service.CarService;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.NoSuchElementException;

public class FuelStatsServlet extends HttpServlet {
    private final CarService carService;
    private final ObjectMapper objectMapper;

    public FuelStatsServlet(CarService carService) {
        this.carService = carService;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        // Manually parse carId from query parameters
        String carIdParam = request.getParameter("carId");
        
        if (carIdParam == null || carIdParam.trim().isEmpty()) {
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\":\"carId parameter is required\"}");
            return;
        }

        try {
            Long carId = Long.parseLong(carIdParam);
            FuelStats stats = carService.calculateFuelStats(carId);
            
            // Set Content-Type and status code explicitly
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_OK);
            
            String json = objectMapper.writeValueAsString(stats);
            response.getWriter().write(json);
            
        } catch (NumberFormatException e) {
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\":\"Invalid carId format\"}");
        } catch (NoSuchElementException e) {
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().write("{\"error\":\"" + e.getMessage() + "\"}");
        } catch (Exception e) {
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\":\"Internal server error\"}");
        }
    }
}

