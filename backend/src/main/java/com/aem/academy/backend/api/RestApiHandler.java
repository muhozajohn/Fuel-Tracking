package com.aem.academy.backend.api;

import com.aem.academy.backend.model.Car;
import com.aem.academy.backend.model.FuelStats;
import com.aem.academy.backend.service.CarService;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

public class RestApiHandler {
    private final CarService carService;
    private final ObjectMapper objectMapper;

    public RestApiHandler(CarService carService) {
        this.carService = carService;
        this.objectMapper = new ObjectMapper();
    }

    public Response handleCreateCar(String requestBody) {
        try {
            CreateCarRequest request = objectMapper.readValue(requestBody, CreateCarRequest.class);
            Car car = carService.createCar(request.getBrand(), request.getModel(), request.getYear());
            String json = objectMapper.writeValueAsString(car);
            return new Response(201, "application/json", json);
        } catch (IllegalArgumentException e) {
            return new Response(400, "application/json", 
                    "{\"error\":\"" + e.getMessage() + "\"}");
        } catch (IOException e) {
            return new Response(400, "application/json", 
                    "{\"error\":\"Invalid JSON format\"}");
        } catch (Exception e) {
            return new Response(500, "application/json", 
                    "{\"error\":\"Internal server error\"}");
        }
    }

    public Response handleListCars() {
        try {
            List<Car> cars = carService.getAllCars();
            String json = objectMapper.writeValueAsString(cars);
            return new Response(200, "application/json", json);
        } catch (Exception e) {
            return new Response(500, "application/json", 
                    "{\"error\":\"Internal server error\"}");
        }
    }

    public Response handleAddFuel(Long carId, String requestBody) {
        try {
            AddFuelRequest request = objectMapper.readValue(requestBody, AddFuelRequest.class);
            carService.addFuelEntry(carId, request.getLiters(), request.getPrice(), request.getOdometer());
            return new Response(201, "application/json", "{\"message\":\"Fuel entry added successfully\"}");
        } catch (NoSuchElementException e) {
            return new Response(404, "application/json", 
                    "{\"error\":\"" + e.getMessage() + "\"}");
        } catch (IllegalArgumentException e) {
            return new Response(400, "application/json", 
                    "{\"error\":\"" + e.getMessage() + "\"}");
        } catch (IOException e) {
            return new Response(400, "application/json", 
                    "{\"error\":\"Invalid JSON format\"}");
        } catch (Exception e) {
            return new Response(500, "application/json", 
                    "{\"error\":\"Internal server error\"}");
        }
    }

    public Response handleGetFuelStats(Long carId) {
        try {
            FuelStats stats = carService.calculateFuelStats(carId);
            String json = objectMapper.writeValueAsString(stats);
            return new Response(200, "application/json", json);
        } catch (NoSuchElementException e) {
            return new Response(404, "application/json", 
                    "{\"error\":\"" + e.getMessage() + "\"}");
        } catch (Exception e) {
            return new Response(500, "application/json", 
                    "{\"error\":\"Internal server error\"}");
        }
    }

    public static class Response {
        private final int status;
        private final String contentType;
        private final String body;

        public Response(int status, String contentType, String body) {
            this.status = status;
            this.contentType = contentType;
            this.body = body;
        }

        public int getStatus() {
            return status;
        }

        public String getContentType() {
            return contentType;
        }

        public String getBody() {
            return body;
        }
    }
}

