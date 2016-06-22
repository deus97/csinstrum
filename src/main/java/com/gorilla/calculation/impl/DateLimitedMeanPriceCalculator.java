package com.gorilla.calculation.impl;

import com.gorilla.domain.Instrument;
import org.springframework.beans.factory.annotation.Required;

import java.time.LocalDate;

public class DateLimitedMeanPriceCalculator extends MeanPriceCalculator {

    private LocalDate from;

    private LocalDate to;

    @Override
    public synchronized void accept(Instrument instrument) {
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
