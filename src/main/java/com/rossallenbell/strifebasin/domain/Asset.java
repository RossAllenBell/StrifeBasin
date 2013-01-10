package com.rossallenbell.strifebasin.domain;

import java.awt.geom.Point2D;

public interface Asset {
    
    public long getAssetId();
    
    public Point2D.Double getLocation();
    
    public Point2D.Double getHitLocation();
    
    public double getSize();
    
    public int getHealth();
    
    public int getMaxHealth();

    public double getHealthRatio();
    
}
