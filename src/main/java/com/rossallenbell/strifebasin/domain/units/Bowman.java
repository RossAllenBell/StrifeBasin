package com.rossallenbell.strifebasin.domain.units;

import com.rossallenbell.strifebasin.domain.Player;

public class Bowman extends Unit {
    
    public Bowman(Player owner) {
        super(owner);
    }

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
