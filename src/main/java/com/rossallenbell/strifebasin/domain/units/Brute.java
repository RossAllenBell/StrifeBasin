package com.rossallenbell.strifebasin.domain.units;

import com.rossallenbell.strifebasin.domain.Me;
import com.rossallenbell.strifebasin.ui.resources.HasAnimation;

@HasAnimation
public class Brute extends PlayerUnit {
    
    public Brute(Me owner) {
        super(owner);
    }

    @Override
    public int getMaxHealth() {
        return 25;
    }
    
    @Override
    public double getDamage() {
        return 7;
    }
    
    @Override
    public double getSpeed() {
        return 3;
    }
    
    @Override
    public double getAttackSpeed() {
        return 1500;
    }
    
}
