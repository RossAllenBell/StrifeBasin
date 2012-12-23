package com.rossallenbell.strifebasin.domain.buildings.basic;

import java.awt.Dimension;

import com.rossallenbell.strifebasin.domain.buildings.BasicBuilding;

public class Barracks extends BasicBuilding {

    @Override
    public int cost() {
        return 10;
    }

    @Override
    public Dimension getShape() {
        return new Dimension(4,4);
    }
    
}
