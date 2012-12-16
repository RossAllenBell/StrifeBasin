package com.rossallenbell.strifebasin.ui;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import com.rossallenbell.strifebasin.domain.Game;

public class InputListener implements MouseListener, KeyListener, MouseWheelListener {
    
    private Game game;
    private Renderer renderer;
    
    public InputListener(Game game, Renderer renderer) {
        this.game = game;
        this.renderer = renderer;
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
