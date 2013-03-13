package com.rossallenbell.strifebasin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import org.reflections.Reflections;

import com.rossallenbell.strifebasin.domain.Me;
import com.rossallenbell.strifebasin.domain.buildings.buildable.unitspawning.UnitSpawingBuilding;
import com.rossallenbell.strifebasin.domain.units.PlayerUnit;
import com.rossallenbell.strifebasin.threads.GameLoop;
import com.rossallenbell.strifebasin.ui.Window;

public class StrifeBasin {
    
    public static boolean DEBUG = false;
    public static boolean UNITS = false;
    
    public static Window window;
    
    public static void main(String[] args) {
        for (String arg : args) {
            if (arg.equalsIgnoreCase("debug")) {
                DEBUG = true;
            } else if (arg.equalsIgnoreCase("units")) {
                UNITS = true;
            }
        }
        
        if (UNITS) {
            printUnitStats();
            System.exit(0);
        }
        
        window = Window.getInstance();
    }
    
    public static void connectionComplete() {
        window.buildGameDisplay();
        new Thread(GameLoop.getInstance()).start();
    }
    
    private static void printUnitStats() {
        Reflections reflections = new Reflections("com.rossallenbell.strifebasin.domain.buildings.buildable.unitspawning");
        Set<Class<? extends UnitSpawingBuilding>> buildingClasses = reflections.getSubTypesOf(UnitSpawingBuilding.class);
        Me me = new Me();
        List<UnitSpawingBuilding> buildings = new ArrayList<>();
        for (Class<? extends UnitSpawingBuilding> buildingClass : buildingClasses) {
            try {
                buildings.add(buildingClass.getConstructor(Me.class).newInstance(me));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        Collections.sort(buildings, new Comparator<UnitSpawingBuilding>() {
            @Override
            public int compare(UnitSpawingBuilding buildingA, UnitSpawingBuilding buildingB) {
                return Integer.compare(buildingA.cost(), buildingB.cost());
            }
        });
        
        String formatString = "%1$-11s | %2$4s | %3$6s | %4$6s | %5$5s | %6$11s | %7$5s | %8$10s";
        System.out.println();
        System.out.println(String.format(formatString, "Unit", "Cost", "Health", "Damage", "Speed", "AttackSpeed", "Range", "AggroRange"));
        System.out.println();
        for (UnitSpawingBuilding building : buildings) {
            PlayerUnit unit = building.spawn(0);
            System.out.println(String.format(formatString, unit.getClass().getSimpleName(), building.cost(), unit.getMaxHealth(), unit.getDamage(), unit.getSpeed(), unit.getAttackSpeed(), unit.getRange(), unit.getAggroRange()));
        }
    }
    
}