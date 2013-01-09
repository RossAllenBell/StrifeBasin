package com.rossallenbell.strifebasin.domain;

import java.awt.geom.Point2D;

import com.rossallenbell.strifebasin.connection.domain.NetworkUnit;

public abstract class PlayerAsset implements Asset {
    
    private long id;
    
    private Me owner;
    
    private Point2D.Double location;
    
    private int health;
    
    public PlayerAsset(Me owner) {
        location = new Point2D.Double();
        this.owner = owner;
        health = getMaxHealth();
    }
    
    @Override
    public Point2D.Double getLocation() {
        return location;
    }
    
    public void setLocation(double x, double y) {
        location.setLocation(x, y);
    }
    
    public void setLocation(Point2D.Double location) {
        location.setLocation(location);
    }
    
    public Me getOwner() {
        return owner;
    }
    
    public void setAssetId(long assetId) {
        id = assetId;
    }
    
    @Override
    public long getAssetId() {
        return id;
    }
    
    @Override
    public int getHealth() {
        return health;
    }
    
    public void takeDamage(NetworkUnit networkUnit) {
        health -= networkUnit.getDamage();
    }
    
    @Override
    public double getHealthRatio() {
        return (double) getHealth() / getMaxHealth();
    }
    
}
