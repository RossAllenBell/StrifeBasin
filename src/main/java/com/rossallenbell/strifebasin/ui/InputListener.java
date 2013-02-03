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
    
    private static InputListener theInstance;
    
    public static InputListener getInstance() {
        if (theInstance == null) {
            synchronized (theInstance) {
                if (theInstance == null) {
                    theInstance = new InputListener();
                }
            }
        }
        return theInstance;
    }
    
    private InputListener() {
        
    }
    
    @Override
    public void mouseWheelMoved(MouseWheelEvent event) {
        if (event.getWheelRotation() < 0) {
            Game.getInstance().wheelIn();
            Renderer.getInstance().wheelIn();
        } else {
            Game.getInstance().wheelOut();
            Renderer.getInstance().wheelOut();
        }
    }
    
    @Override
    public void keyPressed(KeyEvent event) {
        Game.getInstance().keyPressed(event.getKeyCode());
        Renderer.getInstance().keyPressed(event.getKeyCode());
        BuildMenu.getInstance().keyPressed(event.getKeyCode());
    }
    
    @Override
    public void keyReleased(KeyEvent event) {
        Game.getInstance().keyReleased(event.getKeyCode());
        Renderer.getInstance().keyReleased(event.getKeyCode());
    }
    
    @Override
    public void keyTyped(KeyEvent event) {
        
    }
    
    @Override
    public void mouseClicked(MouseEvent e) {
        Renderer.getInstance().mouseClicked(e);
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
