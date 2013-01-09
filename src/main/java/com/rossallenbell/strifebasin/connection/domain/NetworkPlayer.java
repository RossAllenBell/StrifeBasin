package com.rossallenbell.strifebasin.connection.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.rossallenbell.strifebasin.connection.CommObject;
import com.rossallenbell.strifebasin.domain.Me;
import com.rossallenbell.strifebasin.domain.buildings.Building;
import com.rossallenbell.strifebasin.domain.buildings.nonbuildable.Sanctuary;
import com.rossallenbell.strifebasin.domain.units.PlayerUnit;

public class NetworkPlayer extends CommObject {
    
    private static final long serialVersionUID = 1L;
    
    private final List<NetworkAsset> buildings;
    private final List<NetworkUnit> units;
    
    public NetworkPlayer() {
        buildings = new ArrayList<NetworkAsset>();
        units = new ArrayList<NetworkUnit>();
    }
    
    public NetworkPlayer(Me player) {
        buildings = new ArrayList<NetworkAsset>();
        for (Building building : player.getBuildings().values()) {
            buildings.add(new NetworkAsset(building));
        }
        
        units = new ArrayList<NetworkUnit>();
        for (PlayerUnit unit : player.getUnits().values()) {
            units.add(new NetworkUnit(unit));
        }
    }
    
    public List<NetworkAsset> getBuildings() {
        return Collections.unmodifiableList(buildings);
    }
    
    public List<NetworkUnit> getUnits() {
        return Collections.unmodifiableList(units);
    }
    
    public NetworkAsset getSanctuary() {
        for (NetworkAsset building : getBuildings()) {
            if (building.getOriginalAssetClass() == Sanctuary.class) {
                return building;
            }
        }
        assert false;
        return null;
    }
    
}
