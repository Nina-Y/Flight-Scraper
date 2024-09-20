Flight Scraper

Overview

The Flight Scraper program is a Java application designed to fetch flight data from a public API, filter it based on user-defined parameters, and save the results in a CSV file. It also identifies and saves the cheapest flight from the extracted data.

Features
- Fetch flight data based on user inputs.
- Filter flights by maximum price and taxes.
- Save all valid flight combinations to flights.csv.
- Save the cheapest flight to cheapest_flight.csv.
- Handles up to 1 flight connection.

Program Structure

Main Class

FlightScraper.java
- Handles the overall program flow.
- Manages user input, API calls, flight data extraction, filtering, and CSV file writing.

Key methods:
- scrape(): Main method to start the flight scraping process.
- getUserInputs(): Gathers all input from the user.
- processFlightData(): Extracts and processes flight data.
- saveToCSV(): Saves all valid flight combinations to flights.csv.
- saveCheapestFlightToCSV(): Saves the cheapest flight to cheapest_flight.csv.

Data Models
- FlightCombination class:
Represents a flight with its price, taxes, and segments (outbound/inbound).
- FlightSegment class:
Represents individual flight details (eg. departure/ arrival airports and times).

Utility Methods
- extractFlightData(): Parses API response and filters flights based on user-defined limits.
- fetchFlightData(): Retrieves flight data from the API.
- buildApiUrl(): Builds the API request URL.
- parseBigDecimal(): Parses user input for price/tax into BigDecimal.

Usage

Inputs
- Departure/Arrival Airports: IATA codes (eg. MAD, JFK).
- Outbound/Inbound Dates: Dates in YYYY-MM-DD.
- Max Price/Taxes: Optional (skip by pressing Enter).

Example:
- Enter departure airport code: MAD
- Enter arrival airport code: FUE
- Enter outbound date: 2024-10-09
- Enter inbound date: 2024-10-16
- Enter max price: 700
- Enter max taxes: 30

Error Handling
- Invalid API responses are handled with an error message.
- Invalid inputs (eg. non-numeric price/ taxes) default to no limit.

Requirements
- Java 11 or higher.
