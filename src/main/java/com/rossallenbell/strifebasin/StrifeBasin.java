package com.rossallenbell.strifebasin;

import com.rossallenbell.strifebasin.threads.GameLoop;
import com.rossallenbell.strifebasin.ui.Window;

public class StrifeBasin {
    
    public static boolean DEBUG = true;
    
    public static Window window;
    
    public static void main(String[] args) {
        window = Window.getInstance();
    }
    
    public static void connectionComplete() {
        window.buildGameDisplay();
        new Thread(GameLoop.getInstance()).start();
    }
    
}