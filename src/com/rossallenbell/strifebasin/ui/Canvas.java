package com.rossallenbell.strifebasin.ui;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JComponent;

@SuppressWarnings("serial")
public class Canvas extends JComponent implements MouseListener {
    
    Renderer renderer;
    
    public Canvas(Renderer aRenderer){
        renderer = aRenderer;
        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                if(renderer != null) renderer.resizeView(getSize());
            }
        });
    }
    
    public void paintComponent(Graphics g) {
        if(renderer != null) renderer.render((Graphics2D) g);
    }
    
    @Override
    public void mouseClicked(MouseEvent arg0) {
        
    }
    
    @Override
    public void mouseEntered(MouseEvent arg0) {
        
    }
    
    @Override
    public void mouseExited(MouseEvent arg0) {
        
    }
    
    @Override
    public void mousePressed(MouseEvent arg0) {
        
    }
    
    @Override
    public void mouseReleased(MouseEvent arg0) {
        
    }
    
}
