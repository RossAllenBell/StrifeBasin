package com.rossallenbell.strifebasin.domain.buildings;

import java.awt.Dimension;

import com.rossallenbell.strifebasin.domain.Asset;
import com.rossallenbell.strifebasin.domain.Player;

public abstract class Building extends Asset {
    
    public Building(Player owner) {
        super(owner);
    }
    
    public abstract Dimension getShape();
    
    public void update(long updateTime) {
        
    }
    
    @Override
    public double getSize() {
        return getShape().getWidth();
    }
    
}
