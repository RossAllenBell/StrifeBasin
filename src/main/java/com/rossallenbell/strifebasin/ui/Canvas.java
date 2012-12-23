package com.rossallenbell.strifebasin.ui;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JComponent;

@SuppressWarnings("serial")
public class Canvas extends JComponent {
    
    private final Renderer renderer;
    
    public Canvas(Renderer aRenderer){
        renderer = aRenderer;
        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                if(getRenderer() != null) getRenderer().setViewDimensions(getSize());
            }
        });
    }
    
    public void paintComponent(Graphics g) {
        if(getRenderer() != null) getRenderer().render((Graphics2D) g);
    }

    public Renderer getRenderer() {
        return renderer;
    }
    
}
