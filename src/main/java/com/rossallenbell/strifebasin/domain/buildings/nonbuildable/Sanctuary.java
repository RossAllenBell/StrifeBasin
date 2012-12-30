package com.rossallenbell.strifebasin.domain.buildings.nonbuildable;

import java.awt.Dimension;

import com.rossallenbell.strifebasin.domain.buildings.Building;

public class Sanctuary extends Building {
    
    private static final long serialVersionUID = 1L;

    @Override
    public Dimension getShape() {
        return new Dimension(6,6);
    }
    
}
