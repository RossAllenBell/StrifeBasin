package com.rossallenbell.strifebasin.domain;

import java.util.Iterator;
import java.util.List;

import com.rossallenbell.strifebasin.connection.domain.NetworkAsset;
import com.rossallenbell.strifebasin.connection.domain.NetworkPlayer;
import com.rossallenbell.strifebasin.connection.domain.NetworkUnit;
import com.rossallenbell.strifebasin.connection.gameevents.AttackEvent;
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
    private NetworkPlayer them;
    
    private static Game theInstance;
    
    public static Game getInstance() {
        if (theInstance == null) {
            theInstance = new Game();
        }
        return theInstance;
    }
    
    private Game() {
        me = new Player();
        them = new NetworkPlayer();
        
        Sanctuary mySantuary = new Sanctuary(me);
        mySantuary.setLocation(0, BOARD_HEIGHT / 2 - mySantuary.getShape().height);
        mySantuary.setAssetId(me.getNextAssetId());
        me.addBuilding(mySantuary);
    }
    
    public Player getMe() {
        return me;
    }
    
    public NetworkPlayer getThem() {
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
        if (me.getLastIncomeTime() < updateTime - INCOME_COOLDOWN) {
            me.income();
            me.setLastIncomeTime(updateTime);
        }
        
        Iterator<Building> buildings = me.getBuildings().iterator();
        while (buildings.hasNext()) {
            Building building = buildings.next();
            if (building.getHealth() > 0) {
                building.update(updateTime);
            } else if (building.getHealth() <= 0 && !(building instanceof Sanctuary)) {
                buildings.remove();
            }
        }
        
        Iterator<Unit> units = me.getUnits().iterator();
        while (units.hasNext()) {
            Unit unit = units.next();
            if (unit.getHealth() > 0) {
                unit.update(updateTime);
            } else if (unit.getHealth() <= 0) {
                buildings.remove();
            }
        }
    }
    
    public void buildingPlaced(BuildableBuilding building) {
        if (me.getMoney() >= building.cost()) {
            me.alterMoney(-1 * building.cost());
            me.addBuilding(building);
        }
    }
    
    public List<Building> getMyBuildings() {
        return me.getBuildings();
    }
    
    public List<NetworkAsset> getTheirBuildings() {
        return them.getBuildings();
    }
    
    public List<Unit> getMyUnits() {
        return me.getUnits();
    }
    
    public List<NetworkUnit> getTheirUnits() {
        return them.getUnits();
    }
    
    public void updateTheirUnitsAndBuildings(NetworkPlayer networkPlayer) {
        this.them = networkPlayer;
        for (NetworkAsset building : networkPlayer.getBuildings()) {
            building.getLocation().setLocation(BOARD_WIDTH - building.getLocation().x - building.getSize(), building.getLocation().y);
        }
        for (NetworkAsset unit : networkPlayer.getUnits()) {
            unit.getLocation().setLocation(BOARD_WIDTH - unit.getLocation().x, unit.getLocation().y);
        }
    }

    public void attackEvent(AttackEvent attackEvent) {
        for(Asset building : me.getBuildings()) {
            if (building.getAssetId() == attackEvent.getTarget().getAssetId()) {
                building.takeDamage(attackEvent.getUnit());
            }
        }
    }
    
}
