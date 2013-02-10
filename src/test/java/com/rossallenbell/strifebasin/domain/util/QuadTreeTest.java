package com.rossallenbell.strifebasin.domain.util;

import static org.junit.Assert.fail;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.rossallenbell.strifebasin.domain.Me;
import com.rossallenbell.strifebasin.domain.units.PlayerUnit;
import com.rossallenbell.strifebasin.domain.units.Spearman;
import com.rossallenbell.strifebasin.domain.util.QuadTree;

public class QuadTreeTest {
    
    double MAX_X = 300;
    final static double MAX_Y = 150;
    final static int UNIT_COUNT = 200;
    final static int UPDATE_LOOPS = 5;
    final static int TEST_QUERIES = 30;
    
    @Test
    public void test() {
        QuadTree<PlayerUnit> quadTree = new QuadTree<>();
        List<PlayerUnit> units = new ArrayList<>();
        Me me = new Me();
        
        for (int i = 0; i < UNIT_COUNT; i++) {
            Spearman unit = new Spearman(me);
            unit.setAssetId(me.getNextAssetId());
            units.add(unit);
        }
        
        for (PlayerUnit unit : units) {
            quadTree.insert(unit);
        }
        
        for (int i = 0; i < UPDATE_LOOPS; i++) {
            quadTree.clear();
            for (PlayerUnit unit : units) {
//                quadTree.remove(unit);
                
                unit.setLocation(Math.random() * MAX_X, Math.random() * MAX_Y);
                
                quadTree.insert(unit);
            }
            
            for (int j = 0; j < TEST_QUERIES; j++) {
                double x = Math.random() * MAX_X;
                double y = Math.random() * MAX_Y;
                Rectangle2D.Double rect = new Rectangle2D.Double(x, y, Math.random() * (MAX_X - x), Math.random() * (MAX_Y - y));
                List<PlayerUnit> query = quadTree.query(rect);
                for(PlayerUnit unit : units) {
                    if(rect.contains(unit.getLocation())){
                        if(!query.contains(unit)) {
                            fail("Quad tree didn't find a unit it should have");
                        }
                    } else if (query.contains(unit)){
                        fail("Quad tree found a unit it should not have");
                    }
                }
            }
        }
        
        List<PlayerUnit> fullQuery = quadTree.query(new Rectangle2D.Double(0,0,MAX_X,MAX_Y));
        int fullQuerySize = fullQuery.size();
        int unitsSize = units.size();
        if(!fullQuery.containsAll(units) || fullQuerySize != unitsSize) {
            fullQuery.removeAll(units);
            fail(String.format("Querying the entire quad tree did not return all units. Query size: %d. Units size: %d.", fullQuerySize, unitsSize));
        }
        
    }
    
}
