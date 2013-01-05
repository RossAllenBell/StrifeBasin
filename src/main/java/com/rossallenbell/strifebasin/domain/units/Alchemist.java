package com.rossallenbell.strifebasin.domain.units;

import com.rossallenbell.strifebasin.domain.Player;

public class Alchemist extends Unit {
    
    public Alchemist(Player owner) {
        super(owner);
    }

    private static final long serialVersionUID = 1L;

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
