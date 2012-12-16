package com.rossallenbell.strifebasin;

import com.rossallenbell.strifebasin.domain.Game;
import com.rossallenbell.strifebasin.threads.GameLoop;
import com.rossallenbell.strifebasin.ui.Window;

public class StrifeBasin {
    
    private static Window window;
    private static Game game;
    private static GameLoop gameLoop;
    
    public static void main(String[] args){
        game = new Game();
        window = new Window(game);        
        gameLoop = new GameLoop(window.getCanvas());
        new Thread(gameLoop).start();        
    }
    
}