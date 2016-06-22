package com.gorilla.calculation.impl;

import com.gorilla.dao.PriceModifierDao;
import com.gorilla.util.ConfigurationService;
import com.gorilla.domain.Instrument;
import com.gorilla.calculation.CalculationService;
import com.gorilla.calculation.StatisticsCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;
import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Service to calculate statistics for instruments.
 */
@Service
public class CalculationServiceImpl implements CalculationService {

    @Autowired
    private ConfigurationService config;

    @Autowired
    private PriceModifierDao priceModifierDao;

    @Autowired
    private ExecutorService executorService;

    private Map<String, StatisticsCalculator> activeCalculators = new ConcurrentHashMap<>();

    @PreDestroy
    private void destroy() {
        if(executorService==null) return;

        executorService.shutdown();
        try {
            if(!executorService.awaitTermination(5, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
        }
    }

    @Override
    public void accept(final Instrument instrument) {

        executorService.submit(() -> {
            StatisticsCalculator calculatorForInstrument = getCalculatorForInstrument(instrument.getName());

            final BigDecimal multiplier = priceModifierDao.getByInstrumentName(instrument.getName())
                    .map(x -> new BigDecimal(x.getMultiplier()))
                    .orElse(BigDecimal.ONE);

            final Instrument instrumentWithMultiplier = new Instrument(instrument.getName(), instrument.getDate(), instrument.getPrice().multiply(multiplier));

            calculatorForInstrument.accept(instrumentWithMultiplier);
        });

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
