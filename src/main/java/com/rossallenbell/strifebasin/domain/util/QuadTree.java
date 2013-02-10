package com.rossallenbell.strifebasin.domain.util;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.rossallenbell.strifebasin.domain.units.Unit;

/*
 * Adapted from version written by Robert Sedgewick and Kevin Wayne
 */

public class QuadTree<T extends Unit> {
    private Node root;
//    Map<Long, Point2D.Double> idsToLocations;
    
    public QuadTree() {
//        idsToLocations = new HashMap<Long, Point2D.Double>();
    }
    
    private class Node {
//        Node parent;
        double x, y;
        Node NW, NE, SE, SW;
        T value;
        
        Node(T value){//, Node parent) {
            this.x = value.getLocation().x;
            this.y = value.getLocation().y;
            this.value = value;
        }
    }
    
    public void clear() {
        root = null;
//        idsToLocations.clear();
    }
    
    public void insert(T unit) {
//        idsToLocations.put(unit.getAssetId(), new Point2D.Double(unit.getLocation().x, unit.getLocation().y));
        root = insert(root, unit.getLocation().x, unit.getLocation().y, unit);
    }
    
    private Node insert(Node node, double x, double y, T unit) {//, Node parentNode, double x, double y, T unit) {
        if (node == null) {
            return new Node(unit);//, parentNode);
        }
        
        if (x == node.x && y == node.y) {
            return node;
        }
        
        if (x < node.x && y < node.y) {
            node.SW = insert(node.SW, x, y, unit);
        } else if (x < node.x && y >= node.y) {
            node.NW = insert(node.NW, x, y, unit);
        } else if (x >= node.x && y < node.y) {
            node.SE = insert(node.SE, x, y, unit);
        } else if (x >= node.x && y >= node.y) {
            node.NE = insert(node.NE, x, y, unit);
        }
        return node;
    }
    
//    public void remove(T unit) {
//        if (idsToLocations.containsKey(unit.getAssetId())) {
//            remove(root, idsToLocations.get(unit.getAssetId()).x, idsToLocations.get(unit.getAssetId()).y);
//            idsToLocations.remove(unit);
//        }
//    }
//    
//    private void remove(Node node, double x, double y) {
//        if (node == null) {
//            return;
//        }
//        
//        if (x == node.x && y == node.y) {
//            node.value = null;
//            attemptToRollUp(node);
//            return;
//        }
//        
//        if (x < node.x && y < node.y) {
//            remove(node.SW, x, y);
//        } else if (x < node.x && y >= node.y) {
//            remove(node.NW, x, y);
//        } else if (x >= node.x && y < node.y) {
//            remove(node.SE, x, y);
//        } else if (x >= node.x && y >= node.y) {
//            remove(node.NE, x, y);
//        }
//    }
//    
//    private void attemptToRollUp(Node node) {
//        if(node == null || node.parent == null || node.value != null) {
//            return;
//        }
//        
//        if(node.SW == null && node.NW == null && node.SE == null && node.NE == null) {
//            if(node.parent.SW == node) {
//                node.parent.SW = null;
//            } else if(node.parent.NW == node) {
//                node.parent.NW = null;
//            } else if(node.parent.SE == node) {
//                node.parent.SE = null;
//            } else if(node.parent.NE == node) {
//                node.parent.NE = null;
//            }
//            attemptToRollUp(node.parent);
//        } else {
//            attemptToRollUp(node.SW);
//            attemptToRollUp(node.NW);
//            attemptToRollUp(node.SE);
//            attemptToRollUp(node.NE);
//        }
//    }

    public List<T> query(Rectangle2D.Double rect) {
        return query(root, rect);
    }
    
    private List<T> query(Node node, Rectangle2D.Double rect) {
        if (node == null) {
            return Collections.emptyList();
        }
        
        List<T> units = new ArrayList<T>();
        
        if (rect.contains(node.x, node.y) && node.value != null) {
            units.add(node.value);
        }
        
        double xmin = rect.x;
        double ymin = rect.y;
        double xmax = rect.x + rect.width;
        double ymax = rect.y + rect.height;
        
        if (xmin < node.x && ymin < node.y) {
            units.addAll(query(node.SW, rect));
        }
        if (xmin < node.x && ymax >= node.y) {
            units.addAll(query(node.NW, rect));
        }
        if (xmax >= node.x && ymin < node.y) {
            units.addAll(query(node.SE, rect));
        }
        if (xmax >= node.x && ymax >= node.y) {
            units.addAll(query(node.NE, rect));
        }
        
        return units;
    }
    
}
