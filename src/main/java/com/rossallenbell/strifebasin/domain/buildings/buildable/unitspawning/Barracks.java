package com.rossallenbell.strifebasin.domain.buildings.buildable.unitspawning;

import java.awt.Dimension;

import com.rossallenbell.strifebasin.domain.buildings.buildable.BasicBuilding;
import com.rossallenbell.strifebasin.domain.units.Spearman;
import com.rossallenbell.strifebasin.domain.units.Unit;

@BasicBuilding
public class Barracks extends UnitSpawingBuilding {

    @Override
    public int cost() {
        return 10;
    }

    @Override
    public Dimension getShape() {
        return new Dimension(4,4);
    }

    @Override
    protected Class<? extends Unit> getUnit() {
        return Spearman.class;
    }
    
}
