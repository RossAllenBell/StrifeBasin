package com.rossallenbell.strifebasin.domain.buildings.buildable.unitspawning;

import java.awt.Dimension;

import com.rossallenbell.strifebasin.domain.buildings.buildable.BasicBuilding;
import com.rossallenbell.strifebasin.domain.units.Bowman;
import com.rossallenbell.strifebasin.domain.units.Unit;

@BasicBuilding
public class ArcheryRange extends UnitSpawingBuilding {
    
    private static final long serialVersionUID = 1L;

    @Override
    public int cost() {
        return 15;
    }

    @Override
    public Dimension getShape() {
        return new Dimension(4,4);
    }

    @Override
    protected Class<? extends Unit> getUnit() {
        return Bowman.class;
    }
    
}
