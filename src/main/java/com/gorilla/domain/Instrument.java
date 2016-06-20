package com.gorilla.domain;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Instrument {

    private String name;

    private LocalDate date;

    private BigDecimal price;

    public Instrument(String name, LocalDate date, BigDecimal price) {
        this.name = name;
        this.date = date;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public LocalDate getDate() {
        return date;
    }

    public BigDecimal getPrice() {
        return price;
    }



}
