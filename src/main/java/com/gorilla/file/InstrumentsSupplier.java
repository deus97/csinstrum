package com.gorilla.file;

import com.gorilla.util.ConfigurationService;
import com.gorilla.domain.Instrument;
import com.gorilla.util.InstrumentParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.Optional;
import java.util.stream.Stream;

@Component
public class InstrumentsSupplier implements Iterable<Instrument> {

    private final Logger LOG = LoggerFactory.getLogger(getClass());

    @Autowired
    private ConfigurationService config;

    @Autowired
    private InstrumentParser parser;

    private Stream<Instrument> instrumentStream;

    @PreDestroy
    private void destroy() {
        if(instrumentStream==null) return;
        instrumentStream.close(); //need to explicitly close a stream whose source is file
    }

    @Override
    public Iterator<Instrument> iterator() {
        createStreamOfInstrumentsFromFile(config.getInputFileName());
        return instrumentStream.iterator();
    }

    private void createStreamOfInstrumentsFromFile(String fileName) {
        if(instrumentStream != null) instrumentStream.close();

        try {
            this.instrumentStream = Files.lines(Paths.get(fileName))
                    .map(parser::parseInstrument)
                    .filter(Optional::isPresent)
                    .map(Optional::get);
        } catch (IOException e) {
            LOG.error("Bad input file: " + e);
            throw new RuntimeException(e);
        }
    }

}
