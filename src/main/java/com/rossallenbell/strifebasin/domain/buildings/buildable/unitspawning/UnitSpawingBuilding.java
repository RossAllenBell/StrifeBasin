package com.rossallenbell.strifebasin.domain.buildings.buildable.unitspawning;

import java.awt.Dimension;
import java.awt.geom.Point2D;

import com.rossallenbell.strifebasin.domain.Me;
import com.rossallenbell.strifebasin.domain.buildings.buildable.BuildableBuilding;
import com.rossallenbell.strifebasin.domain.units.PlayerUnit;

public abstract class UnitSpawingBuilding extends BuildableBuilding {
    
    public UnitSpawingBuilding(Me owner) {
        super(owner);
    }

    public final static long DEFAULT_SPAWN_COOLDOWN = 10000;
    
    private long lastSpawnTime;
    
    public PlayerUnit spawn(long spawnTime) {
        lastSpawnTime = spawnTime;
        try {
            return getUnit().getConstructor(Me.class).newInstance(getOwner());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public long getLastSpawnTime() {
        return lastSpawnTime;
    }
    
    public long getSpawnCooldown() {
        return DEFAULT_SPAWN_COOLDOWN;
    }
    
    @Override
    public void update(long updateTime) {
        if(getLastSpawnTime() + getSpawnCooldown() <= updateTime){
            Point2D.Double buildingLocation = getHitLocation();
            PlayerUnit spawnedUnit = spawn(updateTime);
            spawnedUnit.setLocation(buildingLocation.x, buildingLocation.y);
            getOwner().addUnit(spawnedUnit);
        }
    }
    
    protected abstract Class<? extends PlayerUnit> getUnit();

    @Override
    public int getMaxHealth() {
        return 100;
    }

    @Override
    public Dimension getShape() {
        return new Dimension(4,4);
    }
    
}
