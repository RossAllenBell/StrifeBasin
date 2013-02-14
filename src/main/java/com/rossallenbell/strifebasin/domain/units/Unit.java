package com.rossallenbell.strifebasin.domain.units;

import java.awt.geom.Point2D;
import java.util.List;

import com.rossallenbell.strifebasin.domain.Asset;
import com.rossallenbell.strifebasin.ui.effects.Effect;
import com.rossallenbell.strifebasin.ui.resources.FrameHelper;

public interface Unit extends Asset {
    
    public static final double MAX_SIZE = PlayerUnit.DEFAULT_SIZE;

    public Point2D.Double getCurrentDestination();

    public double getDamage();

    public double getAggroRange();

    public double getRange();

    public double getAttackSpeed();

    public double getSpeed();

    public long getTargetId();

    public Asset getTarget();
    
    public Point2D.Double getLocation(); 
    
    public FrameHelper getFrameHelper();
    
    public Class<? extends Asset> getAnimationClass();
    
    public Class<? extends Effect> getAttackEffect();

    public List<Point2D.Double> getRoute();
    
}
