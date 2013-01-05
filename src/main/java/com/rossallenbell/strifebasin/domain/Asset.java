package com.rossallenbell.strifebasin.domain;

import java.awt.geom.Point2D;
import java.io.Serializable;

import com.rossallenbell.strifebasin.domain.units.Unit;

public abstract class Asset implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private long id;
    
    private Player owner;
    
    private Point2D.Double location;
    
    private int health;
    
    public Asset(Player owner) {
        location = new Point2D.Double();
        this.owner = owner;
        health = getMaxHealth();
    }
    
    public Point2D.Double getLocation() {
        return location;
    }
    
    public void setLocation(double x, double y) {
        location.setLocation(x, y);
    }
    
    public void setLocation(Point2D.Double location) {
        location.setLocation(location);
    }
    
    public Player getOwner() {
        return owner;
    }
    
    public void setAssetId(long assetId) {
        id = assetId;
    }
    
    public long getAssetId() {
        return id;
    }
    
    public void copyFrom(Asset asset) {
        setLocation(asset.getLocation());
    }
    
    public abstract int getMaxHealth();
    
    public int getHealth() {
        return health;
    }

    public void takeDamage(Unit unit) {
        health -= unit.getDamage();
    }
    
}
