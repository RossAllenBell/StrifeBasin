package com.rossallenbell.strifebasin.domain.units;

import java.awt.geom.Point2D;

import com.rossallenbell.strifebasin.domain.Asset;


public abstract class Unit extends Asset {
    
    private static final long serialVersionUID = 1L;
    
    public static final double DEFAULT_SIZE = 1;
    public static final double DEFAULT_SPEED = 2;
    public static final double DEFAULT_RANGE = 0;
    
    private Point2D.Double location;
    
    public Unit() {
        location = new Point2D.Double();
    }
    
    public Point2D.Double getLocation() {
        return location;
    }
    
    public void setLocation(double x, double y) {
        location.setLocation(x, y);
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

    public void update(Unit unit) {
        location.setLocation(unit.location);
    }
    
}
