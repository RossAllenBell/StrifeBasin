package com.rossallenbell.strifebasin.domain;

public class Game {
    
    public static final int BOARD_WIDTH = 1000;
    public static final int BOARD_HEIGHT = 200;
    public static final double MIN_ZOOM = 1.0;
    public static final double MAX_ZOOM = BOARD_WIDTH / 10.0;
    
    private double zoom;
    private double viewX;
    private double viewY;
    
    public Game(){
        zoom = 1.0;
        viewX = 0;
        viewY = 0;
    }

    public double getZoom() {
        return zoom;
    }

    public void setZoom(double zoom) {
        this.zoom = zoom;
    }

    public double getViewX() {
        return viewX;
    }

    public void setViewX(double viewX) {
        this.viewX = viewX;
    }

    public double getViewY() {
        return viewY;
    }

    public void setViewY(double viewY) {
        this.viewY = viewY;
    }
    
}
