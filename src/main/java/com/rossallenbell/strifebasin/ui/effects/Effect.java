package com.rossallenbell.strifebasin.ui.effects;

import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

public abstract class Effect {
    
    private Point2D.Double location;
    protected final long startTime;
    protected long lastUpdateTime;
    
    public Effect(long startTime, Point2D.Double location) {
        this.startTime = startTime;
        this.location = new Point2D.Double(location.x, location.y);
    }

    public Point2D.Double getLocation() {
        return location;
    }

    public int getSize() {
        return 1;
    }

    public double getDirection() {
        return 0;
    }

    public abstract BufferedImage getImage();
    
    public abstract long getDuration();
    
    public boolean isComplete() {
        return lastUpdateTime > startTime + getDuration();
    }

    public abstract void update(long updateTime);
    
}
