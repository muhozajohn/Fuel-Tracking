package com.aem.academy.backend.service;

import com.aem.academy.backend.model.Car;
import com.aem.academy.backend.model.FuelEntry;
import com.aem.academy.backend.model.FuelStats;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class CarService {
    private final Map<Long, Car> cars = new ConcurrentHashMap<>();

    public Car createCar(String brand, String model, Integer year) {
        if (brand == null || brand.trim().isEmpty()) {
            throw new IllegalArgumentException("Brand cannot be null or empty");
        }
        if (model == null || model.trim().isEmpty()) {
            throw new IllegalArgumentException("Model cannot be null or empty");
        }
        if (year == null || year < 1900 || year > 2100) {
            throw new IllegalArgumentException("Year must be between 1900 and 2100");
        }

        Car car = new Car(brand.trim(), model.trim(), year);
        cars.put(car.getId(), car);
        return car;
    }

    public List<Car> getAllCars() {
        return new ArrayList<>(cars.values());
    }

    public Car getCarById(Long id) {
        Car car = cars.get(id);
        if (car == null) {
            throw new NoSuchElementException("Car with id " + id + " not found");
        }
        return car;
    }

    public void addFuelEntry(Long carId, Double liters, Double price, Integer odometer) {
        if (liters == null || liters <= 0) {
            throw new IllegalArgumentException("Liters must be greater than 0");
        }
        if (price == null || price < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }
        if (odometer == null || odometer < 0) {
            throw new IllegalArgumentException("Odometer cannot be negative");
        }

        Car car = getCarById(carId);
        FuelEntry fuelEntry = new FuelEntry(liters, price, odometer);
        car.addFuelEntry(fuelEntry);
    }

    public FuelStats calculateFuelStats(Long carId) {
        Car car = getCarById(carId);
        List<FuelEntry> fuelEntries = car.getFuelEntries();

        if (fuelEntries.isEmpty()) {
            return new FuelStats(0.0, 0.0, 0.0);
        }

        double totalFuel = fuelEntries.stream()
                .mapToDouble(FuelEntry::getLiters)
                .sum();

        double totalCost = fuelEntries.stream()
                .mapToDouble(FuelEntry::getPrice)
                .sum();

        double averageConsumption = 0.0;
        if (fuelEntries.size() >= 2) {
            // Sort entries by odometer reading (ascending)
            List<FuelEntry> sortedEntries = fuelEntries.stream()
                    .sorted(Comparator.comparingInt(FuelEntry::getOdometer))
                    .collect(Collectors.toList());

            // Calculate total distance traveled (from first to last odometer reading)
            int firstOdometer = sortedEntries.get(0).getOdometer();
            int lastOdometer = sortedEntries.get(sortedEntries.size() - 1).getOdometer();
            int totalDistance = lastOdometer - firstOdometer;

            if (totalDistance > 0) {
                // Calculate total fuel consumed
                // Each fuel entry represents fuel filled at that odometer reading
                // The fuel is used to travel from the previous odometer to the current one
                // For calculation: sum all fuel entries (they all contribute to the distance traveled)
                double totalFuelConsumed = sortedEntries.stream()
                        .mapToDouble(FuelEntry::getLiters)
                        .sum();

                // Calculate average consumption: (total fuel / total distance) * 100 = L/100km
                // This gives the average fuel consumption per 100km over the entire distance
                averageConsumption = (totalFuelConsumed / totalDistance) * 100.0;
            }
        }

        return new FuelStats(totalFuel, totalCost, averageConsumption);
    }
}

