package com.gorilla.calculation.impl;

import com.gorilla.dao.PriceModifierDao;
import com.gorilla.util.ConfigurationService;
import com.gorilla.domain.Instrument;
import com.gorilla.calculation.StatisticsCalculator;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

@Ignore
@RunWith(MockitoJUnitRunner.class)
public class CalculationServiceImplTest {

    private static final String INSTRUMENT_NAME_1 = "INSTRUMENT1";
    private static final String INSTRUMENT_NAME_2 = "INSTRUMENT2";

    @Mock
    private PriceModifierDao priceModifierDao;

    @Mock
    private ConfigurationService config;

    @InjectMocks
    private CalculationServiceImpl unit;

    private StatisticsCalculator meanCalculatorMock;
    private StatisticsCalculator defaultCalculatorMock;

    @Before
    public void setUp() throws Exception {
        meanCalculatorMock = mock(MeanPriceCalculator.class);
        defaultCalculatorMock = mock(FewNewestSummingCalculator.class);

        doReturn(Optional.of(meanCalculatorMock)).when(config).getCalculatorForInstrument(INSTRUMENT_NAME_1);
        doReturn(Optional.empty()).when(config).getCalculatorForInstrument(INSTRUMENT_NAME_2);
        doReturn(defaultCalculatorMock).when(config).getDefaultCalculator();

        doReturn(Optional.empty()).when(priceModifierDao).getByInstrumentName(anyString());
    }

    @Test
    public void testAccept() throws Exception {
        Instrument instrument = new Instrument("INSTRUMENT1", LocalDate.of(2016, Month.JUNE, 01), new BigDecimal("2.3"));

        unit.accept(instrument);

        verify(meanCalculatorMock).accept(any(Instrument.class));
    }

    @Test
    public void testShouldNotThrowIfUnmappedInstrument() throws Exception {
        Instrument instrument = new Instrument("INSTRUMENT2", LocalDate.of(2016, Month.JUNE, 01), new BigDecimal("2.3"));

        unit.accept(instrument);

        verify(defaultCalculatorMock).accept(any(Instrument.class));
    }

    @Test
    public void shouldReturnStatisticsAsMap() throws Exception {
        Instrument instrument = new Instrument("INSTRUMENT1", LocalDate.of(2016, Month.JUNE, 01), new BigDecimal("2.3"));
        doReturn("Mean price = " + instrument.getPrice()).when(meanCalculatorMock).getStatisticsAsString();

        unit.accept(instrument);

        assertTrue(unit.getStatisticsAsMap().containsKey(INSTRUMENT_NAME_1));
        assertTrue(unit.getStatisticsAsMap().get(INSTRUMENT_NAME_1).contains(instrument.getPrice().toString()));
    }
}