package com.rossallenbell.strifebasin.connection.domain;

import java.util.ArrayList;
import java.util.List;

import com.rossallenbell.strifebasin.connection.CommObject;
import com.rossallenbell.strifebasin.domain.Player;
import com.rossallenbell.strifebasin.domain.buildings.Building;
import com.rossallenbell.strifebasin.domain.buildings.nonbuildable.Sanctuary;
import com.rossallenbell.strifebasin.domain.units.Unit;

public class NetworkPlayer extends CommObject {
    
    private static final long serialVersionUID = 1L;
    
    private List<NetworkAsset> buildings;
    private List<NetworkUnit> units;
    
    public NetworkPlayer() {
        buildings = new ArrayList<NetworkAsset>();        
        units = new ArrayList<NetworkUnit>();
    }
    
    public NetworkPlayer(Player player) {
        buildings = new ArrayList<NetworkAsset>();
        for (Building building : player.getBuildings()) {
            buildings.add(new NetworkAsset(building));
        }
        
        units = new ArrayList<NetworkUnit>();
        for (Unit unit : player.getUnits()) {
            units.add(new NetworkUnit(unit));
        }
    }

    public List<NetworkAsset> getBuildings() {
        return buildings;
    }

    public List<NetworkUnit> getUnits() {
        return units;
    }

    public NetworkAsset getSanctuary() {
        for (NetworkAsset building : getBuildings()) {
            if (building.getOriginalAssetClass() == Sanctuary.class) {
                return building;
            }
        }
        return null;
    }
    
}
