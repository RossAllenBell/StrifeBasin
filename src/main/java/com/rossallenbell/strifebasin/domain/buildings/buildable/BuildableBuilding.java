package com.rossallenbell.strifebasin.domain.buildings.buildable;

import com.rossallenbell.strifebasin.domain.buildings.Building;

public abstract class BuildableBuilding extends Building {
    
    private static final long serialVersionUID = 1L;
    
    public abstract int cost();
    
}