package com.rossallenbell.strifebasin.threads;

public abstract class StoppableThread implements Runnable {
    
    private boolean running;
    
    public StoppableThread() {
        running = true;
    }
    
    public void stop() {
        running = false;
    }
    
    public boolean isRunning() {
        return running;
    }
    
}
