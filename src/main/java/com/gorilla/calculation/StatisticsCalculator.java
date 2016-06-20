package com.gorilla.calculation;

import com.gorilla.domain.Instrument;

public interface StatisticsCalculator {

    void accept(Instrument instrument);

    String getStatisticsAsString();

}
