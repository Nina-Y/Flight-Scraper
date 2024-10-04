Flight Scraper

Overview

The Flight Scraper is a Java application that fetches flight data from a public API, filters it based on user-defined parameters, and saves the results in CSV files.

Features
- Fetch flight data based on user inputs.
- Filter flights by maximum price and taxes.
- Save all valid flight combinations to flights.csv.
- Save the cheapest flight(s) to cheapest_flights.csv.
- Handles up to 1 flight connection.
- Option to filter by direct flights only

Program Structure

Main Class: FlightScraper
- Handles the overall program flow.
- Manages user input, API calls, flight data extraction and filtering.

Key methods:
- scrape(): Main method to start the flight scraping process.
- buildApiUrl(): Builds the API request URL.
- fetchFlightData(): Retrieves the flight data from the API.
- processFlightData(): Handles the main logic for processing the flight data.
- findCheapestFlights(): Finds all flight combinations with the lowest price.

Processing Class: FlightProcessor
- handles flight processing logic

Data Models
- FlightCombination class:
Represents a flight with its price, taxes, and segments (outbound/inbound).
- FlightSegment class:
Represents individual flight details (eg. departure/ arrival airports and times).

Modularized Input and Output Handling:
- Reader class handles all user input and displays unavailable routes.
- Writer class handles all CSV writing operations.

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
- Enter max price or skip: 700
- Enter max taxes or skip: 30
- Enter 'd' for direct Outbound flight or skip: d
- Enter 'd' for direct Inbound flight or skip: d

Error Handling
- Invalid API responses are handled with an error message.
- Invalid inputs (eg. non-numeric price/ taxes) default to no limit.

Requirements
- Java 11 or higher.
- OpenCSV library for handling CSV operations. 
- Jackson library for JSON parsing.
