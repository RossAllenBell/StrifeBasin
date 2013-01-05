package com.rossallenbell.strifebasin.domain.util;


public class Pathing {
    
    private static Pathing theInstance;
    
    public static Pathing  getInstance() {
        if(theInstance == null){
            theInstance = new Pathing();
        }
        return theInstance;
    }
    
    private Pathing() {
        
    }
    
}
