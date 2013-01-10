package com.rossallenbell.strifebasin.connection.protocol;

import com.rossallenbell.strifebasin.connection.CommObject;

public class Ping extends CommObject {

    private static final long serialVersionUID = 1L;
    
    private long sentTime;
    
    public Ping() {
        sentTime = System.currentTimeMillis();
    }

    public long getSentTime() {
        return sentTime;
    }
    
}
