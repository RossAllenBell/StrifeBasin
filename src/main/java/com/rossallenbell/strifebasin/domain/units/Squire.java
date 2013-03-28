package com.rossallenbell.strifebasin.domain.units;

import com.rossallenbell.strifebasin.domain.Me;
import com.rossallenbell.strifebasin.ui.resources.HasAnimation;

@HasAnimation
public class Squire extends PlayerUnit {
    
    public Squire(Me owner) {
        super(owner);
    }

    @Override
    public int getMaxHealth() {
        return 50;
    }
    
    @Override
    public double getDamage() {
        return 15;
    }
    
}
