package com.rossallenbell.strifebasin.domain.buildings.nonbuildable;

import java.awt.Dimension;

import com.rossallenbell.strifebasin.domain.buildings.Building;

public class Sanctuary extends Building {

    @Override
    public int cost() {
        return 0;
    }

    @Override
    public Dimension getShape() {
        return new Dimension(6,6);
    }
    
}
