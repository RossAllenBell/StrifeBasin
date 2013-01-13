package com.rossallenbell.strifebasin.domain.units;

import com.rossallenbell.strifebasin.domain.Me;
import com.rossallenbell.strifebasin.ui.resources.HasAnimation;

@HasAnimation
public class Spearman extends PlayerUnit {
    
    public Spearman(Me owner) {
        super(owner);
    }

    @Override
    public int getMaxHealth() {
        return 10;
    }
    
    @Override
    public double getDamage() {
        return 2;
    }
    
}
