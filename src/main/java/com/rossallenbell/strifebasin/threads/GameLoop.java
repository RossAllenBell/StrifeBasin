package com.rossallenbell.strifebasin.threads;

import com.rossallenbell.strifebasin.connection.protocol.Ping;
import com.rossallenbell.strifebasin.domain.Game;
import com.rossallenbell.strifebasin.ui.Canvas;

public class GameLoop extends StoppableThread {
    
    private static final long COMM_UPDATE_INTERVAL = 250;
    private static final long PING_INTERVAL = 1000;
    
    private long lastCommUpdateTime;
    private long lastPingTime;
    
    private static GameLoop theInstance;
    
    public static GameLoop getInstance() {
        if (theInstance == null) {
            theInstance = new GameLoop();
        }
        return theInstance;
    }
    
    private GameLoop() {
        super();
    }
    
    @Override
    public void run() {
        while (isRunning()) {
            try {
                long loopStartTime = System.currentTimeMillis();
                
                if (lastCommUpdateTime + COMM_UPDATE_INTERVAL <= loopStartTime) {
                    CommSocketSender.getInstance().enqueue(Game.getInstance().getMe().snapshot());
                    lastCommUpdateTime = loopStartTime;
                }
                
                if (lastPingTime + PING_INTERVAL <= loopStartTime) {
                    CommSocketSender.getInstance().enqueue(new Ping());
                    lastPingTime = loopStartTime;
                }
                
                Game.getInstance().update(loopStartTime);
                Canvas.getInstance().repaint();
                
                long loopStopTime = System.currentTimeMillis();
                try {
                    Thread.sleep(Math.max(0, (long) (16.667 - (loopStopTime - loopStartTime))));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
}
