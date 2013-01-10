package com.rossallenbell.strifebasin.connection.gameevents;

import com.rossallenbell.strifebasin.connection.CommObject;
import com.rossallenbell.strifebasin.connection.domain.NetworkUnit;
import com.rossallenbell.strifebasin.domain.Asset;

public class AttackEvent extends CommObject {
    
    private static final long serialVersionUID = 1L;
    
    private final NetworkUnit unit;
    private final Asset target;
    
    public AttackEvent(NetworkUnit unit, Asset target) {
        this.unit = unit;
        this.target = target;
    }
    
    public NetworkUnit getUnit() {
        return unit;
    }
    
    public Asset getTarget() {
        return target;
    }
    
}
