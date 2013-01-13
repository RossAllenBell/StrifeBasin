package com.rossallenbell.strifebasin.domain.units;

import com.rossallenbell.strifebasin.domain.Me;
import com.rossallenbell.strifebasin.ui.resources.HasAnimation;

@HasAnimation
public class Bowman extends PlayerUnit {
    
    public Bowman(Me owner) {
        super(owner);
    }

    @Override
    public int getMaxHealth() {
        return 5;
    }
    
    @Override
    public double getDamage() {
        return 1;
    }
    
    @Override
    public double getRange(){
        return 5;
    }
    
}
