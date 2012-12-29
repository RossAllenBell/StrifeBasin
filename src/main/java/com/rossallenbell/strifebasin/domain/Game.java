package com.rossallenbell.strifebasin.domain;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.List;

import org.javatuples.Pair;

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
    
    public Game() {
        me = new Player();
        them = new Player();
        
        Point HQLocation = new Point(0,BOARD_HEIGHT/2-new Sanctuary().getShape().height);
        me.addBuilding(new Sanctuary(), HQLocation);
        
        Point theirHQLocation = new Point(BOARD_WIDTH-new Sanctuary().getShape().width,HQLocation.y);
        them.addBuilding(new Sanctuary(), theirHQLocation);
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
        
        for(Pair<Building, Point> ownedBuilding : me.getBuildings()){
            Building building = ownedBuilding.getValue0();
            Point buildingLocation = ownedBuilding.getValue1();
            if(UnitSpawingBuilding.class.isAssignableFrom(building.getClass())){
                UnitSpawingBuilding spawner = (UnitSpawingBuilding) building;
                if(spawner.getLastSpawnTime() + spawner.getSpawnCooldown() <= updateTime){
                    Unit spawnedUnit = spawner.spawn(updateTime);
                    double x = buildingLocation.getX() + building.getShape().width;
                    double y = buildingLocation.getY() + ((double) building.getShape().height / 2);
                    me.addUnit(spawnedUnit, new Point2D.Double(x,y));
                }
            }
        }
    }

    public void buildingPlaced(BuildableBuilding building, Point buildLocation) {
        if(me.getMoney() >= building.cost()){
            me.alterMoney(-1 * building.cost());
            me.addBuilding(building, buildLocation);
        }
        
    }

    public List<Pair<Building, Point>> getMyBuildings() {
        return me.getBuildings();
    }

    public List<Pair<Building, Point>> getTheirBuildings() {
        return them.getBuildings();
    }

    public List<Pair<Unit, Point2D.Double>> getMyUnits() {
        return me.getUnits();
    }

    public List<Pair<Unit, Point2D.Double>> getTheirUnits() {
        return them.getUnits();
    }
    
}
