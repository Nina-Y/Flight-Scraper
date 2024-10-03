package com.dataextraction;

import com.dataextraction.model.FlightCombination;
import com.dataextraction.model.FlightSegment;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;
import java.util.logging.Logger;
import static com.dataextraction.FlightProcessor.*;
import static com.dataextraction.IO.Reader.getUserInputs;
import static com.dataextraction.JsonConstants.*;

public class FlightScraper {

    static final String BASE_API_URL = "http://homeworktask.infare.lt/search.php?";
    public static final String FLIGHTS_CSV_PATH = "src/main/resources/flights.csv";
    public static final String CHEAPEST_FLIGHTS_CSV_PATH = "src/main/resources/cheapest_flights.csv";
    private static final Logger logger = Logger.getLogger(FlightScraper.class.getName());

    public static void scrape() {

        logger.info("Flight Scraper application started");

        String[] userInputs = getUserInputs();

        String airportDeparture = userInputs[0];
        String airportArrival = userInputs[1];
        String dateDeparture = userInputs[2];
        String dateReturn = userInputs[3];
        BigDecimal maxPrice = parseBigDecimalOrSkip(userInputs[4]);
        BigDecimal maxTaxes = parseBigDecimalOrSkip(userInputs[5]);
        String directOutbound = userInputs[6];
        String directInbound = userInputs[6];

        String apiUrl = buildApiUrl(airportDeparture, airportArrival, dateDeparture, dateReturn);

        String jsonData = fetchFlightData(apiUrl);

        if (isValidJson(jsonData)) {
            processFlightData(jsonData, maxPrice, maxTaxes,directOutbound, directInbound);
        } else {
            System.out.println("Invalid response from API: " + jsonData);
        }
    }

    public static BigDecimal parseBigDecimalOrSkip(String input) {
        if (input.isEmpty()) {
            return BigDecimal.valueOf(Double.MAX_VALUE);
        }
        try {
            return new BigDecimal(input);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input, using no limit.");
            return BigDecimal.valueOf(Double.MAX_VALUE);
        }
    }

    public static String buildApiUrl(String airportDeparture, String airportArrival, String dateDeparture, String dateReturn) {
        return BASE_API_URL + "from=" + airportDeparture + "&to=" + airportArrival +
                "&depart=" + dateDeparture + "&return=" + dateReturn;
    }

