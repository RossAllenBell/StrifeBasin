package com.rossallenbell.strifebasin.domain;

import java.util.Map;

import com.rossallenbell.strifebasin.domain.buildings.Building;
import com.rossallenbell.strifebasin.domain.buildings.buildable.BuildableBuilding;
import com.rossallenbell.strifebasin.domain.buildings.nonbuildable.Sanctuary;
import com.rossallenbell.strifebasin.domain.units.Unit;


public class Game {
    
    public static final int BOARD_WIDTH = 250;
    public static final int BOARD_HEIGHT = 125;
    public static final int STARTING_INCOME = 10;
    public static final long INCOME_COOLDOWN = 10000;
    
    private final Player me;
    private Player them;
    
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
        
        Sanctuary mySantuary = new Sanctuary(me);
        mySantuary.setLocation(0, BOARD_HEIGHT/2-mySantuary.getShape().height);
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
            building.update(updateTime);
        }
        
        for(Unit unit : me.getUnits().values()){
            unit.update(updateTime);
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

    public void updateTheirUnitsAndBuildings(Player them) {
        this.them = them;
        for(Building building : them.getBuildings().values()) {
            building.setLocation(BOARD_WIDTH - building.getLocation().x - building.getShape().width, building.getLocation().y);
        }
        for(Unit unit : them.getUnits().values()) {
            unit.setLocation(BOARD_WIDTH - unit.getLocation().x, unit.getLocation().y);
        }
    }
    
}
