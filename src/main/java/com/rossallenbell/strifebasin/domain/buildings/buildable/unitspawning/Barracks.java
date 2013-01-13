package com.rossallenbell.strifebasin.domain.buildings.buildable.unitspawning;

import java.awt.Dimension;

import com.rossallenbell.strifebasin.domain.Me;
import com.rossallenbell.strifebasin.domain.buildings.buildable.BasicBuilding;
import com.rossallenbell.strifebasin.domain.units.Spearman;
import com.rossallenbell.strifebasin.domain.units.PlayerUnit;
import com.rossallenbell.strifebasin.ui.resources.HasImage;

@HasImage
@BasicBuilding
public class Barracks extends UnitSpawingBuilding {
    
    public Barracks(Me owner) {
        super(owner);
    }

    @Override
    public int cost() {
        return 10;
    }

    @Override
    public Dimension getShape() {
        return new Dimension(4,4);
    }

    @Override
    protected Class<? extends PlayerUnit> getUnit() {
        return Spearman.class;
    }

    public int getMaxHealth() {
        return 100;
    }
    
}
