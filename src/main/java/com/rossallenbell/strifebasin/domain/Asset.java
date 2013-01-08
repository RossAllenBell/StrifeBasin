package com.rossallenbell.strifebasin.domain;

import java.awt.geom.Point2D;

public abstract class Asset {
    
    public abstract long getAssetId();
    
    public abstract Point2D.Double getLocation();
    
    public abstract double getSize();
    
    public abstract int getHealth();
    
    public abstract int getMaxHealth();

    public double getHealthRatio() {
        return (double) getHealth() / getMaxHealth();
    }
    
}
