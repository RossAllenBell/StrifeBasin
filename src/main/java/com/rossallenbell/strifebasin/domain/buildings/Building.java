package com.rossallenbell.strifebasin.domain.buildings;

import java.awt.Dimension;
import java.awt.geom.Point2D;

import com.rossallenbell.strifebasin.domain.Me;
import com.rossallenbell.strifebasin.domain.PlayerAsset;

public abstract class Building extends PlayerAsset {
    
    public Building(Me owner) {
        super(owner);
    }
    
    public abstract Dimension getShape();
    
    public void update(long updateTime) {
        
    }
    
    @Override
    public double getSize() {
        return getShape().getWidth();
    }
    
    @Override
    public Point2D.Double getHitLocation() {
        return new Point2D.Double(getLocation().x + (getSize() / 2), getLocation().y + (getSize() / 2));
    }
    
    public double getIncome() {
        return 0;
    }
    
}
