package com.rossallenbell.strifebasin.domain.units;

import com.rossallenbell.strifebasin.domain.Me;
import com.rossallenbell.strifebasin.ui.resources.HasAnimation;

@HasAnimation
public class Baron extends PlayerUnit {
    
    public Baron(Me owner) {
        super(owner);
    }

    @Override
    public int getMaxHealth() {
        return 200;
    }
    
    @Override
    public double getDamage() {
        return 55;
    }
    
    @Override
    public double getSpeed() {
        return 4;
    }
    
}
