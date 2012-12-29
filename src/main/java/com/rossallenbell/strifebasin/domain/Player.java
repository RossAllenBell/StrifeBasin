package com.rossallenbell.strifebasin.domain;

import java.util.ArrayList;
import java.util.List;

import com.rossallenbell.strifebasin.domain.buildings.Building;
import com.rossallenbell.strifebasin.domain.units.Unit;

public class Player {
    
    private int money;
    private int income;
    private long lastIncomeTime;
    
    private List<Building> buildings;
    private List<Unit> units;
    
    public Player() {
        money = 0;
        income = Game.STARTING_INCOME;
        buildings = new ArrayList<Building>();
        units = new ArrayList<Unit>();
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
        buildings.add(building);
    }
    
    public List<Building> getBuildings() {
        return buildings;
    }
    
    public void addUnit(Unit unit) {
        units.add(unit);
    }
    
    public List<Unit> getUnits() {
        return units;
    }
    
}
