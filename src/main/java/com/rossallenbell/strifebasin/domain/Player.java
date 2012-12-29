package com.rossallenbell.strifebasin.domain;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import org.javatuples.Pair;

import com.rossallenbell.strifebasin.domain.buildings.Building;

public class Player {
    
    private int money;
    private int income;
    private long lastIncomeTime;
    
    private List<Pair<Building, Point>> buildings;
    
    public Player() {
        money = 0;
        income = Game.STARTING_INCOME;
        buildings = new ArrayList<Pair<Building, Point>>();
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
    
}
