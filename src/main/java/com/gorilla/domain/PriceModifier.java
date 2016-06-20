package com.gorilla.domain;

/**
 * Created by Anton Lukashchuk on 07.06.16.
 */
public class PriceModifier {

    private int id;
    private String name;
    private String multiplier;

    public PriceModifier(int id, String name, String multiplier) {
        this.id = id;
        this.name = name;
        this.multiplier = multiplier;
    }

    public String getName() {
        return name;
    }

    public String getMultiplier() {
        return multiplier;
    }
}
