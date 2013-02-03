package com.rossallenbell.strifebasin.ui;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JComponent;

@SuppressWarnings("serial")
public class Canvas extends JComponent {
    
    private final Renderer renderer;
    
    private static Canvas theInstance;
    
    public static Canvas getInstance() {
        if (theInstance == null) {
            synchronized (theInstance) {
                if (theInstance == null) {
                    theInstance = new Canvas();
                }
            }
        }
        return theInstance;
    }
    
    private Canvas() {
        renderer = Renderer.getInstance();
        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                if (renderer != null)
                    renderer.setViewDimensions(getSize());
            }
        });
    }
    
    public void paintComponent(Graphics g) {
        if (renderer != null)
            renderer.render((Graphics2D) g);
    }
    
}
