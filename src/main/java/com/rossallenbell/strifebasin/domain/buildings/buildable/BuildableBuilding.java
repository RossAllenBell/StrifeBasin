package com.rossallenbell.strifebasin.domain.buildings.buildable;

import com.rossallenbell.strifebasin.domain.Player;
import com.rossallenbell.strifebasin.domain.buildings.Building;

public abstract class BuildableBuilding extends Building {
    
    public BuildableBuilding(Player owner) {
        super(owner);
    }

    public abstract int cost();
    
}