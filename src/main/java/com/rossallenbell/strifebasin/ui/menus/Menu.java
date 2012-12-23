package com.rossallenbell.strifebasin.ui.menus;

import java.util.List;

import com.rossallenbell.strifebasin.domain.buildings.Building;

public abstract class Menu {
    
    public abstract void keyPressed(int keyCode);
    
    public abstract List<List<String>> getDisplayStrings();
    
    public abstract Class<? extends Building> getCursorEvent();
    
}
