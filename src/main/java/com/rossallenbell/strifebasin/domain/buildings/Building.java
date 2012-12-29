package com.rossallenbell.strifebasin.domain.buildings;

import java.awt.Dimension;
import java.awt.Point;

public abstract class Building {
    
    private Point location;
    
    public Building() {
        location = new Point();
    }
    
    public Point getLocation() {
        return location;
    }
    
    public void setLocation(int x, int y) {
        location.x = x;
        location.y = y;
    }
    
    public abstract Dimension getShape();
    
}