    public static String fetchFlightData(String apiUrl) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl)) // Specifies the URI for the HTTP request by converting the apiUrl string into a URI object.
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

    private static boolean isValidJson(String jsonData) {
        return jsonData != null && jsonData.trim().startsWith("{");
    }

    public static List<FlightCombination> extractFlightData(String jsonData, BigDecimal maxPrice, BigDecimal maxTaxes, String directOutbound, String directInbound) {

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = parseJson(jsonData, objectMapper);

        if (rootNode == null) {
            return List.of();
        }

        Map<Integer, List<JsonNode>> outboundFlights = new HashMap<>();
        Map<Integer, List<JsonNode>> inboundFlights = new HashMap<>();
        processJourneyDataByDirection(rootNode, outboundFlights, inboundFlights);

        return createFlightCombinations(rootNode, outboundFlights, inboundFlights, maxPrice, maxTaxes, directOutbound, directInbound);
    }

    private static JsonNode parseJson(String jsonData, ObjectMapper objectMapper) {
        try {
            return objectMapper.readTree(jsonData);
        } catch (JsonProcessingException e) {
            System.err.println("Error parsing JSON data: " + e.getMessage());
            return null;
        }
    }

    private static void processJourneyDataByDirection(JsonNode rootNode,
                                           Map<Integer, List<JsonNode>> outboundFlights,
                                           Map<Integer, List<JsonNode>> inboundFlights) {
        JsonNode journeys = rootNode.path(BODY).path(DATA).path(JOURNEYS);
        for (JsonNode journey : journeys) {
            int recommendationId = journey.path(RECOMMENDATION_ID).asInt();
            String direction = journey.path(DIRECTION).asText();

            if (direction.equals(I)) {

                outboundFlights.computeIfAbsent(recommendationId, k -> new ArrayList<>()).add(journey);
            } else if (direction.equals(V)) {
                inboundFlights.computeIfAbsent(recommendationId, k -> new ArrayList<>()).add(journey);
            }
        }
    }

    private static List<FlightCombination> createFlightCombinations(JsonNode rootNode,
                                                                    Map<Integer, List<JsonNode>> outboundFlights,
                                                                    Map<Integer, List<JsonNode>> inboundFlights,
                                                                    BigDecimal maxPrice, BigDecimal maxTaxes,
                                                                    String directOutbound, String directInbound) {
        List<FlightCombination> flightCombinations = new ArrayList<>();

        for (Integer recommendationId : outboundFlights.keySet()) {
            if (inboundFlights.containsKey(recommendationId)) {
                List<JsonNode> outboundOptions = outboundFlights.get(recommendationId);
                List<JsonNode> inboundOptions = inboundFlights.get(recommendationId);

                for (JsonNode outbound : outboundOptions) {
                    for (JsonNode inbound : inboundOptions) {

                        BigDecimal twoWayPrice = extractPrice(recommendationId, rootNode);
                        if (twoWayPrice.compareTo(maxPrice) > 0) {
                            continue;
                        }

                        BigDecimal totalTaxes = extractTax(outbound).add(extractTax(inbound));
                        if (totalTaxes.compareTo(maxTaxes) > 0) {
                            continue;
                        }

                        FlightSegment[] outboundSegments = extractFlightSegments(outbound, directOutbound);
                        FlightSegment[] inboundSegments = extractFlightSegments(inbound, directInbound);

                        if (outboundSegments != null && inboundSegments != null) {
                            FlightCombination combination = new FlightCombination(
                                    twoWayPrice,
                                    totalTaxes,
                                    outboundSegments,
                                    inboundSegments
                            );
                            flightCombinations.add(combination);
                        }
                    }
                }
            }
        }
        return flightCombinations;
    }

    // Extract up to 1 connection (2 segments max) for a flight
    public static FlightSegment[] extractFlightSegments(JsonNode flightNode, String directFlight) {
        JsonNode flights = flightNode.path(FLIGHTS);

        if (directFlight.equals("D") && flights.size() > 1) {
            System.out.println("Skipping non-direct flight (more than 1 segment)");
            return null;
        }

        if (flights.size() > 2) {
            System.out.println("Skipping flight with more than 1 connection");
            return null;
        }

        FlightSegment[] segments = new FlightSegment[flights.size()];

        for (int i = 0; i < flights.size(); i++) {
            JsonNode flight = flights.get(i);

            segments[i] = new FlightSegment(
                    flight.path(AIRPORT_DEPARTURE).path(CODE).asText(),
                    flight.path(AIRPORT_ARRIVAL).path(CODE).asText(),
                    flight.path(DATE_DEPARTURE).asText(),
                    flight.path(DATE_ARRIVAL).asText(),
                    flight.path(COMPANY_CODE).asText() + flight.path(NUMBER).asText()
            );
        }
        return segments;
    }

    private static BigDecimal extractPrice(int recommendationId, JsonNode rootNode) {
        JsonNode totalAvailabilities = rootNode.path(BODY).path(DATA).path(TOTAL_AVAILABILITIES);
        for (JsonNode availability : totalAvailabilities) {
            if (availability.path(RECOMMENDATION_ID).asInt() == recommendationId) {
                return new BigDecimal(availability.path(TOTAL).asText());
            }
        }
        return BigDecimal.ZERO;
    }

    private static BigDecimal extractTax(JsonNode flightNode) {
        return BigDecimal.valueOf(flightNode.path(IMPORT_TAX_ADL).asDouble());
    }

    public static List<FlightCombination> findCheapestFlights(List<FlightCombination> flightCombinations) {
        if (flightCombinations.isEmpty()) {
            return null;
        }

        BigDecimal minPrice = flightCombinations.stream()
                .map(FlightCombination::getPrice)
                .min(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);

        return flightCombinations.stream()
                .filter(flightCombination -> flightCombination.getPrice().compareTo(minPrice) ==0)
                .toList();
    }
}