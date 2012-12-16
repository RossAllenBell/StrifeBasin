package com.rossallenbell.strifebasin.ui;

import javax.swing.JFrame;

import com.rossallenbell.strifebasin.domain.Game;

@SuppressWarnings("serial")
public class Window extends JFrame {
    
    private Canvas canvas;
    
    public Window(Game game) {
        super("Strife Basin");
        setVisible(true);
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        setSize(640, 480);
        
        canvas = new Canvas(new Renderer(game));
        getContentPane().add(canvas);
        
        setExtendedState(JFrame.MAXIMIZED_BOTH);
    }
    
    public Canvas getCanvas() {
        return canvas;
    }
    
}
