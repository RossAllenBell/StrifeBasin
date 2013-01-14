package com.rossallenbell.strifebasin.ui.effects;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class EffectsManager {
    
    private List<Effect> effects;
    
    private static EffectsManager theInstance;
    
    public static EffectsManager getInstance() {
        if (theInstance == null) {
            theInstance = new EffectsManager();
        }
        return theInstance;
    }
    
    private EffectsManager() {
        effects = new ArrayList<Effect>();
    }
    
    public void update(long updateTime) {
        synchronized(effects) {
            Iterator<Effect> effectsI = effects.iterator();
            while(effectsI.hasNext()) {
                Effect effect = effectsI.next();
                effect.update(updateTime);
                if(effect.isComplete()) {
                    effectsI.remove();
                }
            }
        }
    }

    public List<Effect> getEffects() {
        return effects;
    }

    public void addEffect(Effect newAttackEffect) {
        synchronized(effects) {
            effects.add(newAttackEffect);
        }        
    }
    
}
