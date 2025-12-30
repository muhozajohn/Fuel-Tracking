package com.aem.academy.backend.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CreateCarRequest {
    private final String brand;
    private final String model;
    private final Integer year;

    @JsonCreator
    public CreateCarRequest(
            @JsonProperty("brand") String brand,
            @JsonProperty("model") String model,
            @JsonProperty("year") Integer year) {
        this.brand = brand;
        this.model = model;
        this.year = year;
    }

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

    public Integer getYear() {
        return year;
    }
}

