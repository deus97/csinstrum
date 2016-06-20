package com.gorilla.calculation;


import com.gorilla.domain.Instrument;

import java.util.Map;

public interface CalculationService {

    void accept(Instrument instrument);

    /**
     * Key = instrument name, value = string representation of statistics for this instrument
     */
    Map<String, String> getStatisticsAsMap();

}
