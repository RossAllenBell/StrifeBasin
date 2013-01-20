package com.rossallenbell.strifebasin;

import com.rossallenbell.strifebasin.threads.GameLoop;
import com.rossallenbell.strifebasin.ui.Window;

public class StrifeBasin {
    
    public static boolean DEBUG = false;
    
    public static Window window;
    
    public static void main(String[] args) {
        for (String arg : args) {
            if (arg.equalsIgnoreCase("debug")) {
                DEBUG = true;
            }
        }
        
        window = Window.getInstance();
    }
    
    public static void connectionComplete() {
        window.buildGameDisplay();
        new Thread(GameLoop.getInstance()).start();
    }
    
}