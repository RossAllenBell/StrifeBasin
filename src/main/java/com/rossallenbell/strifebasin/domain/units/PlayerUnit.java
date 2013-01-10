package com.rossallenbell.strifebasin.domain.units;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.rossallenbell.strifebasin.connection.domain.NetworkAsset;
import com.rossallenbell.strifebasin.connection.domain.NetworkUnit;
import com.rossallenbell.strifebasin.connection.gameevents.AttackEvent;
import com.rossallenbell.strifebasin.domain.Asset;
import com.rossallenbell.strifebasin.domain.Game;
import com.rossallenbell.strifebasin.domain.Me;
import com.rossallenbell.strifebasin.domain.PlayerAsset;
import com.rossallenbell.strifebasin.domain.util.Pathing;
import com.rossallenbell.strifebasin.threads.CommSocketSender;

public abstract class PlayerUnit extends PlayerAsset implements Unit {
    
    public static final double DEFAULT_SIZE = 1;
    public static final double DEFAULT_SPEED = 2;
    public static final long DEFAULT_ATTACK_SPEED = 2000;
    public static final double DEFAULT_RANGE = 0;
    
    public static final double MINIMUM_AGGRO_RANGE = 10;
    
    public static final long TARGET_ASSESSMENT_COOLDOWN = 500;
    public static final long ROUTE_ASSESSMENT_COOLDOWN = 500;
    
    private List<Point2D.Double> route;
    private NetworkAsset target;
    
    private long lastTargetAssessment;
    private long lastRouteAssessment;
    
    private long lastUpdateTime;
    
    private long lastAttackTime;
    
    public PlayerUnit(Me owner) {
        super(owner);
        route = Collections.synchronizedList(new ArrayList<Point2D.Double>());
    }
    
    @Override
    public double getSize() {
        return DEFAULT_SIZE;
    }
    
    @Override
    public double getSpeed() {
        return DEFAULT_SPEED;
    }
    
    @Override
    public double getAttackSpeed() {
        return DEFAULT_ATTACK_SPEED;
    }
    
    @Override
    public double getRange() {
        return Math.max(getSize() / 2, DEFAULT_RANGE);
    }
    
    @Override
    public double getAggroRange() {
        return Math.max(getRange(), MINIMUM_AGGRO_RANGE);
    }
    
    @Override
    public abstract double getDamage();
    
    public void update(long updateTime) {
        if (lastUpdateTime == 0) {
            lastUpdateTime = updateTime;
        }
        
        // target
        if (target == null || target.getHealth() <= 0 || lastTargetAssessment + TARGET_ASSESSMENT_COOLDOWN <= updateTime) {
            target = Pathing.getInstance().getClosestAggroableAsset(this);
            if (target == null) {
                target = Game.getInstance().getThem().getSanctuary();
            }
        }
        assert target != null;
        
        // move
        double moveDistance = ((updateTime - lastUpdateTime) / 1000.0) * getSpeed();
        if (lastRouteAssessment + ROUTE_ASSESSMENT_COOLDOWN <= updateTime) {
            route = Pathing.getInstance().getRoute(this, target);
            lastRouteAssessment = updateTime;
        }
        
        if (!Pathing.canHitAsset(this, target)) {
            Point2D.Double destination = getCurrentDestination();
            double distanceToDestination = destination.distance(getLocation());
            if (distanceToDestination <= moveDistance) {
                setLocation(destination);
            } else if (moveDistance > 0) {
                Point2D.Double currentLocation = getLocation();
                double direction = Pathing.getDirection(this, destination);
                double dx = Math.sin(direction) * moveDistance;
                double dy = Math.cos(direction) * moveDistance;
                getLocation().setLocation(currentLocation.x + dx, currentLocation.y + dy);
            }
        }
        
        // attack
        if (Pathing.canHitAsset(this, target) && lastAttackTime + getAttackSpeed() <= updateTime) {
            CommSocketSender.getInstance().enqueue(new AttackEvent(new NetworkUnit(this), target));
            lastAttackTime = updateTime;
        }
        
        lastUpdateTime = updateTime;
        
    }
    
    public List<Point2D.Double> getRoute() {
        return route;
    }
    
    public void setRoute(List<Point2D.Double> route) {
        this.route = route;
    }
    
    @Override
    public long getTargetId() {
        return target.getAssetId();
    }
    
    @Override
    public Asset getTarget() {
        return target;
    }
    
    public void setTarget(NetworkAsset target) {
        this.target = target;
    }
    
    @Override
    public Point2D.Double getCurrentDestination() {
        if (!route.isEmpty() && route.get(0).equals(getLocation())) {
            route.remove(0);
        }
        
        if (!route.isEmpty()) {
            return route.get(0);
        } else if (target != null) {
            return target.getHitLocation();
        } else {
            return getLocation();
        }
    }
    
}
