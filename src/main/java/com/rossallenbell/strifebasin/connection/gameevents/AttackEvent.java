package com.rossallenbell.strifebasin.connection.gameevents;

import com.rossallenbell.strifebasin.connection.CommObject;
import com.rossallenbell.strifebasin.connection.domain.NetworkUnit;
import com.rossallenbell.strifebasin.domain.Asset;

public class AttackEvent extends CommObject {
    
    private static final long serialVersionUID = 1L;
    
    public final long unitId;
    public final long targetId;
    
    public AttackEvent(NetworkUnit unit, Asset target) {
        unitId = unit.getAssetId();
        targetId = target.getAssetId();
    }
    
}
