package com.gorilla.util;

import com.gorilla.domain.Instrument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Component
public class InstrumentParser {

    @Autowired
    private ConfigurationService config;

    private DateTimeFormatter dateTimeFormatter;

    @PostConstruct
    public void init(){
        dateTimeFormatter = DateTimeFormatter.ofPattern(config.getDateFormatStr());
    }

    public Optional<Instrument> parseInstrument(String str) {
        try{
            return parseInstrument0(str);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    private Optional<Instrument> parseInstrument0(String str) {
        String[] values = str.split(config.getFiledSeparator());
        Optional<Instrument> result = Optional.empty();
        if(values.length == 3) {
            String name = values[0].trim();
            LocalDate date = LocalDate.parse(values[1].trim(), dateTimeFormatter);
            BigDecimal price = new BigDecimal(values[2].trim());

            if(isWorkingDay(date)) {
                result = Optional.of(new Instrument(name, date, price));
            }
        }

        return result;
    }

    private boolean isWorkingDay(LocalDate date) {
        return (date.getDayOfWeek() != DayOfWeek.SATURDAY) && (date.getDayOfWeek() != DayOfWeek.SUNDAY);
    }

}
