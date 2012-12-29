package com.rossallenbell.strifebasin.ui;

import java.awt.Point;

import javax.swing.JFrame;

import com.rossallenbell.strifebasin.domain.Game;
import com.rossallenbell.strifebasin.ui.menus.Build;

@SuppressWarnings("serial")
public class Window extends JFrame {
    
    private Canvas canvas;
    
    public Window(Game game) {
        super("Strife Basin");
        setVisible(true);
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        setSize(640, 480);
        
        Build buildMenu = new Build();
        canvas = new Canvas(new Renderer(game, buildMenu, this));
        getContentPane().add(canvas);
        
        InputListener inputListener = new InputListener(game, canvas.getRenderer(), buildMenu);
        addMouseListener(inputListener);
        addMouseWheelListener(inputListener);
        addKeyListener(inputListener);
        
        setExtendedState(JFrame.MAXIMIZED_BOTH);
    }
    
    public Canvas getCanvas() {
        return canvas;
    }

    public Point getMousePositionOnCanvas() {
        return canvas.getMousePosition();
    }
    
}
