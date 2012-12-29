package com.rossallenbell.strifebasin.domain.buildings.buildable.advanced;

import java.awt.Dimension;

import com.rossallenbell.strifebasin.domain.buildings.buildable.AdvancedBuilding;

public class AlchemistLab extends AdvancedBuilding {

    @Override
    public int cost() {
        return 100;
    }

    @Override
    public Dimension getShape() {
        return new Dimension(8,8);
    }
    
}
