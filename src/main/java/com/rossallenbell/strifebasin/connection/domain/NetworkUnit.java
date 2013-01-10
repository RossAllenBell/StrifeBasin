package com.rossallenbell.strifebasin.connection.domain;

import java.awt.geom.Point2D;

import com.rossallenbell.strifebasin.domain.Game;
import com.rossallenbell.strifebasin.domain.PlayerAsset;
import com.rossallenbell.strifebasin.domain.units.PlayerUnit;
import com.rossallenbell.strifebasin.domain.units.Unit;

public class NetworkUnit extends NetworkAsset implements Unit {
    
    private static final long serialVersionUID = 1L;
    
    private final double damage;
    
    private final Point2D.Double destination;
    
    private final double speed;
    
    private final double range;
    
    private final double aggroRange;
    
    private final double attackSpeed;
    
    private final long targetId;
    
    public NetworkUnit(PlayerUnit originalUnit) {
        super(originalUnit);
        damage = originalUnit.getDamage();
        destination = new Point2D.Double(originalUnit.getCurrentDestination().x, originalUnit.getCurrentDestination().y);
        speed = originalUnit.getSpeed();
        range = originalUnit.getRange();
        aggroRange = originalUnit.getAggroRange();
        attackSpeed = originalUnit.getAttackSpeed();
        targetId = originalUnit.getTarget().getAssetId();
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
    public PlayerAsset getTarget() {
        return Game.getInstance().getMe().getAssetById(getTargetId());
    }
    
}
