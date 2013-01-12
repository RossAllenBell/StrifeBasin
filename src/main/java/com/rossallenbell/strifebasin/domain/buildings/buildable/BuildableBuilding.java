package com.rossallenbell.strifebasin.domain.buildings.buildable;

import com.rossallenbell.strifebasin.domain.Me;
import com.rossallenbell.strifebasin.domain.buildings.Building;

public abstract class BuildableBuilding extends Building {
    
    public BuildableBuilding(Me owner) {
        super(owner);
    }

    public abstract int cost();
    
    @Override
    public double getIncome() {
        return cost() / 10.0;
    }
    
}