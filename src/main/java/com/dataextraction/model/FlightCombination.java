package com.dataextraction.model;

import java.math.BigDecimal;

public class FlightCombination {
    private final BigDecimal price;
    private final BigDecimal totalTaxes;
    private final FlightSegment[] outboundSegments;
    private final FlightSegment[] inboundSegments;

    public FlightCombination(BigDecimal price, BigDecimal totalTaxes,
                             FlightSegment[] outboundSegments, FlightSegment[] inboundSegments) {
        this.price = price;
        this.totalTaxes = totalTaxes;
        this.outboundSegments = outboundSegments;
        this.inboundSegments = inboundSegments;
    }

    //<editor-fold desc="Getters">
    public BigDecimal getPrice() {
        return price;
    }

    public BigDecimal getTotalTax() {
        return totalTaxes;
    }

    public FlightSegment[] getOutboundSegments() {
        return outboundSegments;
    }

    public FlightSegment[] getInboundSegments() {
        return inboundSegments;
    }
    //</editor-fold>
}