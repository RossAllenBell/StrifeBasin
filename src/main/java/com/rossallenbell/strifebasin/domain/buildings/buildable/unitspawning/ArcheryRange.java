package com.rossallenbell.strifebasin.domain.buildings.buildable.unitspawning;

import com.rossallenbell.strifebasin.domain.Me;
import com.rossallenbell.strifebasin.domain.buildings.buildable.BasicBuilding;
import com.rossallenbell.strifebasin.domain.units.Bowman;
import com.rossallenbell.strifebasin.domain.units.PlayerUnit;
import com.rossallenbell.strifebasin.ui.resources.HasImage;

@HasImage
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
    protected Class<? extends PlayerUnit> getUnit() {
        return Bowman.class;
    }
    
}
