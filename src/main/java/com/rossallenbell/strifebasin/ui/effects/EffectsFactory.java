package com.rossallenbell.strifebasin.ui.effects;

import com.rossallenbell.strifebasin.domain.Asset;
import com.rossallenbell.strifebasin.domain.units.Unit;
import com.rossallenbell.strifebasin.ui.effects.imaged.Arrow;

public class EffectsFactory {
    
    private static EffectsFactory theInstance;
    
    public static EffectsFactory getInstance() {
        if (theInstance == null) {
            synchronized (theInstance) {
                if (theInstance == null) {
                    theInstance = new EffectsFactory();
                }
            }
        }
        return theInstance;
    }
    
    public Effect buildEffect(Unit unit, Asset target) {
        return buildEffect(unit, target, System.currentTimeMillis());
    }
    
    public Effect buildEffect(Unit unit, Asset target, long updateTime) {
        Class<? extends Effect> attackEffect = unit.getAttackEffect();
        if (attackEffect != null) {
            if (attackEffect == Arrow.class) {
                return new Arrow(updateTime, unit.getLocation(), target.getHitLocation());
            }
        }
        
        return null;
    }
    
}
