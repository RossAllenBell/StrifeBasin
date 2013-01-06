package com.rossallenbell.strifebasin.domain.units;

import com.rossallenbell.strifebasin.domain.Player;

public class Spearman extends Unit {
    
    public Spearman(Player owner) {
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
