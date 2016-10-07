package com.gorilla.calculation.impl;

import com.gorilla.calculation.StatisticsCalculator;
import com.gorilla.domain.Instrument;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Anton Lukashchuk on 07.10.16.
 */
public class DateOfMaxPriceFinderCalculatorTest {

    private StatisticsCalculator unit = new DateOfMaxPriceFinderCalculator();

    @Test
    public void testAccept() throws Exception {
        //given
        List<Instrument> instruments = new ArrayList<>();
        instruments.add(new Instrument("1", LocalDate.parse("2015-11-01"), new BigDecimal("1")));
        instruments.add(new Instrument("2", LocalDate.parse("2015-11-02"), new BigDecimal("2")));
        instruments.add(new Instrument("4", LocalDate.parse("2015-11-04"), new BigDecimal("3.1415")));
        instruments.add(new Instrument("3", LocalDate.parse("2015-11-03"), new BigDecimal("3")));

        //when
        instruments.stream().forEach(unit::accept);

        //then
        assertTrue(unit.getStatisticsAsString().contains("3.1415"));
        assertTrue(unit.getStatisticsAsString().contains("2015-11-04"));
    }

}