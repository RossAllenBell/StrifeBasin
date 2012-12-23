package com.rossallenbell.strifebasin.ui.menus;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import org.reflections.Reflections;

import com.rossallenbell.strifebasin.domain.Game;
import com.rossallenbell.strifebasin.domain.buildings.AdvancedBuilding;
import com.rossallenbell.strifebasin.domain.buildings.BasicBuilding;
import com.rossallenbell.strifebasin.domain.buildings.Building;

public class Build extends Menu {
    
    private static enum State {CLOSED, TYPE, BASIC, ADVANCED};
    
    private Game game;
    
    private List<Class<? extends Building>> basicBuildings;
    private List<Class<? extends Building>> advancedBuildings;
    
    private State state;
    private int numSelected;
    
    public Build(Game game){
        state = State.CLOSED;
        
        this.game = game;
        
        basicBuildings = new ArrayList<Class<? extends Building>>();
        advancedBuildings = new ArrayList<Class<? extends Building>>();
        
        Reflections reflections = new Reflections("com.rossallenbell.strifebasin.domain.buildings.basic");
        basicBuildings.addAll(reflections.getSubTypesOf(BasicBuilding.class));
        
        reflections = new Reflections("com.rossallenbell.strifebasin.domain.buildings.advanced");
        advancedBuildings.addAll(reflections.getSubTypesOf(AdvancedBuilding.class));
    }
    
    @Override
    public List<List<String>> getDisplayStrings(){
        List<List<String>> displayStrings = new ArrayList<List<String>>();
        
        List<String> base = new ArrayList<String>();
        base.add("(B) Build");
        displayStrings.add(base);
        
        if(state != State.CLOSED) {
            List<String> type = new ArrayList<String>();
            type.add("(1) Basic");
            type.add("(2) Advanced");
            displayStrings.add(type);
        }
        
        if(state == State.BASIC){
            List<String> basicBuildingNames = new ArrayList<String>();
            int num = 1;
            for(Class<? extends Building> clazz : basicBuildings){
                basicBuildingNames.add("(" + num + ") " + clazz.getSimpleName());
                num++;
            }
            displayStrings.add(basicBuildingNames);
        } else if(state == State.ADVANCED){
            List<String> advancedBuildingNames = new ArrayList<String>();
            int num = 1;
            for(Class<? extends Building> clazz : advancedBuildings){
                advancedBuildingNames.add("(" + num + ") " + clazz.getSimpleName());
                num++;
            }
            displayStrings.add(advancedBuildingNames);
        }
        
        return displayStrings;
    }

    @Override
    public void keyPressed(int keyCode) {
        switch (keyCode) {
            case KeyEvent.VK_ESCAPE:
                state = State.CLOSED;
                numSelected = -1;
                break;
            case KeyEvent.VK_B:
                state = State.TYPE;
                numSelected = -1;
                break;
            case KeyEvent.VK_1:
                if(state == State.TYPE){
                    state = State.BASIC;
                    numSelected = -1;
                    break;
                }
            case KeyEvent.VK_2:
                if(state == State.TYPE){
                    state = State.ADVANCED;
                    numSelected = -1;
                    break;
                }
            case KeyEvent.VK_3:
            case KeyEvent.VK_4:
            case KeyEvent.VK_5:
            case KeyEvent.VK_6:
            case KeyEvent.VK_7:
            case KeyEvent.VK_8:
            case KeyEvent.VK_9:
            case KeyEvent.VK_0:
                numSelected = keyCode - 48;
                break;
        }
    }

    @Override
    public Class<? extends Building> getCursorEvent() {
        if(numSelected != -1){
            if(state == State.BASIC){
                return basicBuildings.get(numSelected - 1);
            }
            if(state == State.ADVANCED){
                return advancedBuildings.get(numSelected - 1);
            }
        }
        
        return null;
    }
    
}
