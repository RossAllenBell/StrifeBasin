package com.rossallenbell.strifebasin.domain.buildings.buildable.unitspawning;

import java.awt.Dimension;

import com.rossallenbell.strifebasin.domain.Me;
import com.rossallenbell.strifebasin.domain.buildings.buildable.AdvancedBuilding;
import com.rossallenbell.strifebasin.domain.units.Baron;
import com.rossallenbell.strifebasin.domain.units.PlayerUnit;
import com.rossallenbell.strifebasin.ui.resources.HasImage;

@HasImage
@AdvancedBuilding
public class Manor extends UnitSpawingBuilding {
    
    public Manor(Me owner) {
        super(owner);
    }

    @Override
    public int cost() {
        return 800;
    }

    @Override
    public Dimension getShape() {
        return new Dimension(6,6);
    }

    @Override
    protected Class<? extends PlayerUnit> getUnit() {
        return Baron.class;
    }
    
}
