package com.rossallenbell.strifebasin.domain.units;

import com.rossallenbell.strifebasin.domain.Me;
import com.rossallenbell.strifebasin.ui.effects.Effect;
import com.rossallenbell.strifebasin.ui.effects.imaged.Arrow;
import com.rossallenbell.strifebasin.ui.resources.HasAnimation;

@HasAnimation
public class Bowman extends PlayerUnit {
    
    public Bowman(Me owner) {
        super(owner);
    }
    
    @Override
    public int getMaxHealth() {
        return 10;
    }
    
    @Override
    public double getDamage() {
        return 1;
    }
    
    @Override
    public double getRange() {
        return 10;
    }
    
    @Override
    public Class<? extends Effect> getAttackEffect() {
        return Arrow.class;
    }
    
}
