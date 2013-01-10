package com.rossallenbell.strifebasin.domain.util;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import com.rossallenbell.strifebasin.connection.domain.NetworkAsset;
import com.rossallenbell.strifebasin.domain.Asset;
import com.rossallenbell.strifebasin.domain.Game;
import com.rossallenbell.strifebasin.domain.units.PlayerUnit;
import com.rossallenbell.strifebasin.domain.units.Unit;

public class Pathing {
    
    private static Pathing theInstance;
    
    public static Pathing getInstance() {
        if (theInstance == null) {
            theInstance = new Pathing();
        }
        return theInstance;
    }
    
    private Pathing() {
        
    }
    
    public NetworkAsset getClosestAggroableAsset(PlayerUnit unit) {
        NetworkAsset target = null;
        
        for (NetworkAsset theirUnit : Game.getInstance().getThem().getUnits()) {
            double distanceToTheirUnit = theirUnit.getLocation().distance(unit.getLocation());
            if (distanceToTheirUnit <= unit.getAggroRange()) {
                if (target == null || target.getLocation().distance(unit.getLocation()) > distanceToTheirUnit) {
                    target = theirUnit;
                }
            }
        }
        
        if (target == null) {
            for (NetworkAsset building : Game.getInstance().getThem().getBuildings()) {
                double distanceToTheirBuilding = building.getLocation().distance(unit.getLocation());
                if (distanceToTheirBuilding <= unit.getAggroRange()) {
                    if (target == null || target.getLocation().distance(unit.getLocation()) > distanceToTheirBuilding) {
                        target = building;
                    }
                }
            }
        }
        
        return target;
    }
    
    public List<Point2D.Double> getRoute(Unit unit, NetworkAsset target) {
        List<Point2D.Double> route = new ArrayList<Point2D.Double>();
        route.add(target.getLocation());
        return route;
    }
    
    public static boolean canHitAsset(Unit unit, Asset target) {
        if (target != null) {
            double distanceToTarget = target.getLocation().distance(unit.getLocation());
            return distanceToTarget <= unit.getRange() + (target.getSize() / 2);
        }
        return false;
    }
    
    public static double getDirection(Unit unit, Point2D.Double location) {        
        return Math.atan2(location.x - unit.getLocation().x, location.y - unit.getLocation().y);
    }
    
}
