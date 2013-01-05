package com.rossallenbell.strifebasin.domain;

import java.awt.geom.Point2D;
import java.io.Serializable;

public class Asset implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private long id;
    
    private Player owner;
    
    private Point2D.Double location;
    
    public Asset(Player owner) {
        location = new Point2D.Double();
        this.owner = owner;
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
    
}
