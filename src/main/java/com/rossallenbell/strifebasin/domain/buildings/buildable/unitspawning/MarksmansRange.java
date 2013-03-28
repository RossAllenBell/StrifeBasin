package com.rossallenbell.strifebasin.domain.buildings.buildable.unitspawning;

import com.rossallenbell.strifebasin.domain.Me;
import com.rossallenbell.strifebasin.domain.buildings.buildable.BasicBuilding;
import com.rossallenbell.strifebasin.domain.units.Crossbowman;
import com.rossallenbell.strifebasin.domain.units.PlayerUnit;
import com.rossallenbell.strifebasin.ui.resources.HasImage;

@HasImage
@BasicBuilding
public class MarksmansRange extends UnitSpawingBuilding {
    
    public MarksmansRange(Me owner) {
        super(owner);
    }

    @Override
    public int cost() {
        return 45;
    }

    @Override
    protected Class<? extends PlayerUnit> getUnit() {
        return Crossbowman.class;
    }
    
}
