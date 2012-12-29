package com.rossallenbell.strifebasin.threads;

import static com.rossallenbell.strifebasin.StrifeBasin.game;
import com.rossallenbell.strifebasin.ui.Canvas;

public class GameLoop implements Runnable {
    
    private final Canvas canvas;
    
    public GameLoop(Canvas canvas) {
        this.canvas = canvas;
    }
    
    @Override
    public void run() {
        while (true) {
            try {
                long loopStartTime = System.currentTimeMillis();
                
                game.update(loopStartTime);
                canvas.repaint();
                
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
