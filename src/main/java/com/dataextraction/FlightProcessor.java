package com.dataextraction;

import com.dataextraction.model.FlightCombination;
import java.math.BigDecimal;
import java.util.List;
import static com.dataextraction.FlightScraper.*;
import static com.dataextraction.IO.Writer.saveCheapestFlightToCSV;
import static com.dataextraction.IO.Writer.saveToCSV;

public class FlightProcessor {

    public static void processFlightData(String jsonData, BigDecimal maxPrice, BigDecimal maxTaxes) {
        List<FlightCombination> flightCombinations = extractFlightData(jsonData, maxPrice, maxTaxes); // method in FlightScraper class
        if (!flightCombinations.isEmpty()) {
            saveToCSV(flightCombinations); // method in Writer class
            System.out.println("All flight combinations saved successfully to 'flights.csv'.");
        }

        List<FlightCombination> cheapestFlights = findCheapestFlights(flightCombinations); // method in FlightScraper class
        if (cheapestFlights != null) {
            saveCheapestFlightToCSV(cheapestFlights); // method in Writer class
            System.out.println("Cheapest flight saved successfully to 'cheapest_flights.csv'.");
        } else {
            System.out.println("No valid flight combinations found.");
        }
    }
}