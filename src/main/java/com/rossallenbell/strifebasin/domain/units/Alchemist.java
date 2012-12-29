package com.rossallenbell.strifebasin.domain.units;

public class Alchemist extends Unit {

    @Override
    public int health() {
        return 10;
    }
    
    @Override
    public double getDamage() {
        return 3;
    }
    
    @Override
    public double getRange(){
        return 5;
    }
    
}
