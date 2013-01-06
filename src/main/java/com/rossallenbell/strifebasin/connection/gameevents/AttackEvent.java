package com.rossallenbell.strifebasin.connection.gameevents;

import com.rossallenbell.strifebasin.connection.CommObject;
import com.rossallenbell.strifebasin.connection.domain.NetworkAsset;
import com.rossallenbell.strifebasin.connection.domain.NetworkUnit;

public class AttackEvent extends CommObject {
    
    private static final long serialVersionUID = 1L;
    
    private final NetworkUnit unit;
    private final NetworkAsset target;
    
    public AttackEvent(NetworkUnit unit, NetworkAsset target) {
        this.unit = unit;
        this.target = target;
    }
    
    public NetworkUnit getUnit() {
        return unit;
    }
    
    public NetworkAsset getTarget() {
        return target;
    }
    
}
