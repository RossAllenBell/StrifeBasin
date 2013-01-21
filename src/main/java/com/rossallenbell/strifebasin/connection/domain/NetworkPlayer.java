package com.rossallenbell.strifebasin.connection.domain;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

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
        return null;
    }
    
    public void update(long updateTime) {
        synchronized (units) {
            for (NetworkUnit unit : units.values()) {
                unit.update(updateTime);
            }
        }
    }
    
    public void applyRemotePlayerData(NetworkPlayer networkPlayer) {
        synchronized (buildings) {
            Iterator<Entry<Long, NetworkBuilding>> previouslyKnownBuildings = buildings.entrySet().iterator();
            while (previouslyKnownBuildings.hasNext()) {
                Entry<Long, NetworkBuilding> building = previouslyKnownBuildings.next();
                if (!networkPlayer.buildings.containsKey(building.getKey())) {
                    previouslyKnownBuildings.remove();
                }
            }
            for (NetworkBuilding building : networkPlayer.getBuildings().values()) {
                building.mirror();
                if (buildings.containsKey(building.getAssetId())) {
                    buildings.get(building.getAssetId()).applyRemoteAssetData(building);
                } else {
                    buildings.put(building.getAssetId(), building);
                }
            }
        }
        
        synchronized (units) {
            Iterator<Entry<Long, NetworkUnit>> previouslyKnownUnits = units.entrySet().iterator();
            while (previouslyKnownUnits.hasNext()) {
                Entry<Long, NetworkUnit> unit = previouslyKnownUnits.next();
                if (!networkPlayer.units.containsKey(unit.getKey())) {
                    previouslyKnownUnits.remove();
                }
            }
            for (NetworkUnit unit : networkPlayer.getUnits().values()) {
                unit.mirror();
                if (units.containsKey(unit.getAssetId())) {
                    units.get(unit.getAssetId()).applyRemoteAssetData(unit);
                } else {
                    units.put(unit.getAssetId(), unit);
                }
            }
        }
    }
    
    @Override
    public NetworkAsset getAssetById(long assetId) {
        NetworkAsset asset = getBuildings().get(assetId);
        if (asset == null) {
            asset = getUnits().get(assetId);
        }
        return asset;
    }
    
}
