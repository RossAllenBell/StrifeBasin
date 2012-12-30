package com.rossallenbell.strifebasin.domain.buildings;

import java.awt.Dimension;
import java.awt.Point;

import com.rossallenbell.strifebasin.domain.Asset;

public abstract class Building extends Asset {
    
    private static final long serialVersionUID = 1L;
    
    private Point location;
    
    public Building() {
        location = new Point();
    }
    
    public Point getLocation() {
        return location;
    }
    
    public void setLocation(int x, int y) {
        location.setLocation(x, y);
    }
    
    public abstract Dimension getShape();

    public void update(Building building) {
        location.setLocation(building.location);
    }
    
}
