package com.rossallenbell.strifebasin.domain;

import java.awt.geom.Point2D;

import com.rossallenbell.strifebasin.connection.domain.NetworkUnit;

public abstract class Asset {
    
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
    
    public abstract double getSize();
    
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

    public void takeDamage(NetworkUnit networkUnit) {
        health -= networkUnit.getDamage();
    }
    
}
