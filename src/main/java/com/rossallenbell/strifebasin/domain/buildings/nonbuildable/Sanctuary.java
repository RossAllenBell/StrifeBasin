package com.rossallenbell.strifebasin.domain.buildings.nonbuildable;

import java.awt.Dimension;

import com.rossallenbell.strifebasin.domain.Player;
import com.rossallenbell.strifebasin.domain.buildings.Building;

public class Sanctuary extends Building {
    
    public Sanctuary(Player owner) {
        super(owner);
    }

    @Override
    public Dimension getShape() {
        return new Dimension(6,6);
    }

    public int getMaxHealth() {
        return 0;
    }
    
}
