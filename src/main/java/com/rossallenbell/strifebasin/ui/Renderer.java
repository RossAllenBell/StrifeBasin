package com.rossallenbell.strifebasin.ui;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.List;

import com.rossallenbell.strifebasin.domain.Game;
import com.rossallenbell.strifebasin.domain.Player;
import com.rossallenbell.strifebasin.domain.buildings.Building;
import com.rossallenbell.strifebasin.domain.buildings.buildable.BuildableBuilding;
import com.rossallenbell.strifebasin.domain.units.Unit;
import com.rossallenbell.strifebasin.ui.menus.BuildMenu;

public class Renderer {
    
    private static final int MINIMAP_WIDTH_PIXELS = 300;
    private static final int MINIMAP_HEIGHT_PIXELS = (int) ((double) MINIMAP_WIDTH_PIXELS * Game.BOARD_HEIGHT / Game.BOARD_WIDTH);
    private static final int PIXELS_PER_BOARD_UNIT = 10;
    private static final int PIXELS_PER_PAN_TICK = 20;
    
    private final BufferedImage image;
    private final BufferedImage background;
    
    private Dimension viewDimensions;
    private int viewCornerPixelX;
    private int viewCornerPixelY;
    
    private boolean panningNorth = false;
    private boolean panningEast = false;
    private boolean panningSouth = false;
    private boolean panningWest = false;
    
    private Point mousePos;
    
    private static Renderer theInstance;
    
    public static Renderer  getInstance() {
        if(theInstance == null){
            theInstance = new Renderer();
        }
        return theInstance;
    }
    
    private Renderer() {
        image = new BufferedImage(Game.BOARD_WIDTH * PIXELS_PER_BOARD_UNIT, Game.BOARD_HEIGHT * PIXELS_PER_BOARD_UNIT, BufferedImage.TYPE_INT_ARGB);
        background = buildBackground();
        
        viewCornerPixelX = 0;
        viewCornerPixelY = 0;
    }
    
