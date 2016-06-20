package com.gorilla.calculation.impl;

import com.gorilla.dao.PriceModifierDao;
import com.gorilla.util.ConfigurationService;
import com.gorilla.domain.Instrument;
import com.gorilla.calculation.CalculationService;
import com.gorilla.calculation.StatisticsCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CalculationServiceImpl implements CalculationService {

    @Autowired
    private ConfigurationService config;

    @Autowired
    private PriceModifierDao priceModifierDao;

    private Map<String, StatisticsCalculator> activeCalculators = new HashMap<>();

    @Override
    public void accept(Instrument instrument) {

        StatisticsCalculator calculatorForInstrument = getCalculatorForInstrument(instrument.getName());

        BigDecimal multiplier = priceModifierDao.getByInstrumentName(instrument.getName())
                .map(x -> new BigDecimal(x.getMultiplier()))
                .orElse(BigDecimal.ONE);

        instrument = new Instrument(instrument.getName(), instrument.getDate(), instrument.getPrice().multiply(multiplier));

        calculatorForInstrument.accept(instrument);
    }

    @Override
    public Map<String, String> getStatisticsAsMap() {
        return activeCalculators.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().getStatisticsAsString()));
    }

    private StatisticsCalculator getCalculatorForInstrument(String instrumentName) {
        if(!activeCalculators.containsKey(instrumentName)) {
            assignCalculatorToInstrument(instrumentName);
        }
        return activeCalculators.get(instrumentName);
    }

    private void assignCalculatorToInstrument(String instrumentName) {
        StatisticsCalculator statisticsCalculator = config.getCalculatorForInstrument(instrumentName)
                .orElse(config.getDefaultCalculator());
        activeCalculators.put(instrumentName, statisticsCalculator);
    }

}
