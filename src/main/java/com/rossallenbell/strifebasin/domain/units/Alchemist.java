package com.rossallenbell.strifebasin.domain.units;

import com.rossallenbell.strifebasin.domain.Me;

public class Alchemist extends PlayerUnit {
    
    public Alchemist(Me owner) {
        super(owner);
    }

    @Override
    public int getMaxHealth() {
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
