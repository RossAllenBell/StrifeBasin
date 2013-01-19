package com.rossallenbell.strifebasin.domain.util;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import com.rossallenbell.strifebasin.domain.Asset;
import com.rossallenbell.strifebasin.domain.Game;
import com.rossallenbell.strifebasin.domain.Player;
import com.rossallenbell.strifebasin.domain.buildings.Building;
import com.rossallenbell.strifebasin.domain.buildings.buildable.BuildableBuilding;
import com.rossallenbell.strifebasin.domain.units.PlayerUnit;
import com.rossallenbell.strifebasin.domain.units.Unit;

public class Pathing {
    
    public static final int REROUTE_RANGE = 10;
    public static final double REROUTE_DIRECTION_TEST_STEP = Math.PI * 2.0 / 4.0;
    public static final double REROUTE_MOVE_TEST_BY_SIZE = 0.5;
    
    public static final byte[][] PATHING_MAP = new byte[Game.BOARD_WIDTH][Game.BOARD_HEIGHT];
    public static final byte PATHING_MAP_EMPTY = 0;
    public static final byte PATHING_MAP_ME = 1;
    public static final byte PATHING_MAP_THEM = 2;
    
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
        
        Point2D.Double currentLocation = unit.getLocation();
        double direction = getDirection(currentLocation, route.get(0));
        double moveDistance = REROUTE_MOVE_TEST_BY_SIZE * unit.getSize();
        double dx = Math.sin(direction) * moveDistance;
        double dy = -Math.cos(direction) * moveDistance;
        Point2D.Double newLocation = new Point2D.Double(currentLocation.x + dx, currentLocation.y + dy);
        if (!canMove(unit, newLocation)) {
            double desiredDistanceToTarget = distanceToHitTarget(unit, target);
            List<Point2D.Double> reroute = reroute(unit, route.get(0), desiredDistanceToTarget);
            route.addAll(0, reroute);
        }
        
