package com.rossallenbell.strifebasin.domain.buildings.buildable.unitspawning;

import com.rossallenbell.strifebasin.domain.Me;
import com.rossallenbell.strifebasin.domain.buildings.buildable.BasicBuilding;
import com.rossallenbell.strifebasin.domain.units.PlayerUnit;
import com.rossallenbell.strifebasin.domain.units.Squire;
import com.rossallenbell.strifebasin.ui.resources.HasImage;

@HasImage
@BasicBuilding
public class TacticiansSchool extends UnitSpawingBuilding {
    
    public TacticiansSchool(Me owner) {
        super(owner);
    }

    @Override
    public int cost() {
        return 70;
    }

    @Override
    protected Class<? extends PlayerUnit> getUnit() {
        return Squire.class;
    }
    
}
