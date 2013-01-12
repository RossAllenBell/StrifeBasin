package com.rossallenbell.strifebasin.domain;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.Iterator;
import java.util.Map;

import com.rossallenbell.strifebasin.connection.domain.NetworkAsset;
import com.rossallenbell.strifebasin.connection.domain.NetworkBuilding;
import com.rossallenbell.strifebasin.connection.domain.NetworkPlayer;
import com.rossallenbell.strifebasin.connection.domain.NetworkUnit;
import com.rossallenbell.strifebasin.connection.gameevents.AttackEvent;
import com.rossallenbell.strifebasin.domain.buildings.Building;
import com.rossallenbell.strifebasin.domain.buildings.buildable.BuildableBuilding;
import com.rossallenbell.strifebasin.domain.buildings.nonbuildable.Sanctuary;
import com.rossallenbell.strifebasin.domain.units.PlayerUnit;
import com.rossallenbell.strifebasin.domain.util.Pathing;

public class Game {
    
    public static final int BOARD_WIDTH = 250;
    public static final int BOARD_HEIGHT = 125;
    public static final double STARTING_INCOME = 10;
    public static final long INCOME_COOLDOWN = 10000;
    
    private final Me me;
    private NetworkPlayer them;
    
    private static Game theInstance;
    
    private long lastUpdateTime;
    
    private BuildableBuilding buildingPreview;
    
    public static Game getInstance() {
        if (theInstance == null) {
            theInstance = new Game();
        }
        return theInstance;
    }
    
    private Game() {
        me = new Me();
        them = new NetworkPlayer();
        
        Sanctuary mySantuary = new Sanctuary(me);
        mySantuary.setLocation(0, BOARD_HEIGHT / 2 - mySantuary.getShape().height);
        mySantuary.setAssetId(me.getNextAssetId());
        me.addBuilding(mySantuary);
    }
    
    public Me getMe() {
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
        
        Iterator<Building> buildings = me.getBuildings().values().iterator();
        while (buildings.hasNext()) {
            Building building = buildings.next();
            if (building.getHealth() > 0) {
                building.update(updateTime);
            } else if (building.getHealth() <= 0 && !(building instanceof Sanctuary)) {
                buildings.remove();
            }
        }
        
        Iterator<PlayerUnit> units = me.getUnits().values().iterator();
        while (units.hasNext()) {
            PlayerUnit unit = units.next();
            if (unit.getHealth() > 0) {
                unit.update(updateTime);
            } else if (unit.getHealth() <= 0) {
                units.remove();
            }
        }
        
        for (NetworkUnit unit : them.getUnits().values()) {
            double moveDistance = ((updateTime - lastUpdateTime) / 1000.0) * unit.getSpeed();
            Asset target = me.getAssetById(unit.getTargetId());
            if (target == null) {
                // guess the next target until we get an update
                target = Pathing.getInstance().getClosestAggroableAsset(unit, Game.getInstance().getMe());
            }
            if (target != null && !Pathing.canHitAsset(unit, target)) {
                Point2D.Double destination = unit.getCurrentDestination();
                Point2D.Double location = unit.getLocation();
                double distanceToDestination = destination.distance(location);
                if (distanceToDestination <= moveDistance) {
                    location.setLocation(destination);
                } else if (moveDistance > 0) {
                    Point2D.Double currentLocation = location;
                    double direction = Pathing.getDirection(unit, destination);
                    double dx = Math.sin(direction) * moveDistance;
                    double dy = Math.cos(direction) * moveDistance;
                    location.setLocation(currentLocation.x + dx, currentLocation.y + dy);
                }
            }
        }
        
        lastUpdateTime = updateTime;
    }
    
    public void buildingPlaced(Point buildLocation) {
        if(buildingPreview != null) {
            BuildableBuilding building;
            try {
                building = buildingPreview.getClass().getConstructor(Me.class).newInstance(Game.getInstance().getMe());
                building.setLocation(buildLocation.x, buildLocation.y);
                if (me.getMoney() >= building.cost()) {
                    me.alterMoney(-1 * building.cost());
                    me.addBuilding(building);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    public Map<Long, Building> getMyBuildings() {
        return me.getBuildings();
    }
    
    public Map<Long, NetworkBuilding> getTheirBuildings() {
        return them.getBuildings();
    }
    
    public Map<Long, PlayerUnit> getMyUnits() {
        return me.getUnits();
    }
    
    public Map<Long, NetworkUnit> getTheirUnits() {
        return them.getUnits();
    }
    
    public void updateTheirUnitsAndBuildings(NetworkPlayer networkPlayer) {
        them = networkPlayer;
        for (NetworkAsset building : them.getBuildings().values()) {
            double mirroedLocationX = BOARD_WIDTH - building.getLocation().x - building.getSize();
            double mirroredLocationY = building.getLocation().y;
            building.getLocation().setLocation(mirroedLocationX, mirroredLocationY);
        }
        for (NetworkUnit unit : them.getUnits().values()) {
            double mirroredLocationX = BOARD_WIDTH - unit.getLocation().x;
            double mirroredLocationY = unit.getLocation().y;
            unit.getLocation().setLocation(mirroredLocationX, mirroredLocationY);
            double mirroredDestinationX = BOARD_WIDTH - unit.getCurrentDestination().x;
            double mirroredDestinationY = unit.getCurrentDestination().y;
            unit.getCurrentDestination().setLocation(mirroredDestinationX, mirroredDestinationY);
        }
    }
    
    public void attackEvent(AttackEvent attackEvent) {
        PlayerAsset asset = me.getAssetById(attackEvent.getTarget().getAssetId());
        if (asset != null) {
            asset.takeDamage(attackEvent.getUnit());
        }
    }
    
    public void setBuildingPreview(Class<? extends BuildableBuilding> clazz) {
        try {
            buildingPreview = clazz.getConstructor(Me.class).newInstance(Game.getInstance().getMe());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void clearBuildingPreview() {
        buildingPreview = null;
    }

    public BuildableBuilding getBuildingPreview() {
        return buildingPreview;
    }
    
}
