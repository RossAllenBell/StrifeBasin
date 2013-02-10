package com.rossallenbell.strifebasin.connection.domain;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import com.rossallenbell.strifebasin.domain.Asset;
import com.rossallenbell.strifebasin.domain.Game;
import com.rossallenbell.strifebasin.domain.units.PlayerUnit;
import com.rossallenbell.strifebasin.domain.units.Unit;
import com.rossallenbell.strifebasin.domain.util.Pathing;
import com.rossallenbell.strifebasin.ui.effects.Effect;
import com.rossallenbell.strifebasin.ui.resources.AnimationManager;

public class NetworkUnit extends NetworkAsset implements Unit {
    
    private static final long serialVersionUID = 1L;
    
    private final double damage;
    private final double speed;
    private final double range;
    private final double aggroRange;
    private final double attackSpeed;
    private final Class<? extends Effect> attackEffect;
    
    private long targetId;
    private int animationFrame;
    private List<Point2D.Double> route;
    
    private long lastUpdateTime;
    private long lastAnimationFrameSwitch;
    
    public NetworkUnit(PlayerUnit originalUnit) {
        super(originalUnit);
        damage = originalUnit.getDamage();
        speed = originalUnit.getSpeed();
        range = originalUnit.getRange();
        aggroRange = originalUnit.getAggroRange();
        attackSpeed = originalUnit.getAttackSpeed();
        targetId = originalUnit.getTargetId();
        animationFrame = originalUnit.getAnimationFrame();
        attackEffect = originalUnit.getAttackEffect();
        
        route = new ArrayList<Point2D.Double>();
        for (Point2D.Double routePoint : originalUnit.getRoute()) {
            route.add((Point2D.Double) routePoint.clone());
        }
        
        lastAnimationFrameSwitch = originalUnit.getLastAnimationFrameSwitch();
    }
    
    @Override
    public double getDamage() {
        return damage;
    }
    
    @Override
    public Point2D.Double getCurrentDestination() {
        synchronized (route) {
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
    }
    
    @Override
    public double getSpeed() {
        return speed;
    }
    
    @Override
    public double getAggroRange() {
        return aggroRange;
    }
    
    @Override
    public double getRange() {
        return range;
    }
    
    @Override
    public double getAttackSpeed() {
        return attackSpeed;
    }
    
    @Override
    public long getTargetId() {
        return targetId;
    }
    
    @Override
    public Asset getTarget() {
        return Game.getInstance().getMe().getAssetById(getTargetId());
    }
    
    @Override
    public int getAnimationFrame() {
        return animationFrame;
    }
    
    @Override
    public Class<? extends Asset> getAnimationClass() {
        return this.getOriginalAssetClass();
    }
    
    public void update(long updateTime) {
        if (lastUpdateTime == 0) {
            lastUpdateTime = updateTime;
        }
        
        Point2D.Double originalLocation = getLocation();
        
        double moveDistance = ((updateTime - lastUpdateTime) / 1000.0) * getSpeed();
        Asset target = Game.getInstance().getMe().getAssetById(getTargetId());
        if (target == null) {
            // guess the next target until we get an update
            target = Pathing.getInstance().getClosestAggroableAsset(this, Game.getInstance().getMe());
        }
        if (target != null && !Pathing.getInstance().canHitAsset(this, target)) {
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
        
        if (originalLocation.equals(getLocation())) {
            animationFrame = 0;
            lastAnimationFrameSwitch = updateTime;
        } else if (lastAnimationFrameSwitch + AnimationManager.DEFAULT_FRAME_DURATION <= updateTime) {
            animationFrame = ++animationFrame % AnimationManager.getInstance().getFrameCount(getAnimationClass());
            lastAnimationFrameSwitch = updateTime;
        }
        
        lastUpdateTime = updateTime;
    }
    
    @Override
    public Class<? extends Effect> getAttackEffect() {
        return attackEffect;
    }
    
    @Override
    public List<Point2D.Double> getRoute() {
        return route;
    }
    
    @Override
    public void mirror() {
        super.mirror();
        
        List<Point2D.Double> mirroredRoute = new ArrayList<Point2D.Double>();
        synchronized (route) {
            for (Point2D.Double routePoint : route) {
                mirroredRoute.add(Game.getMirroredLocation(routePoint));
            }
            route = mirroredRoute;
        }
    }
    
    @Override
    public void applyRemoteAssetData(NetworkAsset asset) {
        super.applyRemoteAssetData(asset);
        
        if (!this.getClass().isAssignableFrom(asset.getClass())) {
            throw new IllegalArgumentException();
        }
        NetworkUnit networkUnit = (NetworkUnit) asset;
        
        targetId = networkUnit.targetId;
        
        synchronized (route) {
            route.clear();
            for (Point2D.Double routePoint : networkUnit.route) {
                route.add((Point2D.Double) routePoint.clone());
            }
        }
    }
    
}
