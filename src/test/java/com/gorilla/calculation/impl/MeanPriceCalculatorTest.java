package com.gorilla.calculation.impl;

import com.gorilla.domain.Instrument;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class MeanPriceCalculatorTest {

    private MeanPriceCalculator unit;

    private List<Instrument> data;

    @Before
    public void setUp() throws Exception {
        unit = new MeanPriceCalculator();

        data = new ArrayList<>();
        data.add(new Instrument("INSTRUMENT1", LocalDate.of(2016, Month.JUNE, 1), new BigDecimal("2.3")));
        data.add(new Instrument("INSTRUMENT1", LocalDate.of(2016, Month.JUNE, 1), new BigDecimal("2.4")));
        data.add(new Instrument("INSTRUMENT1", LocalDate.of(2016, Month.JUNE, 1), new BigDecimal("2.5")));
    }

    @Test
    public void testAccept() throws Exception {
        for(Instrument instr : data) {
            unit.accept(instr);
        }

        assertTrue(unit.getStatisticsAsString().contains("2.4"));
    }

}