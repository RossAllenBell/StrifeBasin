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
        connection = ConnectionToOpponent.getInstance();
        window = Window.getInstance();
    }
    
    public static void connectionComplete() {
        game = Game.getInstance();
        gameLoop = GameLoop.getInstance();
        new Thread(gameLoop).start();
    }
    
}