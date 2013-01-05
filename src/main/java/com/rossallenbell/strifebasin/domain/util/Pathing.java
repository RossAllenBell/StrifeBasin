package com.rossallenbell.strifebasin.domain.util;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import com.rossallenbell.strifebasin.domain.Asset;
import com.rossallenbell.strifebasin.domain.Game;
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
    
    public Asset getClosestAggroableAsset(Unit unit) {
        Asset target = null;
        
        for (Asset theirUnit : Game.getInstance().getThem().getUnits().values()) {
            double distanceToTheirUnit = theirUnit.getLocation().distance(unit.getLocation());
            if (distanceToTheirUnit <= unit.getAggroRange()) {
                if (target == null || target.getLocation().distance(unit.getLocation()) > theirUnit.getLocation().distance(unit.getLocation())) {
                    target = theirUnit;
                }
            }
        }
        
        if (target == null) {
            for (Asset building : Game.getInstance().getThem().getBuildings().values()) {
                double distanceToTheirBuilding = building.getLocation().distance(unit.getLocation());
                if (distanceToTheirBuilding <= unit.getAggroRange()) {
                    if (target == null || target.getLocation().distance(unit.getLocation()) > building.getLocation().distance(unit.getLocation())) {
                        target = building;
                    }
                }
            }
        }
        
        return target;
    }
    
    public List<Point2D.Double> getRoute(Unit unit, Asset target) {
        List<Point2D.Double> route = new ArrayList<Point2D.Double>();
        route.add(target.getLocation());
        return route;
    }
    
}