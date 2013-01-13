package com.rossallenbell.strifebasin.threads;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.rossallenbell.strifebasin.connection.protocol.Ping;
import com.rossallenbell.strifebasin.domain.Game;
import com.rossallenbell.strifebasin.ui.Canvas;

public class GameLoop extends StoppableThread {
    
    private static final double DESIRED_FRAME_DURATION = 1000.0/60.0;
    private static final long COMM_UPDATE_INTERVAL = 250;
    private static final long PING_INTERVAL = 1000;
    private static final int FPS_HISTORY = 10;
    
    private long lastCommUpdateTime;
    private long lastPingTime;
    
    private List<Long> loopTimes;
    
    private static GameLoop theInstance;
    
    public static GameLoop getInstance() {
        if (theInstance == null) {
            theInstance = new GameLoop();
        }
        return theInstance;
    }
    
    private GameLoop() {
        super();
        loopTimes = Collections.synchronizedList(new ArrayList<Long>(FPS_HISTORY));
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
                
                synchronized(loopTimes) {
                    if(loopTimes.size() > FPS_HISTORY - 1) {
                        loopTimes.remove(0);
                    }
                    loopTimes.add(loopStartTime);
                }
                
                try {
                    Thread.sleep(Math.max(0, (long) (DESIRED_FRAME_DURATION - (System.currentTimeMillis() - loopStartTime))));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    public long getFps() {
        if(loopTimes.size() < 2) {
            return 0;
        }

        synchronized(loopTimes) { 
            return 1000 / ((loopTimes.get(loopTimes.size() - 1) - loopTimes.get(0)) / (loopTimes.size() - 1));
        }
    }
    
}
