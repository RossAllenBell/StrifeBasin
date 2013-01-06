package com.rossallenbell.strifebasin.connection.domain;

import java.awt.geom.Point2D;
import java.io.Serializable;

import com.rossallenbell.strifebasin.domain.Asset;

public class NetworkAsset implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private final Class<? extends Asset> originalAssetClass;
    private final long assetId;
    private final Point2D.Double location;
    private final double size;
    private final int health;
    
    public NetworkAsset(Asset originalAsset) {
        originalAssetClass = originalAsset.getClass();
        assetId = originalAsset.getAssetId();
        location = new Point2D.Double(originalAsset.getLocation().x, originalAsset.getLocation().y);
        size = originalAsset.getSize();
        health = originalAsset.getHealth();
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public Class<? extends Asset> getOriginalAssetClass() {
        return originalAssetClass;
    }

    public long getAssetId() {
        return assetId;
    }

    public Point2D.Double getLocation() {
        return location;
    }

    public double getSize() {
        return size;
    }

    public int getHealth() {
        return health;
    }
    
}
