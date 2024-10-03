package com.dataextraction.IO;

import com.dataextraction.model.FlightCombination;
import com.dataextraction.model.FlightSegment;
import com.opencsv.CSVWriter;
import java.io.FileWriter;
import java.util.List;
import static com.dataextraction.FlightScraper.*;

public class Writer {

    public static void writeFlightCombinationsToCSV(String filePath, List<FlightCombination> flightCombinations) {
        try (CSVWriter writer = new CSVWriter(new FileWriter(filePath))) {
            writeCSVHeader(writer);

            for (FlightCombination combination : flightCombinations) {
                writeFlightDataRow(writer, combination);
            }
        } catch (Exception e) {
            System.err.println("An error occurred: " + e.getMessage());
        }
    }

    private static void writeCSVHeader(CSVWriter writer) {
        String[] header = {
                "Price", "Taxes",
                "Outbound 1 Airport Departure", "Outbound 1 Airport Arrival", "Outbound 1 Time Departure", "Outbound 1 Time Arrival", "Outbound 1 Flight Number",
                "Outbound 2 Airport Departure", "Outbound 2 Airport Arrival", "Outbound 2 Time Departure", "Outbound 2 Time Arrival", "Outbound 2 Flight Number",
                "Inbound 1 Airport Departure", "Inbound 1 Airport Arrival", "Inbound 1 Time Departure", "Inbound 1 Time Arrival", "Inbound 1 Flight Number",
                "Inbound 2 Airport Departure", "Inbound 2 Airport Arrival", "Inbound 2 Time Departure", "Inbound 2 Time Arrival", "Inbound 2 Flight Number"
        };
        writer.writeNext(header);
    }

    public static void writeFlightDataRow(CSVWriter writer, FlightCombination combination) {
        FlightSegment[] outbound = combination.getOutboundSegments();
        FlightSegment[] inbound = combination.getInboundSegments();

        String[] outbound1Details = getFlightSegmentDetails(outbound, 0);
        String[] outbound2Details = getFlightSegmentDetails(outbound, 1);
        String[] inbound1Details = getFlightSegmentDetails(inbound, 0);
        String[] inbound2Details = getFlightSegmentDetails(inbound, 1);

        String[] data = {
                String.valueOf(combination.getPrice()),
                String.format("%.2f", combination.getTotalTax()),

                outbound1Details[0], outbound1Details[1], outbound1Details[2], outbound1Details[3], outbound1Details[4],
                outbound2Details[0], outbound2Details[1], outbound2Details[2], outbound2Details[3], outbound2Details[4], // if exists
                inbound1Details[0], inbound1Details[1], inbound1Details[2], inbound1Details[3], inbound1Details[4],
                inbound2Details[0], inbound2Details[1], inbound2Details[2], inbound2Details[3], inbound2Details[4] // if exists
        };

        writer.writeNext(data);
    }

    public static String[] getFlightSegmentDetails(FlightSegment[] segments, int index) {
        if (segments.length > index) {
            return new String[] {
                    segments[index].getAirportDeparture(),
                    segments[index].getAirportArrival(),
                    segments[index].getDepartureTime(),
                    segments[index].getArrivalTime(),
                    segments[index].getFlightNumber()
            };
        } else {
            return new String[] {"", "", "", "", ""};
        }
    }

    public static void saveToCSV(List<FlightCombination> flightCombinations) {
        writeFlightCombinationsToCSV(FLIGHTS_CSV_PATH, flightCombinations);
    }

    public static void saveCheapestFlightToCSV(List<FlightCombination> cheapestFlights) {
        writeFlightCombinationsToCSV(CHEAPEST_FLIGHTS_CSV_PATH, cheapestFlights);
    }
}