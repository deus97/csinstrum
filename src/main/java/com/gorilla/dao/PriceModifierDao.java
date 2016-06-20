package com.gorilla.dao;

import com.gorilla.domain.PriceModifier;

import java.util.Optional;

public interface PriceModifierDao {

    Optional<PriceModifier> getByInstrumentName(String name);
}
