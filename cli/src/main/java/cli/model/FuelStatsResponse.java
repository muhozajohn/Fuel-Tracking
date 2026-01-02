package cli.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class FuelStatsResponse {
    private final Double totalFuel;
    private final Double totalCost;
    private final Double averageConsumption;

    @JsonCreator
    public FuelStatsResponse(
            @JsonProperty("totalFuel") Double totalFuel,
            @JsonProperty("totalCost") Double totalCost,
            @JsonProperty("averageConsumption") Double averageConsumption) {
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

