package com.rossallenbell.strifebasin.domain.buildings.buildable.unitspawning;

import com.rossallenbell.strifebasin.domain.Me;
import com.rossallenbell.strifebasin.domain.buildings.buildable.BasicBuilding;
import com.rossallenbell.strifebasin.domain.units.Brute;
import com.rossallenbell.strifebasin.domain.units.PlayerUnit;
import com.rossallenbell.strifebasin.ui.resources.HasImage;

@HasImage
@BasicBuilding
public class MeadHall extends UnitSpawingBuilding {
    
    public MeadHall(Me owner) {
        super(owner);
    }

    @Override
    public int cost() {
        return 25;
    }

    @Override
    protected Class<? extends PlayerUnit> getUnit() {
        return Brute.class;
    }
    
}
