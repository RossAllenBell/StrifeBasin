package com.rossallenbell.strifebasin.domain;

import java.awt.geom.Point2D;

import com.rossallenbell.strifebasin.domain.units.Unit;

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
        location = new Point2D.Double(x, y);
    }
    
    public void setLocation(Point2D.Double location) {
        setLocation(location.x, location.y);
    }
    
    public Point2D.Double getHitLocation() {
        return location;
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
    
    @Override
    public void takeDamage(Unit unit) {
        health -= unit.getDamage();
    }
    
    @Override
    public double getHealthRatio() {
        return (double) getHealth() / getMaxHealth();
    }
    
    @Override
    public Class<? extends Asset> getImageClass() {
        return this.getClass();
    }
    
    @Override
    public boolean isMine() {
        return true;
    }
    
}
