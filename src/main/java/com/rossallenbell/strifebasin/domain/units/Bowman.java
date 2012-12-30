package com.rossallenbell.strifebasin.domain.units;

public class Bowman extends Unit {
    
    private static final long serialVersionUID = 1L;

    @Override
    public int health() {
        return 5;
    }
    
    @Override
    public double getDamage() {
        return 1;
    }
    
    @Override
    public double getRange(){
        return 5;
    }
    
}
