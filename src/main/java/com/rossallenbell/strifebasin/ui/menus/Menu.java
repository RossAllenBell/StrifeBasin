package com.rossallenbell.strifebasin.ui.menus;

import java.util.List;

import com.rossallenbell.strifebasin.domain.buildings.buildable.BuildableBuilding;

public abstract class Menu {
    
    public abstract void keyPressed(int keyCode);
    
    public abstract List<List<String>> getDisplayStrings();
    
    public abstract Class<? extends BuildableBuilding> getCursorEvent();
    
}
