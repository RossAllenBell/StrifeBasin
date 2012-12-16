package com.rossallenbell.strifebasin.threads;

import com.rossallenbell.strifebasin.ui.Canvas;

public class GameLoop implements Runnable {
    
    Canvas canvas;
    
    public GameLoop(Canvas canvas) {
        this.canvas = canvas;
    }
    
    @Override
    public void run() {
        while (true) {
            long loopStartTime = System.currentTimeMillis();
            
            canvas.repaint();
            
            long loopStopTime = System.currentTimeMillis();
            try {
                Thread.sleep(Math.max(0, (long) (16.667 - (loopStopTime - loopStartTime))));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    
}
