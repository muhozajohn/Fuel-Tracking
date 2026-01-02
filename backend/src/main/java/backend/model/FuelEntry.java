package backend.model;

public class FuelEntry {
    private final Double liters;
    private final Double price;
    private final Integer odometer;

    public FuelEntry(Double liters, Double price, Integer odometer) {
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

