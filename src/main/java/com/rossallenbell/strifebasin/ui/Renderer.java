package com.rossallenbell.strifebasin.ui;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.List;

import com.rossallenbell.strifebasin.connection.ConnectionToOpponent;
import com.rossallenbell.strifebasin.connection.domain.NetworkAsset;
import com.rossallenbell.strifebasin.connection.domain.NetworkUnit;
import com.rossallenbell.strifebasin.domain.Asset;
import com.rossallenbell.strifebasin.domain.Game;
import com.rossallenbell.strifebasin.domain.buildings.Building;
import com.rossallenbell.strifebasin.domain.buildings.buildable.BuildableBuilding;
import com.rossallenbell.strifebasin.domain.units.PlayerUnit;
import com.rossallenbell.strifebasin.threads.GameLoop;
import com.rossallenbell.strifebasin.ui.menus.BuildMenu;
import com.rossallenbell.strifebasin.ui.resources.HasImage;
import com.rossallenbell.strifebasin.ui.resources.ImageManager;

public class Renderer {
    
    private static final String WIN_STRING = "YOU WIN";
    private static final String LOSE_STRING = "YOU LOSE";
    
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
    private long currentTime;
    
    private static Renderer theInstance;
    
    public static Renderer getInstance() {
        if (theInstance == null) {
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
            currentTime = System.currentTimeMillis();
            
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
        // graphics.drawImage(background, viewCornerPixelX, viewCornerPixelY,
        // viewCornerPixelX + viewDimensions.width - 1, viewCornerPixelY +
        // viewDimensions.height - 1, viewCornerPixelX, viewCornerPixelY,
        // viewCornerPixelX + viewDimensions.width - 1, viewCornerPixelY +
        // viewDimensions.height - 1, null);
        graphics.drawImage(background, 0, 0, null);
    }
    
    private void drawContent(Graphics2D graphics) {
        graphics.setColor(new Color(0, 255, 0));
        for (Building building : Game.getInstance().getMyBuildings().values()) {
            drawBuilding(graphics, building);
        }

        graphics.setColor(new Color(255, 0, 0));
        for (NetworkAsset building : Game.getInstance().getTheirBuildings().values()) {
            drawBuilding(graphics, building);
        }
        
        for (PlayerUnit unit : Game.getInstance().getMyUnits().values()) {
            Point2D.Double location = unit.getLocation();
            Ellipse2D.Double circle = new Ellipse2D.Double(location.x * PIXELS_PER_BOARD_UNIT - (unit.getSize() * PIXELS_PER_BOARD_UNIT / 2), location.y * PIXELS_PER_BOARD_UNIT - (unit.getSize() * PIXELS_PER_BOARD_UNIT / 2), unit.getSize() * PIXELS_PER_BOARD_UNIT, unit.getSize() * PIXELS_PER_BOARD_UNIT);
            graphics.setColor(new Color(0, 255, 0));
            graphics.fill(circle);
            
            if (unit.getHealthRatio() < 1) {
                drawHealthbar(graphics, unit);
            }
        }
        
        for (NetworkUnit unit : Game.getInstance().getTheirUnits().values()) {
            Point2D.Double location = unit.getLocation();
            Ellipse2D.Double circle = new Ellipse2D.Double(location.x * PIXELS_PER_BOARD_UNIT - (unit.getSize() * PIXELS_PER_BOARD_UNIT / 2), location.y * PIXELS_PER_BOARD_UNIT - (unit.getSize() * PIXELS_PER_BOARD_UNIT / 2), unit.getSize() * PIXELS_PER_BOARD_UNIT, unit.getSize() * PIXELS_PER_BOARD_UNIT);
            graphics.setColor(new Color(255, 0, 0));
            graphics.fill(circle);
            
            if (unit.getHealthRatio() < 1) {
                drawHealthbar(graphics, unit);
            }
        }
    }

    private void drawBuilding(Graphics2D graphics, Asset building) {
        drawBuilding(graphics, building, (int) (building.getLocation().x * PIXELS_PER_BOARD_UNIT), (int) (building.getLocation().y * PIXELS_PER_BOARD_UNIT));
    }
    
    private void drawBuilding(Graphics2D graphics, Asset building, int displayX, int displayY) {        
        int width = (int) (building.getSize() * PIXELS_PER_BOARD_UNIT);
        int height = (int) (building.getSize() * PIXELS_PER_BOARD_UNIT);
        
        Class<? extends Asset> imagedClass = building.getImageClass();        
        if (imagedClass.isAnnotationPresent(HasImage.class)) {
            BufferedImage image = ImageManager.getInstance().getImage(imagedClass);
            graphics.drawImage(image, displayX, displayY, displayX + width, displayY + height, 0, 0, image.getWidth() - 1, image.getHeight() - 1, null);
        } else {
            graphics.fillRect(displayX, displayY, width, height);
        }
        
        if (building.getHealthRatio() < 1) {
            drawHealthbar(graphics, building);
        }
    }
    
    private void drawHealthbar(Graphics2D graphics, Asset asset) {
        graphics.setColor(new Color(0, 255, 255, 128));
        Point2D.Double topCenterPixel = getTopCenterPixel(asset);
        int x = (int) (topCenterPixel.x - (asset.getSize() / 2 * PIXELS_PER_BOARD_UNIT));
        int y = (int) (topCenterPixel.y - 5);
        int width = (int) (asset.getHealthRatio() * asset.getSize() * PIXELS_PER_BOARD_UNIT);
        int height = 3;
        graphics.fillRect(x, y, width, height);
    }
    
    private Point2D.Double getTopCenterPixel(Asset asset) {
        double x = asset.getHitLocation().x * PIXELS_PER_BOARD_UNIT;
        double y = (asset.getHitLocation().y - (asset.getSize() / 2.0)) * PIXELS_PER_BOARD_UNIT;
        return new Point2D.Double(x, y);
    }
    
    private void drawOverlay(Graphics2D graphics) {
        graphics.setFont(new Font(null, Font.PLAIN, 14));
        FontMetrics fm = graphics.getFontMetrics();
        
        // minimap border
        graphics.setColor(new Color(255, 255, 255, 64));
        graphics.setStroke(new BasicStroke(3));
        graphics.drawRect((viewDimensions.width / 2) - (MINIMAP_WIDTH_PIXELS / 2), 0, MINIMAP_WIDTH_PIXELS, MINIMAP_HEIGHT_PIXELS);
        
        // minimap
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
        
        // game stats
        graphics.setColor(new Color(0, 255, 0));
        String moneyString = "Money: " + (int) Game.getInstance().getMe().getMoney();
        graphics.drawString(moneyString, 10, fm.getHeight());
        String incomeString = "Income: " + (int) Game.getInstance().getMe().getIncome();
        graphics.drawString(incomeString, 10, fm.getHeight() * 2);
        graphics.drawRect(10, (fm.getHeight()) * 2 + 10, fm.stringWidth("Income"), 10);
        double incomeProgress = fm.stringWidth("Income") * (double) (currentTime - Game.getInstance().getMe().getLastIncomeTime()) / Game.INCOME_COOLDOWN;
        graphics.fillRect(10, (fm.getHeight()) * 2 + 10, (int) incomeProgress, 10);
        
        // system stats
        graphics.setColor(new Color(0, 255, 0));
        String pingString = "Ping: " + ConnectionToOpponent.getInstance().getPing();
        graphics.drawString(pingString, viewDimensions.width - fm.stringWidth(pingString) - 10, viewDimensions.height - 10);
        String fpsString = "FPS: " + GameLoop.getInstance().getFps();
        graphics.drawString(fpsString, viewDimensions.width - fm.stringWidth(fpsString) - 10, viewDimensions.height - 10 - fm.getHeight());
        
        // build menu
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
        
        // new building
        BuildableBuilding building = Game.getInstance().getBuildingPreview();
        if (building != null && mousePos != null) {
            if (viewCornerPixelX < Game.BUILD_ZONE_WIDTH * PIXELS_PER_BOARD_UNIT) {
                graphics.setColor(new Color(0, 255, 0, 32));
                graphics.fillRect(0, 0, (Game.BUILD_ZONE_WIDTH * PIXELS_PER_BOARD_UNIT) - viewCornerPixelX, image.getHeight());
            }
            
            Dimension dimension = building.getShape();
            Point selectionPoint = getDisplayGridPointByMousePos(mousePos);
            Point gamePoint = getGameGridUnitByMousePos(mousePos);
            building.setLocation(selectionPoint.x, selectionPoint.y);
            
            if (Game.getInstance().isValidBuildLocation(gamePoint)) {
                if(Game.getInstance().getMe().getMoney() >= building.cost()) {
                    graphics.setColor(new Color(0, 255, 0, 128));
                } else {
                    graphics.setColor(new Color(255, 255, 0, 128));
                }
            } else {
                graphics.setColor(new Color(255, 0, 0, 128));
            }
            
            drawBuilding(graphics, building, selectionPoint.x, selectionPoint.y);
            graphics.fillRect(selectionPoint.x, selectionPoint.y, PIXELS_PER_BOARD_UNIT * dimension.width, PIXELS_PER_BOARD_UNIT * dimension.height);
        }
        
        if (Game.getInstance().getThem().getSanctuary().getHealth() <= 0) {
            graphics.setColor(new Color(0, 255, 0));
            graphics.setFont(new Font(null, Font.PLAIN, 70));
            fm = graphics.getFontMetrics();
            graphics.drawString(WIN_STRING, (viewDimensions.width / 2) - (fm.stringWidth(WIN_STRING) / 2), viewDimensions.height / 2);
        } else if (Game.getInstance().getMe().getSanctuary().getHealth() <= 0) {
            graphics.setColor(new Color(255, 0, 0));
            graphics.setFont(new Font(null, Font.PLAIN, 70));
            fm = graphics.getFontMetrics();
            graphics.drawString(LOSE_STRING, (viewDimensions.width / 2) - (fm.stringWidth(LOSE_STRING) / 2), viewDimensions.height / 2);
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
        
        // graphics.setColor(new Color(60, 60, 60));
        // for (int i = 1; i < Game.BOARD_WIDTH; i++) {
        // graphics.drawLine(i * background.getWidth() / Game.BOARD_WIDTH, 0, i
        // * background.getWidth() / Game.BOARD_WIDTH, background.getHeight() -
        // 1);
        // }
        // for (int i = 1; i < Game.BOARD_HEIGHT; i++) {
        // graphics.drawLine(0, i * background.getHeight() / Game.BOARD_HEIGHT,
        // background.getWidth() - 1, i * background.getHeight() /
        // Game.BOARD_HEIGHT);
        // }
        
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
        BuildableBuilding buildingPreview = Game.getInstance().getBuildingPreview();
        if (buildingPreview != null) {
            Point buildLocation = getGameGridUnitByMousePos(Canvas.getInstance().getMousePosition());
            Game.getInstance().buildingPlaced(buildLocation);
        }
    }
    
}
