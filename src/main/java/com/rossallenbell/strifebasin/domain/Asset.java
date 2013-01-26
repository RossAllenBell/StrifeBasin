package com.rossallenbell.strifebasin.domain;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;

import com.rossallenbell.strifebasin.domain.units.Unit;

public interface Asset {
    
    public long getAssetId();
    
    public Point2D.Double getLocation();
    
    public Point2D.Double getHitLocation();
    
    void setLocation(Double newLocation);
    
    public double getSize();
    
    public int getHealth();
    
    public int getMaxHealth();

    public double getHealthRatio();

    public abstract void takeDamage(Unit networkUnit);
    
    public Class<? extends Asset> getImageClass();
    
    public boolean isMine();

}
