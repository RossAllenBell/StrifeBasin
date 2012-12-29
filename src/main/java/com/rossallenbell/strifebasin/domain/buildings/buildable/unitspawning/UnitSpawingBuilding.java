package com.rossallenbell.strifebasin.domain.buildings.buildable.unitspawning;

import com.rossallenbell.strifebasin.domain.buildings.buildable.BuildableBuilding;
import com.rossallenbell.strifebasin.domain.units.Unit;

public abstract class UnitSpawingBuilding extends BuildableBuilding {
    
    public final static long DEFAULT_SPAWN_COOLDOWN = 10000;
    
    private long lastSpawnTime;
    
    public Unit spawn(long spawnTime) {
        lastSpawnTime = spawnTime;
        try {
            return getUnit().newInstance();
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
    
    protected abstract Class<? extends Unit> getUnit();
    
}