        return route;
    }
    
    private List<Point2D.Double> reroute(Unit unit, Point2D.Double destination, double desiredDistanceToTarget) {
        Point2D.Double location = unit.getLocation();
        if (location.distance(destination) > REROUTE_RANGE) {
            double direction = getDirection(location, destination);
            double dx = Math.sin(direction) * REROUTE_RANGE;
            double dy = -Math.cos(direction) * REROUTE_RANGE;
            destination = new Point2D.Double(location.x + dx, location.y + dy);
            desiredDistanceToTarget = unit.getSize() * 2;
        }
        
        return getLocalRoute(unit, destination, desiredDistanceToTarget);
    }
    
    private List<Point2D.Double> getLocalRoute(Unit unit, Point2D.Double destination, double desiredDistanceToTarget) {
        // boolean[][] localArea = getLocalAreaGrid(unit.getLocation());
        
        Point2D.Double goal = new Point2D.Double(destination.x, destination.y);
        Point2D.Double start = new Point2D.Double(unit.getLocation().x, unit.getLocation().y);
        Set<Point2D.Double> closedSet = new HashSet<Point2D.Double>();
        SortedMap<Double, Point2D.Double> openSet = new TreeMap<Double, Point2D.Double>();
        openSet.put(0.0, start);
        Map<Point2D.Double, Point2D.Double> predecessors = new HashMap<Point2D.Double, Point2D.Double>();
        Map<Point2D.Double, Double> gScores = new HashMap<Point2D.Double, Double>();
        Map<Point2D.Double, Double> fScores = new HashMap<Point2D.Double, Double>();
        
        gScores.put(start, 0.0);
        fScores.put(start, gScores.get(start) + Point.distance(start.x, start.y, goal.x, goal.y));
        
        Point2D.Double current;
        while (!openSet.isEmpty()) {
            Double currentFScore = openSet.firstKey();
            current = openSet.get(currentFScore);
            if (Point.distance(current.x, current.y, goal.x, goal.y) <= desiredDistanceToTarget) {
                List<Point2D.Double> solution = new ArrayList<Point2D.Double>();
                solution.add(current);
                while (predecessors.containsKey(solution.get(0))) {
                    solution.add(0, predecessors.get(solution.get(0)));
                }
                return solution;
            }
            
            openSet.remove(currentFScore);
            closedSet.add(current);
            for (Point2D.Double neighbor : neighbors(unit, current)) {
                if (closedSet.contains(neighbor)) {
                    continue;
                }
                double tentativeGScore = gScores.get(current) + 1;
                
                if (!openSet.containsValue(neighbor) || tentativeGScore <= gScores.get(neighbor)) {
                    predecessors.put(neighbor, current);
                    gScores.put(neighbor, tentativeGScore);
                    fScores.put(neighbor, gScores.get(neighbor) + Point.distance(neighbor.x, neighbor.y, goal.x, goal.y));
                    if (!openSet.containsValue(neighbor)) {
                        openSet.put(fScores.get(neighbor), neighbor);
                    }
                }
            }
        }
        
        return Collections.emptyList();
    }
    
    private Collection<Point2D.Double> neighbors(Unit unit, Point2D.Double currentLocation) {
        Collection<Point2D.Double> neighbors = new HashSet<Point2D.Double>();
        
        Point2D.Double north = new Point2D.Double(currentLocation.x, currentLocation.y - 1);
        if (canMove(unit, north) && Point.distance(unit.getLocation().x, unit.getLocation().y, north.x, north.y) <= REROUTE_RANGE) {
            neighbors.add(north);
        }
        Point2D.Double east = new Point2D.Double(currentLocation.x + 1, currentLocation.y);
        if (canMove(unit, east) && Point.distance(unit.getLocation().x, unit.getLocation().y, east.x, east.y) <= REROUTE_RANGE) {
            neighbors.add(east);
        }
        Point2D.Double south = new Point2D.Double(currentLocation.x, currentLocation.y + 1);
        if (canMove(unit, south) && Point.distance(unit.getLocation().x, unit.getLocation().y, south.x, south.y) <= REROUTE_RANGE) {
            neighbors.add(south);
        }
        Point2D.Double west = new Point2D.Double(currentLocation.x - 1, currentLocation.y);
        if (canMove(unit, west) && Point.distance(unit.getLocation().x, unit.getLocation().y, west.x, west.y) <= REROUTE_RANGE) {
            neighbors.add(west);
        }
        
        return neighbors;
    }
    
    public void updatePathingMap(Unit unit) {
        double x = unit.getLocation().x;
        double y = unit.getLocation().y;
        double radius = unit.getSize() / 2;
        
        int northBounds = (int) Math.floor(y - radius);
        int southBounds = (int) Math.floor(y + radius);
        int westBounds = (int) Math.floor(x - radius);
        int eastBounds = (int) Math.floor(x + radius);
        
        for (int i = westBounds; i <= eastBounds; i++) {
            for (int j = northBounds; j <= southBounds; j++) {
                PATHING_MAP[i][j] = unit.isMine()? PATHING_MAP_ME : PATHING_MAP_THEM;
            }
        }
    }
    
    public void clearPathingMap() {
        for (int i = 0; i < PATHING_MAP.length; i++) {
            for (int j = 0; j < PATHING_MAP[0].length; j++) {
                PATHING_MAP[i][j] = PATHING_MAP_EMPTY;
            }
        }
    }
    
    public boolean canHitAsset(Unit unit, Asset target) {
        if (target != null) {
            double distanceToTarget = target.getHitLocation().distance(unit.getLocation());
            return distanceToTarget <= distanceToHitTarget(unit, target);
        }
        return false;
    }

    private double distanceToHitTarget(Unit unit, Asset target) {
        return unit.getRange() + (target.getSize() / 2);
    }
    
    public double getDirection(Point2D.Double origin, Point2D.Double destination) {
        double dx = destination.x - origin.x;
        double dy = destination.y - origin.y;
        return Math.atan2(dy, dx) + (Math.PI / 2);
    }
    
    public boolean buildingsOverlap(BuildableBuilding buildingPreview, Building otherBuilding) {
        return buildingPreview.getLocation().x < otherBuilding.getLocation().x + otherBuilding.getSize() && buildingPreview.getLocation().x + buildingPreview.getSize() > otherBuilding.getLocation().x && buildingPreview.getLocation().y < otherBuilding.getLocation().y + otherBuilding.getSize() && buildingPreview.getLocation().y + buildingPreview.getSize() > otherBuilding.getLocation().y;
    }
    
    public boolean canMove(Unit unit, Point2D.Double newLocation) {
        Map<Long, PlayerUnit> myUnits = Game.getInstance().getMyUnits();
        synchronized (myUnits) {
            for (Unit myUnit : myUnits.values()) {
                if (myUnit.getAssetId() != unit.getAssetId()) {
                    double newDistance = newLocation.distance(myUnit.getLocation());
                    if (newDistance < (unit.getSize() / 2) + (myUnit.getSize() / 2)) {
                        if (newDistance < unit.getLocation().distance(myUnit.getLocation())) {
                            return false;
                        }
                    }
                }
            }
        }
        
        return true;
    }
    
}
