package com.aem.academy.backend.model;

public class FuelStats {
    private final Double totalFuel;
    private final Double totalCost;
    private final Double averageConsumption;

    public FuelStats(Double totalFuel, Double totalCost, Double averageConsumption) {
        this.totalFuel = totalFuel;
        this.totalCost = totalCost;
        this.averageConsumption = averageConsumption;
    }

    public Double getTotalFuel() {
        return totalFuel;
    }

    public Double getTotalCost() {
        return totalCost;
    }

    public Double getAverageConsumption() {
        return averageConsumption;
    }
}

