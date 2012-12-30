package com.rossallenbell.strifebasin.domain;

import java.io.Serializable;

public class Asset implements Serializable{
    
    private static final long serialVersionUID = 1L;
    
    private long id;

    public void setAssetId(long assetId) {
        id = assetId;
    }
    
    public long getAssetId() {
        return id;
    }
    
}
