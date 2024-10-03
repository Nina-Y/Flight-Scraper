package com.dataextraction.IO;

import java.util.Scanner;

public class Reader {

    public static String[] getUserInputs() {
        Scanner scanner = new Scanner(System.in);
        displayUnavailableRoutes();

        String airportDeparture = getInput(scanner, "Enter Departure airport Code (eg. MAD, JFK, CPH): ");
        String airportArrival = getInput(scanner, "Enter Arrival airport Code (eg. AUH, FUE, MAD): ");
        String dateDeparture = getInput(scanner, "Enter Outbound departure Date (YYYY-MM-DD): ");
        String dateReturn = getInput(scanner, "Enter Inbound departure Date (YYYY-MM-DD): ");
        String maxPrice = getInput(scanner, "Enter maximum Price (or press ENTER to skip): ");
        String maxTaxes = getInput(scanner, "Enter maximum total Taxes (or press ENTER to skip): ");
        String directOutbound = getInput(scanner, "Enter 'd' for Direct Outbound flight (or press ENTER to skip): ");
        String directInbound = getInput(scanner, "Enter 'd' for Direct Inbound flight (or press ENTER to skip): ");
        return new String[] {airportDeparture, airportArrival, dateDeparture, dateReturn, maxPrice, maxTaxes, directOutbound, directInbound};
    }
    public static void displayUnavailableRoutes() {
        System.out.println("""
                Note: 
                The following routes are not available: JFK-MAD, CPH-AUH, CPH-FUE.
                Available routes and connection airports: 
                - MAD-AUH (direct), 
                - MAD-FUE (TFN for outbound, LPA for inbound), 
                - JFK-AUH (MAD for both directions), 
                - CPH-MAD (AMS for both directions), 
                - JFK-FUE (MAD, LPA - 2 connections in one direction, the program skips the results).
                """);
    }

    private static String getInput(Scanner scanner, String message) {
        System.out.println(message);
        return scanner.nextLine().toUpperCase();
    }
}