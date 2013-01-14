package com.rossallenbell.strifebasin.connection.domain;

import java.awt.geom.Point2D;

import com.rossallenbell.strifebasin.domain.Asset;
import com.rossallenbell.strifebasin.domain.Game;
import com.rossallenbell.strifebasin.domain.units.PlayerUnit;
import com.rossallenbell.strifebasin.domain.units.Unit;
import com.rossallenbell.strifebasin.domain.util.Pathing;
import com.rossallenbell.strifebasin.ui.effects.Effect;

public class NetworkUnit extends NetworkAsset implements Unit {
    
    private static final long serialVersionUID = 1L;   
    
    private final double damage;    
    private final Point2D.Double destination;    
    private final double speed;    
    private final double range;    
    private final double aggroRange;    
    private final double attackSpeed;    
    private final long targetId;
    private final int animationFrame;
    private final Class<? extends Effect> attackEffect;
    
    private long lastUpdateTime;
    
    public NetworkUnit(PlayerUnit originalUnit) {
        super(originalUnit);
        damage = originalUnit.getDamage();
        destination = new Point2D.Double(originalUnit.getCurrentDestination().x, originalUnit.getCurrentDestination().y);
        speed = originalUnit.getSpeed();
        range = originalUnit.getRange();
        aggroRange = originalUnit.getAggroRange();
        attackSpeed = originalUnit.getAttackSpeed();
        targetId = originalUnit.getTargetId();
        animationFrame = originalUnit.getAnimationFrame();
        attackEffect = originalUnit.getAttackEffect();
    }
    
    @Override
    public double getDamage() {
        return damage;
    }
    
    @Override
    public Point2D.Double getCurrentDestination() {
        return destination;
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
        if(lastUpdateTime == 0) {
            lastUpdateTime = updateTime;
        }
        
        double moveDistance = ((updateTime - lastUpdateTime) / 1000.0) * getSpeed();
        Asset target = Game.getInstance().getMe().getAssetById(getTargetId());
        if (target == null) {
            // guess the next target until we get an update
            target = Pathing.getInstance().getClosestAggroableAsset(this, Game.getInstance().getMe());
        }
        if (target != null && !Pathing.getInstance().canHitAsset(this, target)) {
            Point2D.Double destination = getCurrentDestination();
            Point2D.Double location = getLocation();
            double distanceToDestination = destination.distance(location);
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
            
            if(newLocation != null && Pathing.getInstance().canMove(this, newLocation)) {
                getLocation().setLocation(newLocation);
            }            
        }
        
        lastUpdateTime = updateTime;
    }
    
    @Override
    public Class<? extends Effect> getAttackEffect() {
        return attackEffect;
    }
    
}
