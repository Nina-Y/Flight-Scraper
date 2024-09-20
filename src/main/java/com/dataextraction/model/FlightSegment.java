package com.dataextraction.model;

public class FlightSegment {
    private final String airportDeparture;
    private final String airportArrival;
    private final String departureTime;
    private final String arrivalTime;
    private final String flightNumber;

    public FlightSegment(String airportDeparture, String airportArrival, String departureTime, String arrivalTime, String flightNumber) {
        this.airportDeparture = airportDeparture;
        this.airportArrival = airportArrival;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.flightNumber = flightNumber;
    }

    //<editor-fold desc="Getters">
    public String getAirportDeparture() {
        return airportDeparture;
    }

    public String getAirportArrival() {
        return airportArrival;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public String getFlightNumber() {
        return flightNumber;
    }
    //</editor-fold>
}

