package com.rossallenbell.strifebasin.domain.buildings.nonbuildable;

import java.awt.Dimension;

import com.rossallenbell.strifebasin.domain.Me;
import com.rossallenbell.strifebasin.domain.buildings.Building;
import com.rossallenbell.strifebasin.ui.resources.HasImage;

@HasImage
public class Sanctuary extends Building {
    
    public Sanctuary(Me owner) {
        super(owner);
    }

    @Override
    public Dimension getShape() {
        return new Dimension(8,8);
    }

    public int getMaxHealth() {
        return 2000;
    }
    
}
