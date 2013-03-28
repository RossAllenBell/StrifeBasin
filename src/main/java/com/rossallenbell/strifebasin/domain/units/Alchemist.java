package com.rossallenbell.strifebasin.domain.units;

import com.rossallenbell.strifebasin.domain.Me;
import com.rossallenbell.strifebasin.ui.resources.HasAnimation;

@HasAnimation
public class Alchemist extends PlayerUnit {
    
    public Alchemist(Me owner) {
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
    
    @Override
    public double getRange(){
        return 5;
    }
    
}
