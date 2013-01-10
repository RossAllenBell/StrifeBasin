package com.rossallenbell.strifebasin.domain;

import java.util.Map;

import com.rossallenbell.strifebasin.domain.units.Unit;

public interface Player {
    
    public Map<Long, ? extends Unit> getUnits();
    
    public Map<Long, ? extends Asset> getBuildings();
    
}
