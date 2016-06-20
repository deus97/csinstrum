package com.gorilla.calculation.impl;

import com.gorilla.calculation.StatisticsCalculator;
import com.gorilla.domain.Instrument;

import java.math.BigDecimal;
import java.time.LocalDate;

public class DateOfMaxPriceFinderCalculator implements StatisticsCalculator {

    private Instrument mostExpensive = new Instrument("", LocalDate.now(), BigDecimal.ZERO);

    @Override
    public void accept(Instrument instrument) {
        if(mostExpensive.getPrice().compareTo(instrument.getPrice()) < 0) {
            mostExpensive = instrument;
        }
    }

    @Override
    public String getStatisticsAsString() {
        return "max price was detected on: " + mostExpensive.getDate() + ", price (multiplied by modifier) = " + mostExpensive.getPrice();
    }
}
