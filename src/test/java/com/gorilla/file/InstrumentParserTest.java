package com.gorilla.file;

import com.gorilla.domain.Instrument;
import com.gorilla.util.ConfigurationService;
import com.gorilla.util.InstrumentParser;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.doReturn;

/**
 * Created by Anton Lukashchuk on 07.06.16.
 */
@RunWith(MockitoJUnitRunner.class)
public class InstrumentParserTest {

    @Mock
    private ConfigurationService config;

    @InjectMocks
    private InstrumentParser unit;

    @Before
    public void setUp() throws Exception {
        doReturn("dd-MMM-yyyy").when(config).getDateFormatStr();
        doReturn(",").when(config).getFiledSeparator();

        unit.init();
    }

    @Test
    public void testParseInstrument() throws Exception {
        String instrumentOneStr =    "INSTRUMENT1,15-Mar-1996,2.572";
        String instrumentTwoStr =    "INSTRUMENT2,16-Mar-1997,2.672";

        Optional<Instrument> instrumentOne = unit.parseInstrument(instrumentOneStr);
        Optional<Instrument> instrumentTwo = unit.parseInstrument(instrumentTwoStr);

        assertTrue(instrumentOne.isPresent());
        assertTrue(instrumentTwo.isPresent());
        assertEquals("INSTRUMENT1", instrumentOne.orElse(null).getName());
        assertEquals("INSTRUMENT2", instrumentTwo.orElse(null).getName());
        assertEquals(LocalDate.of(1996, Month.MARCH, 15), instrumentOne.orElse(null).getDate());
        assertEquals(LocalDate.of(1997, Month.MARCH, 16), instrumentTwo.orElse(null).getDate());
        assertEquals(new BigDecimal("2.572"), instrumentOne.orElse(null).getPrice());
        assertEquals(new BigDecimal("2.672"), instrumentTwo.orElse(null).getPrice());


    }
}