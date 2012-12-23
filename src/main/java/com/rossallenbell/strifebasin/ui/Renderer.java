package com.rossallenbell.strifebasin.ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.List;

import com.rossallenbell.strifebasin.domain.Game;
import com.rossallenbell.strifebasin.domain.buildings.Building;
import com.rossallenbell.strifebasin.ui.menus.Menu;

public class Renderer {
    
    private static final int MINIMAP_WIDTH_PIXELS = 300;
    private static final int MINIMAP_HEIGHT_PIXELS = (int) ((double) MINIMAP_WIDTH_PIXELS * Game.BOARD_HEIGHT / Game.BOARD_WIDTH);
    private static final int PIXELS_PER_BOARD_UNIT = 10;
    private static final int PIXELS_PER_PAN_TICK = 20;
    
    private final Game game;
    private final Menu buildMenu;
    private final BufferedImage image;
    private final BufferedImage background;
    private final Window window;
    
    private Dimension viewDimensions;
    private int viewCornerPixelX;
    private int viewCornerPixelY;
    
    private boolean panningNorth = false;
    private boolean panningEast = false;
    private boolean panningSouth = false;
    private boolean panningWest = false;
    
    private Point mousePos;
    
    public Renderer(Game game, Menu buildMenu, Window window) {
        this.game = game;
        this.buildMenu = buildMenu;
        this.window = window;
        
        image = new BufferedImage(Game.BOARD_WIDTH * PIXELS_PER_BOARD_UNIT, Game.BOARD_HEIGHT * PIXELS_PER_BOARD_UNIT, BufferedImage.TYPE_INT_ARGB);
        background = buildBackground();
        
        viewCornerPixelX = 0;
        viewCornerPixelY = 0;
    }
    
    public void render(Graphics2D destinationGraphics) {
        if (viewDimensions != null) {
            mousePos = window.getMousePositionOnCanvas();
            
            Graphics2D graphics = image.createGraphics();
            
            if (panningNorth) {
                viewCornerPixelY = Math.max(0, viewCornerPixelY - PIXELS_PER_PAN_TICK);
            } else if (panningSouth) {
                viewCornerPixelY = Math.min(image.getHeight() - viewDimensions.height, viewCornerPixelY + PIXELS_PER_PAN_TICK);
            }
            
            if (panningEast) {
                viewCornerPixelX = Math.min(image.getWidth() - viewDimensions.width, viewCornerPixelX + PIXELS_PER_PAN_TICK);
            } else if (panningWest) {
                viewCornerPixelX = Math.max(0, viewCornerPixelX - PIXELS_PER_PAN_TICK);
            }
            
            drawBackground(graphics);
            drawContent(graphics);
            
            destinationGraphics.drawImage(image, 0, 0, viewDimensions.width - 1, viewDimensions.height - 1, viewCornerPixelX, viewCornerPixelY, viewCornerPixelX + viewDimensions.width - 1, viewCornerPixelY + viewDimensions.height - 1, null);
            
            drawOverlay(destinationGraphics);
        }
    }
    
    private void drawBackground(Graphics2D graphics) {
        graphics.drawImage(background, viewCornerPixelX, viewCornerPixelY, viewCornerPixelX + viewDimensions.width - 1, viewCornerPixelY + viewDimensions.height - 1, viewCornerPixelX, viewCornerPixelY, viewCornerPixelX + viewDimensions.width - 1, viewCornerPixelY + viewDimensions.height - 1, null);
    }
    
    private void drawContent(Graphics2D graphics) {
        
    }
    
