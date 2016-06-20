package com.gorilla.calculation.impl;

import com.gorilla.domain.Instrument;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class FewNewestSummingCalculatorTest {

    private FewNewestSummingCalculator unit;

    private List<Instrument> instruments;

    @Before
    public void setUp() throws Exception {
        unit = new FewNewestSummingCalculator();
        unit.setFew(3);
        unit.init();

        instruments = new ArrayList<>();
        instruments.add(new Instrument("1", LocalDate.parse("2015-11-01"), new BigDecimal("1")));
        instruments.add(new Instrument("2", LocalDate.parse("2015-11-02"), new BigDecimal("2")));
        instruments.add(new Instrument("3", LocalDate.parse("2015-11-03"), new BigDecimal("3")));
        instruments.add(new Instrument("4", LocalDate.parse("2015-11-04"), new BigDecimal("4")));
        instruments.add(new Instrument("5", LocalDate.parse("2015-11-05"), new BigDecimal("5")));
        instruments.add(new Instrument("6", LocalDate.parse("2015-11-06"), new BigDecimal("6")));
        instruments.add(new Instrument("7", LocalDate.parse("2015-11-07"), new BigDecimal("7")));
        instruments.add(new Instrument("8", LocalDate.parse("2015-11-08"), new BigDecimal("8")));
        instruments.add(new Instrument("9", LocalDate.parse("2015-11-09"), new BigDecimal("9")));
        instruments.add(new Instrument("10", LocalDate.parse("2015-11-10"), new BigDecimal("10")));
        instruments.add(new Instrument("11", LocalDate.parse("2015-11-11"), new BigDecimal("11")));
        instruments.add(new Instrument("12", LocalDate.parse("2015-11-12"), new BigDecimal("12")));
        instruments.add(new Instrument("13", LocalDate.parse("2015-11-13"), new BigDecimal("13")));
    }

    @Test
    public void shouldSumFewNewestPrices() throws Exception {
        //repeat many times with shuffled input, result must always be the same
        for(int i=0; i<10; i++) {
            unit.init();
            Collections.shuffle(instruments);

            instruments.stream().forEach(x -> unit.accept(x));

            //sum of 3 newest instruments 11+12+13=36
            assertTrue(unit.getStatisticsAsString().contains("36"));
        }
    }
}