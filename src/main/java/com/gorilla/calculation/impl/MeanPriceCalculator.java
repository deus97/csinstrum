package com.gorilla.calculation.impl;

import com.gorilla.domain.Instrument;
import com.gorilla.calculation.StatisticsCalculator;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class MeanPriceCalculator implements StatisticsCalculator {

    private BigDecimal mean = BigDecimal.ZERO;

    private BigDecimal numberOfValues = BigDecimal.ZERO;

    @Override
    public void accept(Instrument instrument) {

        BigDecimal price = instrument.getPrice();

        BigDecimal incrementedNumberOfValues = numberOfValues.add(BigDecimal.ONE);

        //mean = (mean*numberOfValues+price)/(++numberOfValues)
        mean = ( (mean.multiply(numberOfValues)).add(price) ).divide(incrementedNumberOfValues, 4, RoundingMode.HALF_UP);

        numberOfValues = incrementedNumberOfValues;
    }

    @Override
    public String getStatisticsAsString() {
        return "mean price is: " + mean;
    }


}
