package com.rossallenbell.strifebasin;

import com.rossallenbell.strifebasin.connection.ConnectionToOpponent;
import com.rossallenbell.strifebasin.domain.Game;
import com.rossallenbell.strifebasin.threads.GameLoop;
import com.rossallenbell.strifebasin.ui.Window;

public class StrifeBasin {
    
    public static ConnectionToOpponent connection;    
    public static Window window;
    public static Game game;
    public static GameLoop gameLoop;
    
    public static void main(String[] args) {
        connection = new ConnectionToOpponent();
        window = new Window();
    }
    
    public static void connectionComplete() {
        game = new Game();
        gameLoop = new GameLoop(window.getCanvas());
        new Thread(gameLoop).start();
    }
    
}