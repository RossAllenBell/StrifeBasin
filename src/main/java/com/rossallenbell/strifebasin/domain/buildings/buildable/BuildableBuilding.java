package com.rossallenbell.strifebasin.domain.buildings.buildable;

import com.rossallenbell.strifebasin.domain.Player;
import com.rossallenbell.strifebasin.domain.buildings.Building;

public abstract class BuildableBuilding extends Building {
    
    public BuildableBuilding(Player owner) {
        super(owner);
    }

    private static final long serialVersionUID = 1L;
    
    public abstract int cost();
    
}