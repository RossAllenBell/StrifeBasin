package com.rossallenbell.strifebasin.domain;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.rossallenbell.strifebasin.connection.CommObject;
import com.rossallenbell.strifebasin.domain.buildings.Building;
import com.rossallenbell.strifebasin.domain.units.Unit;

public class Player extends CommObject {

    private static final long serialVersionUID = 1L;
    
    private int money;
    private int income;
    private long lastIncomeTime;
    
    private Map<Long, Building> buildings;
    private Map<Long, Unit> units;
    private long assetId;
    
    public Player() {
        assetId = 0;
        money = 0;
        income = Game.STARTING_INCOME;
        buildings = Collections.synchronizedMap(new HashMap<Long, Building>());
        units = Collections.synchronizedMap(new HashMap<Long, Unit>());
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
    
    public void addUnit(Unit unit) {
        unit.setAssetId(getNextAssetId());
        units.put(unit.getAssetId(), unit);
    }
    
    public Map<Long, Unit> getUnits() {
        return units;
    }
    
    public synchronized long getNextAssetId() {
        return assetId++;
    }
    
}