    public void render(Graphics2D destinationGraphics) {
        if (viewDimensions != null) {
            mousePos = Canvas.getInstance().getMousePosition();
            
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
        List<Building> myBuildings = Game.getInstance().getMyBuildings();
        graphics.setColor(new Color(0, 255, 0));
        for(Building building : myBuildings){
            Point2D.Double location = building.getLocation();
            graphics.fillRect((int) (location.x * PIXELS_PER_BOARD_UNIT), (int) (location.y * PIXELS_PER_BOARD_UNIT), building.getShape().width * PIXELS_PER_BOARD_UNIT, building.getShape().height * PIXELS_PER_BOARD_UNIT);
        }
        
        List<Building> theirBuildings = Game.getInstance().getTheirBuildings();
        graphics.setColor(new Color(255, 0, 0));
        for(Building building : theirBuildings){
            Point2D.Double location = building.getLocation();
            graphics.fillRect((int) (location.x * PIXELS_PER_BOARD_UNIT), (int) (location.y * PIXELS_PER_BOARD_UNIT), building.getShape().width * PIXELS_PER_BOARD_UNIT, building.getShape().height * PIXELS_PER_BOARD_UNIT);
        }
        
        List<Unit> myUnits = Game.getInstance().getMyUnits();
        graphics.setColor(new Color(0, 255, 0));
        for(Unit unit : myUnits){
            Point2D.Double location = unit.getLocation();
            Ellipse2D.Double circle = new Ellipse2D.Double(location.x * PIXELS_PER_BOARD_UNIT - (unit.getSize() * PIXELS_PER_BOARD_UNIT / 2), location.y * PIXELS_PER_BOARD_UNIT - (unit.getSize() * PIXELS_PER_BOARD_UNIT / 2), unit.getSize() * PIXELS_PER_BOARD_UNIT, unit.getSize() * PIXELS_PER_BOARD_UNIT);
            graphics.fill(circle);
        }
        
        List<Unit> theirUnits = Game.getInstance().getTheirUnits();
        graphics.setColor(new Color(255, 0, 0));
        for(Unit unit : theirUnits){
            Point2D.Double location = unit.getLocation();
            Ellipse2D.Double circle = new Ellipse2D.Double(location.x * PIXELS_PER_BOARD_UNIT - (unit.getSize() * PIXELS_PER_BOARD_UNIT / 2), location.y * PIXELS_PER_BOARD_UNIT - (unit.getSize() * PIXELS_PER_BOARD_UNIT / 2), unit.getSize() * PIXELS_PER_BOARD_UNIT, unit.getSize() * PIXELS_PER_BOARD_UNIT);
            graphics.fill(circle);
        }
    }
    
    private void drawOverlay(Graphics2D graphics) {
        // minimap border
        graphics.setColor(new Color(255, 255, 255, 64));
        graphics.setStroke(new BasicStroke(3));
        graphics.drawRect((viewDimensions.width / 2) - (MINIMAP_WIDTH_PIXELS / 2), 0, MINIMAP_WIDTH_PIXELS, MINIMAP_HEIGHT_PIXELS);
        
        //minimap
        Composite composite = graphics.getComposite();
        graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
        graphics.drawImage(image, (viewDimensions.width / 2) - (MINIMAP_WIDTH_PIXELS / 2), 0, (viewDimensions.width / 2) - (MINIMAP_WIDTH_PIXELS / 2) + MINIMAP_WIDTH_PIXELS, MINIMAP_HEIGHT_PIXELS, 0, 0, image.getWidth(), image.getHeight(), null);
        graphics.setComposite(composite);
        
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
        String moneyString = "Money: " + Game.getInstance().getMe().getMoney();
        graphics.drawString(moneyString, 10, fm.getHeight());
        
        List<List<String>> buildMenuDisplayStrings = BuildMenu.getInstance().getDisplayStrings();
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
        
        Class<? extends Building> clazz = BuildMenu.getInstance().getCursorEvent();
        if (clazz != null && mousePos != null) {
            try {
                Building building = clazz.getConstructor(Player.class).newInstance(Game.getInstance().getMe());
                Dimension dimension = building.getShape();
                graphics.setColor(new Color(0, 255, 0, 128));
                
                Point selectionPoint = getDisplayGridPointByMousePos(mousePos);
                
                graphics.fillRect(selectionPoint.x, selectionPoint.y, PIXELS_PER_BOARD_UNIT * dimension.width, PIXELS_PER_BOARD_UNIT * dimension.height);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    public Point getDisplayGridPointByMousePos(Point point) {
        Point gameGrid = getGameGridUnitByMousePos(point);
        
        return new Point(gameGrid.x * PIXELS_PER_BOARD_UNIT - viewCornerPixelX, gameGrid.y * PIXELS_PER_BOARD_UNIT - viewCornerPixelY);
    }
    
    public Point getGameGridUnitByMousePos(Point point) {
        int x = point.x + viewCornerPixelX;
        int y = point.y + viewCornerPixelY;
        
        x /= PIXELS_PER_BOARD_UNIT;
        y /= PIXELS_PER_BOARD_UNIT;
        
        return new Point(x, y);
    }
    
    public void setViewDimensions(Dimension viewDimensions) {
        this.viewDimensions = viewDimensions;
    }
    
    private BufferedImage buildBackground() {
        BufferedImage background = new BufferedImage(Game.BOARD_WIDTH * PIXELS_PER_BOARD_UNIT, Game.BOARD_HEIGHT * PIXELS_PER_BOARD_UNIT, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = background.createGraphics();
        
        graphics.setColor(new Color(30, 30, 30));
        graphics.fillRect(0, 0, background.getWidth(), background.getHeight());
        
//        graphics.setColor(new Color(60, 60, 60));
//        for (int i = 1; i < Game.BOARD_WIDTH; i++) {
//            graphics.drawLine(i * background.getWidth() / Game.BOARD_WIDTH, 0, i * background.getWidth() / Game.BOARD_WIDTH, background.getHeight() - 1);
//        }
//        for (int i = 1; i < Game.BOARD_HEIGHT; i++) {
//            graphics.drawLine(0, i * background.getHeight() / Game.BOARD_HEIGHT, background.getWidth() - 1, i * background.getHeight() / Game.BOARD_HEIGHT);
//        }
        
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
    
    public void mouseClicked(MouseEvent e) {
        Class<? extends BuildableBuilding> buildMenuItem = BuildMenu.getInstance().getCursorEvent();
        if (buildMenuItem != null) {
            Point buildLocation = getGameGridUnitByMousePos(Canvas.getInstance().getMousePosition());
            try {
                BuildableBuilding building = buildMenuItem.getConstructor(Player.class).newInstance(Game.getInstance().getMe());
                building.setLocation(buildLocation.x, buildLocation.y);
                Game.getInstance().buildingPlaced(building);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }
    
}
