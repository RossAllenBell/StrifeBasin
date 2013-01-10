package com.rossallenbell.strifebasin.domain;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.rossallenbell.strifebasin.connection.domain.NetworkPlayer;
import com.rossallenbell.strifebasin.domain.buildings.Building;
import com.rossallenbell.strifebasin.domain.buildings.nonbuildable.Sanctuary;
import com.rossallenbell.strifebasin.domain.units.PlayerUnit;

public class Me implements Player {
    
    private int money;
    private int income;
    private long lastIncomeTime;
    
    private Map<Long, Building> buildings;
    private Map<Long, PlayerUnit> units;
    private long assetId;
    
    public Me() {
        assetId = 0;
        money = 0;
        income = Game.STARTING_INCOME;
        buildings = Collections.synchronizedMap(new HashMap<Long, Building>());
        units = Collections.synchronizedMap(new HashMap<Long, PlayerUnit>());
    }

    public Sanctuary getSanctuary() {
        for (Building building : getBuildings().values()) {
            if (building instanceof Sanctuary) {
                return (Sanctuary) building;
            }
        }
        return null;
    }
    
    public int getMoney() {
        return money;
    }
    
    public void alterMoney(int amount) {
        money += amount;
    }
    
    public void income() {
        money += income;
    }
    
    public int getIncome() {
        return income;
    }
    
    public void alterIncome(int amount) {
        income += amount;
    }
    
    public long getLastIncomeTime() {
        return lastIncomeTime;
    }
    
    public void setLastIncomeTime(long lastIncomeTime) {
        this.lastIncomeTime = lastIncomeTime;
    }
    
    public void addBuilding(Building building) {
        building.setAssetId(getNextAssetId());
        buildings.put(building.getAssetId(), building);
    }
    
    public Map<Long, Building> getBuildings() {
        return buildings;
    }
    
    public void addUnit(PlayerUnit unit) {
        unit.setAssetId(getNextAssetId());
        units.put(unit.getAssetId(), unit);
    }
    
    public Map<Long, PlayerUnit> getUnits() {
        return units;
    }
    
    public synchronized long getNextAssetId() {
        return assetId++;
    }

    public NetworkPlayer snapshot() {
        return new NetworkPlayer(this);
    }
    
    public PlayerAsset getAssetById(long assetId) {
        PlayerAsset asset = getBuildings().get(assetId);
        if(asset == null) {
            asset = getUnits().get(assetId);
        }        
        return asset;
    }
    
}