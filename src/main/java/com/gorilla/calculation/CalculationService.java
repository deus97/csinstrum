package com.gorilla.calculation;


import com.gorilla.domain.Instrument;

import java.util.Map;

public interface CalculationService {

    /**
     * Include given instrument into statistics
     * @param instrument
     */
    void accept(Instrument instrument);

    /**
     * Generate full statistics and return it as Map.
     * @return Map where Key is an instrument name, value is a string representation of statistics for this instrument
     */
    Map<String, String> getStatisticsAsMap();

}
