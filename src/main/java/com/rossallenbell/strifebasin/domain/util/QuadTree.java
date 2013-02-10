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
    
    private class Node {
        double x, y;
        Node NW, NE, SE, SW;
        T value;
        
        Node(T value){
            this.x = value.getLocation().x;
            this.y = value.getLocation().y;
            this.value = value;
        }
    }
    
    public void clear() {
        root = null;
    }
    
    public void insert(T unit) {
        root = insert(root, unit.getLocation().x, unit.getLocation().y, unit);
    }
    
    private Node insert(Node node, double x, double y, T unit) {
        if (node == null) {
            return new Node(unit);
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
