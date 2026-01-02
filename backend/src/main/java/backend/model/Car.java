package backend.model;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class Car {
    private static final AtomicLong idGenerator = new AtomicLong(1);
    
    private final Long id;
    private final String brand;
    private final String model;
    private final Integer year;
    private final List<FuelEntry> fuelEntries;

    public Car(String brand, String model, Integer year) {
        this.id = idGenerator.getAndIncrement();
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.fuelEntries = new ArrayList<>();
    }

    public Long getId() {
        return id;
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

    public List<FuelEntry> getFuelEntries() {
        return new ArrayList<>(fuelEntries);
    }

    public void addFuelEntry(FuelEntry fuelEntry) {
        fuelEntries.add(fuelEntry);
    }
}

