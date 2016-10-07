package com.gorilla.calculation.impl;

import com.gorilla.calculation.StatisticsCalculator;
import com.gorilla.domain.Instrument;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Anton Lukashchuk on 07.10.16.
 */
public class DateLimitedMeanPriceCalculatorTest {

    private DateLimitedMeanPriceCalculator unit = new DateLimitedMeanPriceCalculator();
    private List<Instrument> data = new ArrayList<>();

    @Before
    public void setUp() throws Exception {
        data.add(new Instrument("INSTRUMENT1", LocalDate.of(2016, Month.JUNE, 1), new BigDecimal("2.3")));
        data.add(new Instrument("INSTRUMENT1", LocalDate.of(2016, Month.JUNE, 2), new BigDecimal("2.4")));
        data.add(new Instrument("INSTRUMENT1", LocalDate.of(2016, Month.JUNE, 3), new BigDecimal("2.5")));
        data.add(new Instrument("INSTRUMENT1", LocalDate.of(2016, Month.JUNE, 4), new BigDecimal("2.6")));
        data.add(new Instrument("INSTRUMENT1", LocalDate.of(2016, Month.JUNE, 5), new BigDecimal("2.7")));
    }


    @Test
    public void testAccept() throws Exception {
        //given
        unit.setFrom(LocalDate.of(2016, Month.JUNE, 1));
        unit.setTo(LocalDate.of(2016, Month.JUNE, 3));

        //when
        data.stream().forEach(unit::accept);

        //then
        assertTrue(unit.getStatisticsAsString().contains("2.4"));
    }
}