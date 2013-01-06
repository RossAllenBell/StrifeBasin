package com.rossallenbell.strifebasin.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.rossallenbell.strifebasin.connection.domain.NetworkPlayer;
import com.rossallenbell.strifebasin.domain.buildings.Building;
import com.rossallenbell.strifebasin.domain.buildings.nonbuildable.Sanctuary;
import com.rossallenbell.strifebasin.domain.units.Unit;

public class Player {
    
    private int money;
    private int income;
    private long lastIncomeTime;
    
    private List<Building> buildings;
    private List<Unit> units;
    private long assetId;
    
    public Player() {
        assetId = 0;
        money = 0;
        income = Game.STARTING_INCOME;
        buildings = Collections.synchronizedList(new ArrayList<Building>());
        units = Collections.synchronizedList(new ArrayList<Unit>());
    }

    public Sanctuary getSanctuary() {
        for (Building building : getBuildings()) {
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
        buildings.add(building);
    }
    
    public List<Building> getBuildings() {
        return buildings;
    }
    
    public void addUnit(Unit unit) {
        unit.setAssetId(getNextAssetId());
        units.add(unit);
    }
    
    public List<Unit> getUnits() {
        return units;
    }
    
    public synchronized long getNextAssetId() {
        return assetId++;
    }

    public NetworkPlayer snapshot() {
        return new NetworkPlayer(this);
    }
    
}
