package com.gorilla.calculation.impl;

import com.gorilla.domain.Instrument;
import org.springframework.beans.factory.annotation.Required;

import java.time.LocalDate;
import java.time.temporal.TemporalUnit;

/**
 * Calculates mean price of an instrument within the given date range
 */
public class DateLimitedMeanPriceCalculator extends MeanPriceCalculator {

    private LocalDate from;

    private LocalDate to;

    public void init() {
        if(from.isAfter(to)) {
            throw new IllegalArgumentException("From date is after to date.");
        }

        //include limits i.e. change (from; to) -> [from; to]
        from = from.minusDays(1);
        to = to.plusDays(1);
    }

    @Override
    public void accept(Instrument instrument) {
        if(instrument.getDate().isAfter(from) && instrument.getDate().isBefore(to)) {
            super.accept(instrument);
        }
    }

    @Override
    public String getStatisticsAsString() {
        return "date limited (from " + from + " to " + to + ") " + super.getStatisticsAsString();
    }

    @Required
    public void setFrom(LocalDate from) {
        this.from = from;
    }

    @Required
    public void setTo(LocalDate to) {
        this.to = to;
    }
}
