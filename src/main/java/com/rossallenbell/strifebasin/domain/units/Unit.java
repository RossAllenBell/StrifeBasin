package com.rossallenbell.strifebasin.domain.units;

import com.rossallenbell.strifebasin.domain.Asset;
import com.rossallenbell.strifebasin.domain.Player;

public abstract class Unit extends Asset {
    
    private static final long serialVersionUID = 1L;
    
    public static final double DEFAULT_SIZE = 1;
    public static final double DEFAULT_SPEED = 2;
    public static final double DEFAULT_RANGE = 0;
    
    public Unit(Player owner) {
        super(owner);
    }
    
    public double getSize() {
        return DEFAULT_SIZE;
    }
    
    public double getSpeed() {
        return DEFAULT_SPEED;
    }
    
    public double getRange() {
        return DEFAULT_RANGE;
    }
    
    public abstract int health();
    
    public abstract double getDamage();

    public void update(long updateTime) {
        
    }
    
}
