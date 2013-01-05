package com.rossallenbell.strifebasin.domain.buildings.buildable.unitspawning;

import java.awt.geom.Point2D.Double;

import com.rossallenbell.strifebasin.domain.Game;
import com.rossallenbell.strifebasin.domain.Player;
import com.rossallenbell.strifebasin.domain.buildings.buildable.BuildableBuilding;
import com.rossallenbell.strifebasin.domain.units.Unit;

public abstract class UnitSpawingBuilding extends BuildableBuilding {
    
    public UnitSpawingBuilding(Player owner) {
        super(owner);
    }

    private static final long serialVersionUID = 1L;
    
    public final static long DEFAULT_SPAWN_COOLDOWN = 10000;
    
    private long lastSpawnTime;
    
    public Unit spawn(long spawnTime) {
        lastSpawnTime = spawnTime;
        try {
            return getUnit().getConstructor(Player.class).newInstance(Game.getInstance().getMe());
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
    
    public void copyFrom(UnitSpawingBuilding building) {
        super.copyFrom(building);
        lastSpawnTime = building.lastSpawnTime;
    }
    
    @Override
    public void update(long updateTime) {
        if(getLastSpawnTime() + getSpawnCooldown() <= updateTime){
            Double buildingLocation = getLocation();
            Unit spawnedUnit = spawn(updateTime);
            double x = buildingLocation.getX() + getShape().width;
            double y = buildingLocation.getY() + ((double) getShape().height / 2);
            spawnedUnit.setLocation(x, y);
            getOwner().addUnit(spawnedUnit);
        }
    }
    
    protected abstract Class<? extends Unit> getUnit();
    
}
