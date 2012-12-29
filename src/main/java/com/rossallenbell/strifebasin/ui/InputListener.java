package com.rossallenbell.strifebasin.ui;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import com.rossallenbell.strifebasin.domain.Game;
import com.rossallenbell.strifebasin.ui.menus.BuildMenu;

public class InputListener implements MouseListener, KeyListener, MouseWheelListener {
    
    private Renderer renderer;
    private BuildMenu buildMenu;
    private Game game;
    
    private static InputListener theInstance;
    
    public static InputListener  getInstance() {
        if(theInstance == null){
            theInstance = new InputListener();
        }
        return theInstance;
    }
    
    public InputListener() {
        game = Game.getInstance();
        renderer = Renderer.getInstance();
        buildMenu = BuildMenu.getInstance();
    }
    
    @Override
    public void mouseWheelMoved(MouseWheelEvent event) {
        if (event.getWheelRotation() < 0) {
            game.wheelIn();
            renderer.wheelIn();
        } else {
            game.wheelOut();
            renderer.wheelOut();
        }
    }
    
    @Override
    public void keyPressed(KeyEvent event) {
        game.keyPressed(event.getKeyCode());
        renderer.keyPressed(event.getKeyCode());
        buildMenu.keyPressed(event.getKeyCode());
    }
    
    @Override
    public void keyReleased(KeyEvent event) {
        game.keyReleased(event.getKeyCode());
        renderer.keyReleased(event.getKeyCode());
    }
    
    @Override
    public void keyTyped(KeyEvent event) {
        
    }
    
    @Override
    public void mouseClicked(MouseEvent e) {
        renderer.mouseClicked(e);
    }
    
    @Override
    public void mouseEntered(MouseEvent e) {
        
    }
    
    @Override
    public void mouseExited(MouseEvent e) {
        
    }
    
    @Override
    public void mousePressed(MouseEvent e) {
        
    }
    
    @Override
    public void mouseReleased(MouseEvent e) {
        
    }
    
}
