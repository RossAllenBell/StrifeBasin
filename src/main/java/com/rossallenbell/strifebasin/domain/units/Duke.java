package com.rossallenbell.strifebasin.domain.units;

import com.rossallenbell.strifebasin.domain.Me;
import com.rossallenbell.strifebasin.ui.resources.HasAnimation;

@HasAnimation
public class Duke extends PlayerUnit {
    
    public Duke(Me owner) {
        super(owner);
    }

    @Override
    public int getMaxHealth() {
        return 125;
    }
    
    @Override
    public double getDamage() {
        return 35;
    }
    
}
