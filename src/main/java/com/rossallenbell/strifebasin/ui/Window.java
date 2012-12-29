package com.rossallenbell.strifebasin.ui;

import static com.rossallenbell.strifebasin.StrifeBasin.connection;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import com.rossallenbell.strifebasin.ui.menus.Build;

@SuppressWarnings("serial")
public class Window extends JFrame {
    
    private Canvas canvas;
    
    public Window() {
        super("Strife Basin");
        
        setVisible(true);
        
        getContentPane().add(new ConnectionPanel());
        
        Dimension size = new Dimension(640, 480);
        setPreferredSize(size);
        setMinimumSize(size);
        pack();
        
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                if(connection != null) {
                    connection.cleanup();
                }
                dispose();
            }
        });
    }
    
    public void buildGameDisplay() {
        Build buildMenu = new Build();
        canvas = new Canvas(new Renderer(buildMenu, this));
        getContentPane().add(canvas);
        
        InputListener inputListener = new InputListener(canvas.getRenderer(), buildMenu);
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