    private void drawOverlay(Graphics2D graphics) {
        // minimap border
        graphics.setColor(new Color(255, 255, 255, 128));
        graphics.setStroke(new BasicStroke(3));
        graphics.drawRect((viewDimensions.width / 2) - (MINIMAP_WIDTH_PIXELS / 2), 0, MINIMAP_WIDTH_PIXELS, MINIMAP_HEIGHT_PIXELS);
        
        // minimap view indicator
        graphics.setColor(new Color(0, 128, 255, 255));
        graphics.setStroke(new BasicStroke(1));
        int minimapViewX = (viewDimensions.width / 2) - (MINIMAP_WIDTH_PIXELS / 2) + (int) ((double) viewCornerPixelX / image.getWidth() * MINIMAP_WIDTH_PIXELS);
        int minimapViewY = (int) ((double) viewCornerPixelY / image.getHeight() * MINIMAP_HEIGHT_PIXELS);
        int minimapViewWidth = (int) ((double) viewDimensions.width / image.getWidth() * MINIMAP_WIDTH_PIXELS);
        int minimapViewHeight = (int) ((double) viewDimensions.height / image.getHeight() * MINIMAP_HEIGHT_PIXELS);
        graphics.drawRect(minimapViewX, minimapViewY, minimapViewWidth, minimapViewHeight);
        
        FontMetrics fm = graphics.getFontMetrics();
        
        graphics.setColor(new Color(0, 255, 0));
        String moneyString = "Money: " + game.getMe().getMoney();
        graphics.drawString(moneyString, 10, fm.getHeight());
        
        List<List<String>> buildMenuDisplayStrings = buildMenu.getDisplayStrings();
        int nextX = 10;
        int num = 1;
        for (List<String> strings : buildMenuDisplayStrings) {
            int nextY = viewDimensions.height - 10;
            int thisColumnX = nextX;
            for (String string : strings) {
                graphics.drawString(string, thisColumnX, nextY);
                if (fm.stringWidth(string) + thisColumnX > nextX) {
                    nextX = fm.stringWidth(string) + thisColumnX;
                }
                nextY -= fm.getHeight();
            }
            
            if (num < buildMenuDisplayStrings.size()) {
                num++;
                graphics.drawString(" > ", nextX, viewDimensions.height - 10);
                nextX += fm.stringWidth(" > ");
            }
        }
        
        Class<? extends Building> clazz = buildMenu.getCursorEvent();
        if(clazz != null && mousePos != null){
            try {
                Building building = clazz.newInstance();
                Dimension dimension = building.getShape();
                graphics.setColor(new Color(0, 255, 0, 128));
                
                int x = mousePos.x;
                x -= mousePos.x % PIXELS_PER_BOARD_UNIT;
                
                int y = mousePos.y;
                y -= mousePos.y % PIXELS_PER_BOARD_UNIT;
                
                graphics.fillRect(x, y, PIXELS_PER_BOARD_UNIT * dimension.width, PIXELS_PER_BOARD_UNIT * dimension.height);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    public void setViewDimensions(Dimension viewDimensions) {
        this.viewDimensions = viewDimensions;
    }
    
    private BufferedImage buildBackground() {
        BufferedImage background = new BufferedImage(Game.BOARD_WIDTH * PIXELS_PER_BOARD_UNIT, Game.BOARD_HEIGHT * PIXELS_PER_BOARD_UNIT, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = background.createGraphics();
        
        graphics.setColor(new Color(30, 30, 30));
        graphics.fillRect(0, 0, background.getWidth(), background.getHeight());
        
        graphics.setColor(new Color(60, 60, 60));
        for (int i = 1; i < Game.BOARD_WIDTH; i++) {
            graphics.drawLine(i * background.getWidth() / Game.BOARD_WIDTH, 0, i * background.getWidth() / Game.BOARD_WIDTH, background.getHeight() - 1);
        }
        for (int i = 1; i < Game.BOARD_HEIGHT; i++) {
            graphics.drawLine(0, i * background.getHeight() / Game.BOARD_HEIGHT, background.getWidth() - 1, i * background.getHeight() / Game.BOARD_HEIGHT);
        }
        
        return background;
    }
    
    public void wheelIn() {
        
    }
    
    public void wheelOut() {
        
    }
    
    public void keyPressed(int keyCode) {
        switch (keyCode) {
            case KeyEvent.VK_W:
                panningNorth = true;
                break;
            case KeyEvent.VK_D:
                panningEast = true;
                break;
            case KeyEvent.VK_S:
                panningSouth = true;
                break;
            case KeyEvent.VK_A:
                panningWest = true;
                break;
        }
    }
    
    public void keyReleased(int keyCode) {
        switch (keyCode) {
            case KeyEvent.VK_W:
                panningNorth = false;
                break;
            case KeyEvent.VK_D:
                panningEast = false;
                break;
            case KeyEvent.VK_S:
                panningSouth = false;
                break;
            case KeyEvent.VK_A:
                panningWest = false;
                break;
        }
    }
    
}