package com.rossallenbell.strifebasin.connection.domain;

import java.util.HashMap;
import java.util.Map;

import com.rossallenbell.strifebasin.connection.CommObject;
import com.rossallenbell.strifebasin.domain.Me;
import com.rossallenbell.strifebasin.domain.Player;
import com.rossallenbell.strifebasin.domain.buildings.Building;
import com.rossallenbell.strifebasin.domain.buildings.nonbuildable.Sanctuary;
import com.rossallenbell.strifebasin.domain.units.PlayerUnit;

public class NetworkPlayer extends CommObject implements Player {
    
    private static final long serialVersionUID = 1L;
    
    private final Map<Long, NetworkBuilding> buildings;
    private final Map<Long, NetworkUnit> units;

    public NetworkPlayer() {
        buildings = new HashMap<Long, NetworkBuilding>();
        units = new HashMap<Long, NetworkUnit>();
    }
    
    public NetworkPlayer(Me player) {
        buildings = new HashMap<Long, NetworkBuilding>();
        for (Building building : player.getBuildings().values()) {
            buildings.put(building.getAssetId(), new NetworkBuilding(building));
        }
        
        units = new HashMap<Long, NetworkUnit>();
        for (PlayerUnit unit : player.getUnits().values()) {
            units.put(unit.getAssetId(), new NetworkUnit(unit));
        }
    }
    
    public Map<Long, NetworkBuilding> getBuildings() {
        return buildings;
    }
    
    public Map<Long, NetworkUnit> getUnits() {
        return units;
    }
    
    public NetworkAsset getSanctuary() {
        for (NetworkAsset building : getBuildings().values()) {
            if (building.getOriginalAssetClass() == Sanctuary.class) {
                return building;
            }
        }
        assert false;
        return null;
    }

    public void update(long updateTime) {        
        for (NetworkUnit unit : getUnits().values()) {
            unit.update(updateTime);
        }
    }
    
}
