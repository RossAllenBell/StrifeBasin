package com.rossallenbell.strifebasin.domain.units;

public class Spearman extends Unit {
    
    private static final long serialVersionUID = 1L;

    @Override
    public int health() {
        return 10;
    }
    
    @Override
    public double getDamage() {
        return 2;
    }
    
}
