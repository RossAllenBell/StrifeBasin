package com.rossallenbell.strifebasin.domain.units;

import java.awt.geom.Point2D;

import com.rossallenbell.strifebasin.domain.Asset;

public interface Unit {

    public Point2D.Double getCurrentDestination();

    public double getDirection();

    public double getDamage();

    public double getAggroRange();

    public double getRange();

    public double getAttackSpeed();

    public double getSpeed();

    public long getTargetId();

    public Asset getTarget();
    
    public Point2D.Double getLocation(); 
    
}
