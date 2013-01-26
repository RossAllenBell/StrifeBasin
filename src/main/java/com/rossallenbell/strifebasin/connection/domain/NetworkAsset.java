package com.rossallenbell.strifebasin.connection.domain;

import java.awt.geom.Point2D;
import java.io.Serializable;

import com.rossallenbell.strifebasin.domain.Asset;
import com.rossallenbell.strifebasin.domain.Game;
import com.rossallenbell.strifebasin.domain.units.Unit;

public abstract class NetworkAsset implements Asset, Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private final Class<? extends Asset> originalAssetClass;
    private final long assetId;
    private final double size;
    private final int maxHealth;
    
    private int health;
    private Point2D.Double location;
    
    public NetworkAsset(Asset originalAsset) {
        originalAssetClass = originalAsset.getClass();
        assetId = originalAsset.getAssetId();
        location = new Point2D.Double(originalAsset.getLocation().x, originalAsset.getLocation().y);
        size = originalAsset.getSize();
        health = originalAsset.getHealth();
        maxHealth = originalAsset.getMaxHealth();
    }
    
    public static long getSerialversionuid() {
        return serialVersionUID;
    }
    
    public Class<? extends Asset> getOriginalAssetClass() {
        return originalAssetClass;
    }
    
    @Override
    public long getAssetId() {
        return assetId;
    }
    
    @Override
    public Point2D.Double getLocation() {
        return (Point2D.Double) location.clone();
    }

    @Override
    public Point2D.Double getHitLocation() {
        return getLocation();
    }
    
    @Override
    public void setLocation(Point2D.Double newLocation) {        
        location = newLocation;
    }
    
    @Override
    public double getSize() {
        return size;
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
    public int getMaxHealth() {
        return maxHealth;
    }
    
    @Override
    public double getHealthRatio() {
        return (double) getHealth() / getMaxHealth();
    }

    public void takeDamage(NetworkUnit networkUnit) {}

    @Override
    public Class<? extends Asset> getImageClass() {
        return originalAssetClass;
    }
    
    @Override
    public boolean isMine() {
        return false;
    }

    public void applyRemoteAssetData(NetworkAsset asset) {
        setLocation(asset.getLocation());
        health = Math.min(health, asset.getHealth());
    }

    public void mirror() {
        location = Game.getMirroredLocation(location);
    }
    
}
