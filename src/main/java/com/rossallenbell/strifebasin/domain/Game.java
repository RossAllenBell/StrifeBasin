package com.rossallenbell.strifebasin.domain;

import java.awt.Point;
import java.util.Map;

import com.rossallenbell.strifebasin.connection.gameupdates.UnitsAndBuildings;
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
        mySantuary.setAssetId(me.getNextAssetId());
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
        
        for(Building building : me.getBuildings().values()){
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

    public Map<Long, Building> getMyBuildings() {
        return me.getBuildings();
    }

    public Map<Long, Building> getTheirBuildings() {
        return them.getBuildings();
    }

    public Map<Long, Unit> getMyUnits() {
        return me.getUnits();
    }

    public Map<Long, Unit> getTheirUnits() {
        return them.getUnits();
    }

    public void updateTheirUnitsAndBuildings(UnitsAndBuildings unitsAndBuildings) {
        Map<Long, Building> theirOriginalBuildings = them.getBuildings();
        Map<Long, Building> theirUpdatedBuildings = unitsAndBuildings.getBuildings();
        for(Long assetId : theirOriginalBuildings.keySet()) {
            if(!theirUpdatedBuildings.containsKey(assetId)) {
                theirOriginalBuildings.remove(assetId);
            }
        }
        for(Long assetId : theirUpdatedBuildings.keySet()) {
            Building updatedBuilding = theirUpdatedBuildings.get(assetId);
            if(!theirOriginalBuildings.containsKey(assetId)) {
                theirOriginalBuildings.put(assetId, updatedBuilding);
            } else {
                Building originalBuilding = theirOriginalBuildings.get(assetId);
                originalBuilding.update(updatedBuilding);
            }
            theirOriginalBuildings.get(assetId).setLocation(BOARD_WIDTH - updatedBuilding.getLocation().x - updatedBuilding.getShape().width, updatedBuilding.getLocation().y);
        }
        
        Map<Long, Unit> theirOriginalUnits = them.getUnits();
        Map<Long, Unit> theirUpdatedUnits = unitsAndBuildings.getUnits();
        for(Long assetId : theirOriginalUnits.keySet()) {
            if(!theirUpdatedUnits.containsKey(assetId)) {
                theirOriginalUnits.remove(assetId);
            }
        }
        for(Long assetId : theirUpdatedUnits.keySet()) {
            Unit updatedUnit = theirUpdatedUnits.get(assetId);
            if(!theirOriginalUnits.containsKey(assetId)) {
                theirOriginalUnits.put(assetId, updatedUnit);
            } else {
                Unit originalUnit = theirOriginalUnits.get(assetId);
                originalUnit.update(updatedUnit);
            }
            theirOriginalUnits.get(assetId).setLocation(BOARD_WIDTH - updatedUnit.getLocation().x, updatedUnit.getLocation().y);
        }
    }
    
}
