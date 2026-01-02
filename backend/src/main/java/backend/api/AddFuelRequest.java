package backend.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AddFuelRequest {
    private final Double liters;
    private final Double price;
    private final Integer odometer;

    @JsonCreator
    public AddFuelRequest(
            @JsonProperty("liters") Double liters,
            @JsonProperty("price") Double price,
            @JsonProperty("odometer") Integer odometer) {
        this.liters = liters;
        this.price = price;
        this.odometer = odometer;
    }

    public Double getLiters() {
        return liters;
    }

    public Double getPrice() {
        return price;
    }

    public Integer getOdometer() {
        return odometer;
    }
}

