package com.rossallenbell.strifebasin.ui.effects.imaged;

import java.awt.geom.Point2D;

import com.rossallenbell.strifebasin.domain.util.Pathing;
import com.rossallenbell.strifebasin.ui.resources.HasImage;

@HasImage
public class Arrow extends ImagedEffect {
    
    private Point2D.Double target;
    private Point2D.Double origin;
    private double direction;
    
    public Arrow(long startTime, Point2D.Double origin, Point2D.Double target) {
        super(startTime, origin);
        this.target = target;
        this.origin = origin;
        direction = Pathing.getDirection(origin, target);
    }
    
    @Override
    public long getDuration() {
        return 500;
    }
    
    @Override
    public double getDirection() {
        return direction;
    }
    
    @Override
    public int getSize() {
        return 2;
    }
    
    public void update(long updateTime) {
        if (lastUpdateTime == 0) {
            lastUpdateTime = updateTime;
        }
        
        double progress = ((updateTime - startTime) / (double) getDuration()) * origin.distance(target);
        double direction = getDirection();
        double dx = Math.sin(direction) * progress;
        double dy = -Math.cos(direction) * progress;
        getLocation().setLocation(origin.x + dx, origin.y + dy);
        
        lastUpdateTime = updateTime;
    }
    
}
