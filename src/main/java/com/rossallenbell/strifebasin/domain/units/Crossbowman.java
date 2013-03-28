package com.rossallenbell.strifebasin.domain.units;

import com.rossallenbell.strifebasin.domain.Me;
import com.rossallenbell.strifebasin.ui.effects.Effect;
import com.rossallenbell.strifebasin.ui.effects.imaged.Arrow;
import com.rossallenbell.strifebasin.ui.resources.HasAnimation;

@HasAnimation
public class Crossbowman extends PlayerUnit {
    
    public Crossbowman(Me owner) {
        super(owner);
    }

    @Override
    public int getMaxHealth() {
        return 30;
    }
    
    @Override
    public double getDamage() {
        return 7;
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
