package com.dataextraction;

import com.dataextraction.model.FlightCombination;
import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import static com.dataextraction.FlightScraper.*;
import static com.dataextraction.IO.Writer.saveCheapestFlightToCSV;
import static com.dataextraction.IO.Writer.saveToCSV;

public class FlightProcessor {

    public static void processFlightData(String jsonData, BigDecimal maxPrice, BigDecimal maxTaxes) {
        List<FlightCombination> flightCombinations = extractFlightData(jsonData, maxPrice, maxTaxes);
        if (!flightCombinations.isEmpty()) {
            saveToCSV(flightCombinations);
            System.out.println("All flight combinations saved successfully to 'flights.csv'.");
        }

        FlightCombination cheapestFlight = findCheapestFlight(flightCombinations);
        if (cheapestFlight != null) {
            saveCheapestFlightToCSV(cheapestFlight);
            System.out.println("Cheapest flight saved successfully to 'cheapest_flight.csv'.");
        } else {
            System.out.println("No valid flight combinations found.");
        }
    }

    public static String buildApiUrl(String airportDeparture, String airportArrival, String dateDeparture, String dateReturn) {
        return BASE_API_URL + "from=" + airportDeparture + "&to=" + airportArrival +
                "&depart=" + dateDeparture + "&return=" + dateReturn;
    }

    public static String fetchFlightData(String apiUrl) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return response.body();
            } else {
                System.out.println("Failed to fetch data. HTTP Status code: " + response.statusCode());
                return null;
            }
        } catch (Exception e) {
            System.err.println("An error occurred: " + e.getMessage());
            return null;
        }
    }
}