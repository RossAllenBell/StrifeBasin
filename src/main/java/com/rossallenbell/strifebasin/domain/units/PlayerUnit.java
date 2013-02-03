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
import com.rossallenbell.strifebasin.ui.effects.Effect;
import com.rossallenbell.strifebasin.ui.effects.EffectsFactory;
import com.rossallenbell.strifebasin.ui.effects.EffectsManager;
import com.rossallenbell.strifebasin.ui.resources.AnimationManager;

public abstract class PlayerUnit extends PlayerAsset implements Unit {
    
    public static final double DEFAULT_SIZE = 3;
    public static final double DEFAULT_SPEED = 2;
    public static final long DEFAULT_ATTACK_SPEED = 2000;
    public static final double DEFAULT_RANGE = 0;
    
    public static final double MINIMUM_AGGRO_RANGE = 10;
    
    public static final long TARGET_ASSESSMENT_COOLDOWN = 250;
    public static final long ROUTE_ASSESSMENT_COOLDOWN = 500;
    
    private List<Point2D.Double> route;
    private long targetId;
    
    private long lastTargetAssessment;
    private long lastRouteAssessment;
    private long lastUpdateTime;
    private long lastAttackTime;
    private long lastAnimationFrameSwitch;
    
    private int animationFrame;
    
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
        return Math.max(getSize() / 2, DEFAULT_RANGE) + 0.3;
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
        Asset target = Game.getInstance().getThem().getUnits().get(targetId);
        if (target == null || target.getHealth() <= 0 || lastTargetAssessment + TARGET_ASSESSMENT_COOLDOWN <= updateTime) {
            target = Pathing.getInstance().getClosestAggroableAsset(this, Game.getInstance().getThem());
            if (target == null) {
                target = Game.getInstance().getThem().getSanctuary();
            }
        }
        targetId = target.getAssetId();
        
        // move
        double moveDistance = ((updateTime - lastUpdateTime) / 1000.0) * getSpeed();
        
        if (!Pathing.getInstance().canHitAsset(this, target)) {
            if (lastRouteAssessment + ROUTE_ASSESSMENT_COOLDOWN <= updateTime) {
                route = Pathing.getInstance().getRoute(this, target);
                lastRouteAssessment = updateTime;
            }
            
            Point2D.Double destination = getCurrentDestination();
            double distanceToDestination = destination.distance(getLocation());
            Point2D.Double newLocation = null;
            if (distanceToDestination <= moveDistance) {
                newLocation = destination;
            } else if (moveDistance > 0) {
                Point2D.Double currentLocation = getLocation();
                double direction = Pathing.getInstance().getDirection(currentLocation, destination);
                double dx = Math.sin(direction) * moveDistance;
                double dy = -Math.cos(direction) * moveDistance;
                newLocation = new Point2D.Double(currentLocation.x + dx, currentLocation.y + dy);
            }
            
            if (newLocation != null && Pathing.getInstance().canMove(this, newLocation)) {
                setLocation(newLocation);
            }
        }
        
        // attack
        if (Pathing.getInstance().canHitAsset(this, target) && lastAttackTime + getAttackSpeed() <= updateTime) {
            route.clear();
            
            Effect attackEffect = EffectsFactory.getInstance().buildEffect(this, target, updateTime);
            if (attackEffect != null) {
                EffectsManager.getInstance().addEffect(attackEffect);
            }
            CommSocketSender.getInstance().enqueue(new AttackEvent(new NetworkUnit(this), target));
            target.takeDamage(this);
            lastAttackTime = updateTime;
        }
        
        if (lastAnimationFrameSwitch + AnimationManager.DEFAULT_FRAME_DURATION <= updateTime) {
            animationFrame = ++animationFrame % AnimationManager.getInstance().getFrameCount(getAnimationClass());
            lastAnimationFrameSwitch = updateTime;
        }
        
        lastUpdateTime = updateTime;
        
    }
    
    public List<Point2D.Double> getRoute() {
        return route;
    }
    
    @Override
    public long getTargetId() {
        return targetId;
    }
    
    @Override
    public Asset getTarget() {
        return Game.getInstance().getThem().getAssetById(targetId);
    }
    
    public void setTarget(NetworkAsset target) {
        targetId = target.getAssetId();
    }
    
    @Override
    public Point2D.Double getCurrentDestination() {
        if (!route.isEmpty() && route.get(0).equals(getLocation())) {
            route.remove(0);
        }
        
        if (!route.isEmpty()) {
            return route.get(0);
        } else {
            Asset target = getTarget();
            if (target != null) {
                return target.getHitLocation();
            } else {
                return getLocation();
            }
        }
    }
    
    @Override
    public int getAnimationFrame() {
        return animationFrame;
    }
    
    @Override
    public Class<? extends Asset> getAnimationClass() {
        return this.getClass();
    }
    
    @Override
    public Class<? extends Effect> getAttackEffect() {
        return null;
    }
    
    public long getLastAnimationFrameSwitch() {
        return lastAnimationFrameSwitch;
    }
    
}
