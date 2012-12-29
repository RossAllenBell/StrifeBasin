package com.rossallenbell.strifebasin.threads;

import com.rossallenbell.strifebasin.domain.Game;
import com.rossallenbell.strifebasin.ui.Canvas;

public class GameLoop implements Runnable {
    
    private final Canvas canvas;
    private final Game game;
    
    private boolean running;
    
    private static GameLoop theInstance;
    
    public static GameLoop getInstance() {
        if (theInstance == null) {
            theInstance = new GameLoop();
        }
        return theInstance;
    }
    
    private GameLoop() {
        canvas = Canvas.getInstance();
        game = Game.getInstance();
        running = true;
    }
    
    @Override
    public void run() {
        while (running) {
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
    
    public void cleanup() {
        running = false;
    }
    
}
