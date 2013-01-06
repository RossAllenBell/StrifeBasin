package com.rossallenbell.strifebasin.connection.domain;

import com.rossallenbell.strifebasin.domain.units.Unit;

public class NetworkUnit extends NetworkAsset {

    private static final long serialVersionUID = 1L;
    
    private final double damage;

    public NetworkUnit(Unit originalUnit) {
        super(originalUnit);
        damage = originalUnit.getDamage();
    }
    
    public double getDamage() {
        return damage;
    }
    
}
