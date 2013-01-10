package com.rossallenbell.strifebasin.connection.protocol;

import com.rossallenbell.strifebasin.connection.CommObject;

public class Pong extends CommObject {

    private static final long serialVersionUID = 1L;
    
    private Ping ping;
    private long receivedTime;

    public Pong(Ping ping) {
        this.ping = ping;
    }
    
    public Pong receive() {
        receivedTime = System.currentTimeMillis();
        return this;
    }

    public long getRoundtripTime() {
        return receivedTime - ping.getSentTime();
    }
    
}
