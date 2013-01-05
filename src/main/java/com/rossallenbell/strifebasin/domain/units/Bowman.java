package com.rossallenbell.strifebasin.domain.units;

import com.rossallenbell.strifebasin.domain.Player;

public class Bowman extends Unit {
    
    public Bowman(Player owner) {
        super(owner);
    }

    private static final long serialVersionUID = 1L;

    @Override
    public int getMaxHealth() {
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
