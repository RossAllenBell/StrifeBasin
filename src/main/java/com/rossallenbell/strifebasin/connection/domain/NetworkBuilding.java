package com.rossallenbell.strifebasin.connection.domain;

import java.awt.geom.Point2D;

import com.rossallenbell.strifebasin.domain.PlayerAsset;

public class NetworkBuilding extends NetworkAsset {
    
    private static final long serialVersionUID = 1L;
    
    public NetworkBuilding(PlayerAsset originalAsset) {
        super(originalAsset);
    }
    
    @Override
    public Point2D.Double getHitLocation() {
        return new Point2D.Double(getLocation().x + (getSize() / 2), getLocation().y + (getSize() / 2));
    }
    
}
