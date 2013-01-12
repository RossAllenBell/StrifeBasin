package com.rossallenbell.strifebasin.ui.menus;

import java.util.List;

public abstract class Menu {
    
    public abstract void keyPressed(int keyCode);
    
    public abstract List<List<String>> getDisplayStrings();
    
}
