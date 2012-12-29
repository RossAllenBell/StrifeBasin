package com.rossallenbell.strifebasin.domain.buildings.buildable.unitspawning;

import java.awt.Dimension;

import com.rossallenbell.strifebasin.domain.buildings.buildable.AdvancedBuilding;
import com.rossallenbell.strifebasin.domain.units.Alchemist;
import com.rossallenbell.strifebasin.domain.units.Unit;

@AdvancedBuilding
public class AlchemistLab extends UnitSpawingBuilding {

    @Override
    public int cost() {
        return 100;
    }

    @Override
    public Dimension getShape() {
        return new Dimension(8,8);
    }

    @Override
    protected Class<? extends Unit> getUnit() {
        return Alchemist.class;
    }
    
}
