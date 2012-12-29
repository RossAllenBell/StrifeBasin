package com.rossallenbell.strifebasin.domain.units;


public abstract class Unit {
    
    public static final double DEFAULT_SIZE = 1;
    public static final double DEFAULT_SPEED = 2;
    public static final double DEFAULT_RANGE = 0;
    
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
    
}
