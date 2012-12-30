package com.rossallenbell.strifebasin.connection.gameupdates;

import java.util.Map;

import com.rossallenbell.strifebasin.connection.CommObject;
import com.rossallenbell.strifebasin.domain.Player;
import com.rossallenbell.strifebasin.domain.buildings.Building;
import com.rossallenbell.strifebasin.domain.units.Unit;

public class UnitsAndBuildings extends CommObject {

    private static final long serialVersionUID = 1L;
    
    private final Map<Long, Building> buildings;
    private final Map<Long, Unit> units;
    
    public UnitsAndBuildings(Player me) {
        buildings = me.getBuildings();
        units = me.getUnits();
    }

    public Map<Long, Building> getBuildings() {
        return buildings;
    }

    public Map<Long, Unit> getUnits() {
        return units;
    }
    
}
