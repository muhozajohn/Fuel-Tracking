package cli;

import cli.model.FuelStatsResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class CliApplication {
    private static final String BASE_URL = "http://localhost:8080";
    private static final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build();
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static void main(String[] args) {
        if (args.length == 0) {
            printUsage();
            return;
        }

        String command = args[0];

        try {
            switch (command) {
                case "create-car":
                    handleCreateCar(args);
                    break;
                case "add-fuel":
                    handleAddFuel(args);
                    break;
                case "fuel-stats":
                    handleFuelStats(args);
                    break;
                default:
                    System.err.println("Unknown command: " + command);
                    printUsage();
                    System.exit(1);
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static void handleCreateCar(String[] args) throws Exception {
        String brand = null;
        String model = null;
        Integer year = null;

        for (int i = 1; i < args.length; i++) {
            if (args[i].equals("--brand") && i + 1 < args.length) {
                brand = args[++i];
            } else if (args[i].equals("--model") && i + 1 < args.length) {
                model = args[++i];
            } else if (args[i].equals("--year") && i + 1 < args.length) {
                year = Integer.parseInt(args[++i]);
            }
        }

        if (brand == null || model == null || year == null) {
            System.err.println("Error: --brand, --model, and --year are required");
            System.exit(1);
        }

        String jsonBody = String.format(
                "{\"brand\":\"%s\",\"model\":\"%s\",\"year\":%d}",
                escapeJson(brand), escapeJson(model), year);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/api/cars"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 201) {
            System.out.println("Car created successfully");
            System.out.println(response.body());
        } else {
            System.err.println("Failed to create car: " + response.body());
            System.exit(1);
        }
    }

    private static void handleAddFuel(String[] args) throws Exception {
        Long carId = null;
        Double liters = null;
        Double price = null;
        Integer odometer = null;

        for (int i = 1; i < args.length; i++) {
            if (args[i].equals("--carId") && i + 1 < args.length) {
                carId = Long.parseLong(args[++i]);
            } else if (args[i].equals("--liters") && i + 1 < args.length) {
                liters = Double.parseDouble(args[++i]);
            } else if (args[i].equals("--price") && i + 1 < args.length) {
                price = Double.parseDouble(args[++i]);
            } else if (args[i].equals("--odometer") && i + 1 < args.length) {
                odometer = Integer.parseInt(args[++i]);
            }
        }

        if (carId == null || liters == null || price == null || odometer == null) {
            System.err.println("Error: --carId, --liters, --price, and --odometer are required");
            System.exit(1);
        }

        String jsonBody = String.format(
                "{\"liters\":%.2f,\"price\":%.2f,\"odometer\":%d}",
                liters, price, odometer);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/api/cars/" + carId + "/fuel"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 201) {
            System.out.println("Fuel entry added successfully");
        } else {
            System.err.println("Failed to add fuel entry: " + response.body());
            System.exit(1);
        }
    }

    private static void handleFuelStats(String[] args) throws Exception {
        Long carId = null;

        for (int i = 1; i < args.length; i++) {
            if (args[i].equals("--carId") && i + 1 < args.length) {
                carId = Long.parseLong(args[++i]);
            }
        }

        if (carId == null) {
            System.err.println("Error: --carId is required");
            System.exit(1);
        }

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/api/cars/" + carId + "/fuel/stats"))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            // Parse JSON response and format output
            String body = response.body();
            parseAndDisplayStats(body);
        } else {
            System.err.println("Failed to get fuel stats: " + response.body());
            System.exit(1);
        }
    }

    private static void parseAndDisplayStats(String json) {
        try {
            FuelStatsResponse stats = objectMapper.readValue(json, FuelStatsResponse.class);
            // Match exact output format from assignment:
            // Total fuel: 120 L
            // Total cost: 155.00
            // Average consumption: 6.4 L/100km
            System.out.println("Total fuel: " + String.format("%.0f", stats.getTotalFuel()) + " L");
            System.out.println("Total cost: " + String.format("%.2f", stats.getTotalCost()));
            System.out.println("Average consumption: " + String.format("%.1f", stats.getAverageConsumption()) + " L/100km");
        } catch (Exception e) {
            System.err.println("Failed to parse response: " + e.getMessage());
            System.out.println(json);
        }
    }

    private static String escapeJson(String str) {
        return str.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }

    private static void printUsage() {
        System.out.println("Usage:");
        System.out.println("  create-car --brand <brand> --model <model> --year <year>");
        System.out.println("  add-fuel --carId <id> --liters <liters> --price <price> --odometer <odometer>");
        System.out.println("  fuel-stats --carId <id>");
    }
}

