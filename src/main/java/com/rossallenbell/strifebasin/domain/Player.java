package com.rossallenbell.strifebasin.domain;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import org.javatuples.Pair;

import com.rossallenbell.strifebasin.domain.buildings.Building;
import com.rossallenbell.strifebasin.domain.units.Unit;

public class Player {
    
    private int money;
    private int income;
    private long lastIncomeTime;
    
    private List<Pair<Building, Point>> buildings;
    private List<Pair<Unit, Point2D.Double>> units;
    
    public Player() {
        money = 0;
        income = Game.STARTING_INCOME;
        buildings = new ArrayList<Pair<Building, Point>>();
        units = new ArrayList<Pair<Unit, Point2D.Double>>();
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
    
    public void addBuilding(Building building, Point buildLocation) {
        buildings.add(new Pair<Building, Point>(building, buildLocation));
    }
    
    public List<Pair<Building, Point>> getBuildings() {
        return buildings;
    }
    
    public void addUnit(Unit unit, Point2D.Double spawnLocation) {
        units.add(new Pair<Unit, Point2D.Double>(unit, spawnLocation));
    }
    
    public List<Pair<Unit, Point2D.Double>> getUnits() {
        return units;
    }
    
}
