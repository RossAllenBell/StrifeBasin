package com.rossallenbell.strifebasin.domain.util;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import com.rossallenbell.strifebasin.domain.Asset;
import com.rossallenbell.strifebasin.domain.Player;
import com.rossallenbell.strifebasin.domain.buildings.Building;
import com.rossallenbell.strifebasin.domain.buildings.buildable.BuildableBuilding;
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
    
    public Asset getClosestAggroableAsset(Unit unit, Player theOtherPlayer) {
        Asset target = null;
        
        for (Unit theirUnit : theOtherPlayer.getUnits().values()) {
            double distanceToTheirUnit = theirUnit.getHitLocation().distance(unit.getLocation());
            if (distanceToTheirUnit <= unit.getAggroRange()) {
                if (target == null || target.getHitLocation().distance(unit.getLocation()) > distanceToTheirUnit) {
                    target = theirUnit;
                }
            }
        }
        
        if (target == null) {
            for (Asset building : theOtherPlayer.getBuildings().values()) {
                double distanceToTheirBuilding = building.getHitLocation().distance(unit.getLocation());
                if (distanceToTheirBuilding <= unit.getAggroRange()) {
                    if (target == null || target.getHitLocation().distance(unit.getLocation()) > distanceToTheirBuilding) {
                        target = building;
                    }
                }
            }
        }
        
        return target;
    }
    
    public List<Point2D.Double> getRoute(Unit unit, Asset target) {
        List<Point2D.Double> route = new ArrayList<Point2D.Double>();
        route.add(target.getHitLocation());
        return route;
    }
    
    public static boolean canHitAsset(Unit unit, Asset target) {
        if (target != null) {
            double distanceToTarget = target.getHitLocation().distance(unit.getLocation());
            return distanceToTarget <= unit.getRange() + (target.getSize() / 2);
        }
        return false;
    }
    
    public static double getDirection(Unit unit, Point2D.Double location) {        
        return Math.atan2(location.x - unit.getLocation().x, location.y - unit.getLocation().y);
    }

    public boolean buildingsOverlap(BuildableBuilding buildingPreview, Building otherBuilding) {
        return buildingPreview.getLocation().x < otherBuilding.getLocation().x + otherBuilding.getSize() &&
                buildingPreview.getLocation().x + buildingPreview.getSize() > otherBuilding.getLocation().x &&
                buildingPreview.getLocation().y < otherBuilding.getLocation().y + otherBuilding.getSize() &&
                buildingPreview.getLocation().y + buildingPreview.getSize() > otherBuilding.getLocation().y;
    }
    
}
