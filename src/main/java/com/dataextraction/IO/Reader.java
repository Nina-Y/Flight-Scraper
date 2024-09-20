package com.dataextraction.IO;

import java.math.BigDecimal;
import java.util.Scanner;

public class Reader {

    public static String[] getUserInputs() {
        Scanner scanner = new Scanner(System.in);
        displayUnavailableRoutes();

        String airportDeparture = getInput(scanner, "Enter departure airport code (eg. MAD, JFK, CPH): ");
        String airportArrival = getInput(scanner, "Enter arrival airport code (eg. AUH, FUE, MAD): ");
        String dateDeparture = getInput(scanner, "Enter outbound departure date (YYYY-MM-DD): ");
        String dateReturn = getInput(scanner, "Enter inbound departure date (YYYY-MM-DD): ");
        String maxPrice = getInput(scanner, "Enter maximum price (press Enter to skip): ");
        String maxTaxes = getInput(scanner, "Enter maximum total taxes (press Enter to skip): ");

        return new String[]{airportDeparture, airportArrival, dateDeparture, dateReturn, maxPrice, maxTaxes};
    }
    public static void displayUnavailableRoutes() {
        System.out.println("Note: The following routes are not available: JFK-MAD, CPH-AUH, CPH-FUE.");
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

    private static String getInput(Scanner scanner, String message) {
        System.out.println(message);
        return scanner.nextLine();
    }
}
