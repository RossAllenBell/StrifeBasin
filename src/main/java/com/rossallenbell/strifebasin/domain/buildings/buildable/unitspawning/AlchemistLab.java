package com.rossallenbell.strifebasin.domain.buildings.buildable.unitspawning;

import java.awt.Dimension;

import com.rossallenbell.strifebasin.domain.Me;
import com.rossallenbell.strifebasin.domain.buildings.buildable.AdvancedBuilding;
import com.rossallenbell.strifebasin.domain.units.Alchemist;
import com.rossallenbell.strifebasin.domain.units.PlayerUnit;

@AdvancedBuilding
public class AlchemistLab extends UnitSpawingBuilding {
    
    public AlchemistLab(Me owner) {
        super(owner);
    }

    @Override
    public int cost() {
        return 100;
    }

    @Override
    public Dimension getShape() {
        return new Dimension(8,8);
    }

    @Override
    protected Class<? extends PlayerUnit> getUnit() {
        return Alchemist.class;
    }

    public int getMaxHealth() {
        return 100;
    }
    
}
