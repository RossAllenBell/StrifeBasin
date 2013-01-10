package com.rossallenbell.strifebasin.connection.domain;

import java.awt.geom.Point2D;
import java.io.Serializable;

import com.rossallenbell.strifebasin.domain.Asset;
import com.rossallenbell.strifebasin.domain.units.Unit;

public class NetworkAsset implements Asset, Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private final Class<? extends Asset> originalAssetClass;
    private final long assetId;
    private final Point2D.Double location;
    private final double size;
    private int health;
    private final int maxHealth;
    
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
        return location;
    }

    @Override
    public Point2D.Double getHitLocation() {
        return location;
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
    
}