package com.rossallenbell.strifebasin.domain.units;

import com.rossallenbell.strifebasin.domain.Me;
import com.rossallenbell.strifebasin.ui.effects.Effect;
import com.rossallenbell.strifebasin.ui.effects.imaged.Arrow;
import com.rossallenbell.strifebasin.ui.resources.HasAnimation;

@HasAnimation
public class Ballista extends PlayerUnit {
    
    public Ballista(Me owner) {
        super(owner);
    }

    @Override
    public int getMaxHealth() {
        return 80;
    }
    
    @Override
    public double getDamage() {
        return 20;
    }
    
    @Override
    public double getRange(){
        return 15;
    }
    
    @Override
    public Class<? extends Effect> getAttackEffect() {
        return Arrow.class;
    }
    
}
