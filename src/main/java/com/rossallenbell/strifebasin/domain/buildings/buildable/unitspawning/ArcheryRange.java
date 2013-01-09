package com.rossallenbell.strifebasin.domain.buildings.buildable.unitspawning;

import java.awt.Dimension;

import com.rossallenbell.strifebasin.domain.Me;
import com.rossallenbell.strifebasin.domain.buildings.buildable.BasicBuilding;
import com.rossallenbell.strifebasin.domain.units.Bowman;
import com.rossallenbell.strifebasin.domain.units.PlayerUnit;

@BasicBuilding
public class ArcheryRange extends UnitSpawingBuilding {
    
    public ArcheryRange(Me owner) {
        super(owner);
    }

    @Override
    public int cost() {
        return 15;
    }

    @Override
    public Dimension getShape() {
        return new Dimension(4,4);
    }

    @Override
    protected Class<? extends PlayerUnit> getUnit() {
        return Bowman.class;
    }

    public int getMaxHealth() {
        return 100;
    }
    
}
