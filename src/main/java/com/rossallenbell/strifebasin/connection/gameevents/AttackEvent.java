package com.rossallenbell.strifebasin.connection.gameevents;

import com.rossallenbell.strifebasin.connection.CommObject;
import com.rossallenbell.strifebasin.domain.Asset;
import com.rossallenbell.strifebasin.domain.units.Unit;

public class AttackEvent extends CommObject {
    
    private static final long serialVersionUID = 1L;
    
    private final Unit unit;
    private final Asset target;
    
    public AttackEvent(Unit unit, Asset target) {
        this.unit = unit;
        this.target = target;
    }

    public Unit getUnit() {
        return unit;
    }

    public Asset getTarget() {
        return target;
    }
    
}
