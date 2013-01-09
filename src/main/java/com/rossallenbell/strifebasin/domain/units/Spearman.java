package com.rossallenbell.strifebasin.domain.units;

import com.rossallenbell.strifebasin.domain.Me;

public class Spearman extends PlayerUnit {
    
    public Spearman(Me owner) {
        super(owner);
    }

    @Override
    public int getMaxHealth() {
        return 10;
    }
    
    @Override
    public double getDamage() {
        return 2;
    }
    
}
