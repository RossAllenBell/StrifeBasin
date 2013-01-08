package com.rossallenbell.strifebasin.domain;

import java.awt.geom.Point2D;

import com.rossallenbell.strifebasin.connection.domain.NetworkUnit;

public abstract class PlayerAsset extends Asset {
    
    private long id;
    
    private Player owner;
    
    private Point2D.Double location;
    
    private int health;
    
    public PlayerAsset(Player owner) {
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

    @Override
    public abstract double getSize();
    
    public Player getOwner() {
        return owner;
    }
    
    public void setAssetId(long assetId) {
        id = assetId;
    }

    @Override
    public long getAssetId() {
        return id;
    }
    
    public void copyFrom(PlayerAsset asset) {
        setLocation(asset.getLocation());
    }
    
    @Override
    public abstract int getMaxHealth();
    
    @Override
    public int getHealth() {
        return health;
    }

    public void takeDamage(NetworkUnit networkUnit) {
        health -= networkUnit.getDamage();
    }
    
}
