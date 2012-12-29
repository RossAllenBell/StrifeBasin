package com.rossallenbell.strifebasin.domain;

import java.awt.Point;
import java.util.List;

import com.rossallenbell.strifebasin.domain.buildings.Building;
import com.rossallenbell.strifebasin.domain.buildings.buildable.BuildableBuilding;
import com.rossallenbell.strifebasin.domain.buildings.buildable.unitspawning.UnitSpawingBuilding;
import com.rossallenbell.strifebasin.domain.buildings.nonbuildable.Sanctuary;
import com.rossallenbell.strifebasin.domain.units.Unit;


public class Game {
    
    public static final int BOARD_WIDTH = 250;
    public static final int BOARD_HEIGHT = 125;
    public static final int STARTING_INCOME = 10;
    public static final long INCOME_COOLDOWN = 10000;
    
    private final Player me;
    private final Player them;
    
    private static Game theInstance;
    
    public static Game  getInstance() {
        if(theInstance == null){
            theInstance = new Game();
        }
        return theInstance;
    }
    
    private Game() {
        me = new Player();
        them = new Player();
        
        Sanctuary mySantuary = new Sanctuary();
        mySantuary.setLocation(0, BOARD_HEIGHT/2-new Sanctuary().getShape().height);
        me.addBuilding(mySantuary);
    }
    
    public Player getMe(){
        return me;
    }
    
    public Player getThem(){
        return them;
    }
    
    public void wheelIn() {
        
    }
    
    public void wheelOut() {
        
    }
    
    public void keyPressed(int keyCode) {
        
    }
    
    public void keyReleased(int keyCode) {
        
    }

    public void update(long updateTime) {
        if(me.getLastIncomeTime() < updateTime - INCOME_COOLDOWN){
            me.income();
            me.setLastIncomeTime(updateTime);
        }
        
        for(Building building : me.getBuildings()){
            if(UnitSpawingBuilding.class.isAssignableFrom(building.getClass())){
                UnitSpawingBuilding spawner = (UnitSpawingBuilding) building;
                if(spawner.getLastSpawnTime() + spawner.getSpawnCooldown() <= updateTime){
                    Point buildingLocation = building.getLocation();
                    Unit spawnedUnit = spawner.spawn(updateTime);
                    double x = buildingLocation.getX() + building.getShape().width;
                    double y = buildingLocation.getY() + ((double) building.getShape().height / 2);
                    spawnedUnit.setLocation(x, y);
                    me.addUnit(spawnedUnit);
                }
            }
        }
    }

    public void buildingPlaced(BuildableBuilding building) {
        if(me.getMoney() >= building.cost()){
            me.alterMoney(-1 * building.cost());
            me.addBuilding(building);
        }
        
    }

    public List<Building> getMyBuildings() {
        return me.getBuildings();
    }

    public List<Building> getTheirBuildings() {
        return them.getBuildings();
    }

    public List<Unit> getMyUnits() {
        return me.getUnits();
    }

    public List<Unit> getTheirUnits() {
        return them.getUnits();
    }
    
}
