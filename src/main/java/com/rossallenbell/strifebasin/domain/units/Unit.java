package com.rossallenbell.strifebasin.domain.units;

import java.awt.geom.Point2D;
import java.util.List;

import com.rossallenbell.strifebasin.connection.gameevents.AttackEvent;
import com.rossallenbell.strifebasin.domain.Asset;
import com.rossallenbell.strifebasin.domain.Game;
import com.rossallenbell.strifebasin.domain.Player;
import com.rossallenbell.strifebasin.domain.util.Pathing;
import com.rossallenbell.strifebasin.threads.CommSocketSender;

public abstract class Unit extends Asset {
    
    private static final long serialVersionUID = 1L;
    
    public static final double DEFAULT_SIZE = 1;
    public static final double DEFAULT_SPEED = 2;
    public static final long DEFAULT_ATTACK_SPEED = 2000;
    public static final double DEFAULT_RANGE = 0;
    
    public static final double MINIMUM_AGGRO_RANGE = 10;
    
    public static final long TARGET_ASSESSMENT_COOLDOWN = 1000;
    public static final long ROUTE_ASSESSMENT_COOLDOWN = 1000;
    
    private List<Point2D.Double> route;
    private Asset target;
    
    private long lastTargetAssessment;
    private long lastRouteAssessment;
    
    private long lastUpdateTime;
    
    private long lastAttackTime;
    
    public Unit(Player owner) {
        super(owner);
    }
    
    public double getSize() {
        return DEFAULT_SIZE;
    }
    
    public double getSpeed() {
        return DEFAULT_SPEED;
    }
    
    public double getAttackSpeed() {
        return DEFAULT_ATTACK_SPEED;
    }
    
    public double getRange() {
        return DEFAULT_RANGE;
    }
    
    public double getAggroRange() {
        return Math.max(getRange(), MINIMUM_AGGRO_RANGE);
    }
    
    public abstract double getDamage();
    
    public void update(long updateTime) {
        // target
        if (target == null || target.getHealth() <= 0 || lastTargetAssessment + TARGET_ASSESSMENT_COOLDOWN <= updateTime) {
            target = Pathing.getInstance().getClosestAggroableAsset(this);
            if (target == null) {
                target = Game.getInstance().getThem().getSanctuary();
            }
        }
        
        // move
        double moveDistance = (updateTime - lastUpdateTime) / 1000.0 * getSpeed();
        if (lastRouteAssessment + ROUTE_ASSESSMENT_COOLDOWN <= updateTime) {
            route = Pathing.getInstance().getRoute(this, target);
        }
        Point2D.Double nextDestination = route.get(0);
        if (nextDestination.distance(getLocation()) <= moveDistance) {
            setLocation(route.remove(0));
        } else {
            Point2D.Double currentLocation = getLocation();
            double direction = getDirection();
            double dx = Math.sin(direction) + moveDistance;
            double dy = Math.cos(direction) + moveDistance;
            getLocation().setLocation(currentLocation.x + dx, currentLocation.y + dy);
        }
        
        // attack
        if (target.getLocation().distance(getLocation()) <= getRange() && lastAttackTime + getAttackSpeed() <= updateTime) {
            lastAttackTime = updateTime;
            CommSocketSender.getInstance().enqueue(new AttackEvent(this, target));
        }
        
        lastUpdateTime = updateTime;
        
    }
    
    public List<Point2D.Double> getRoute() {
        return route;
    }
    
    public void setRoute(List<Point2D.Double> route) {
        this.route = route;
    }
    
    public Asset getTarget() {
        return target;
    }
    
    public void setTarget(Asset target) {
        this.target = target;
    }
    
    public double getDirection() {
        Point2D.Double target = route.get(0);
        if (target == null) {
            target = this.target.getLocation();
        }
        
        if (target == null) {
            return 0;
        }
        
        return Math.atan2(target.x - getLocation().x, target.y - getLocation().y);
    }
    
}
