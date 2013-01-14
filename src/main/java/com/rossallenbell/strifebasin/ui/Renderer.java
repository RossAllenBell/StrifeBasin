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
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.rossallenbell.strifebasin.connection.ConnectionToOpponent;
import com.rossallenbell.strifebasin.connection.domain.NetworkAsset;
import com.rossallenbell.strifebasin.connection.domain.NetworkUnit;
import com.rossallenbell.strifebasin.domain.Asset;
import com.rossallenbell.strifebasin.domain.Game;
import com.rossallenbell.strifebasin.domain.buildings.Building;
import com.rossallenbell.strifebasin.domain.buildings.buildable.BuildableBuilding;
import com.rossallenbell.strifebasin.domain.units.PlayerUnit;
import com.rossallenbell.strifebasin.domain.units.Unit;
import com.rossallenbell.strifebasin.domain.util.Pathing;
import com.rossallenbell.strifebasin.ui.effects.Effect;
import com.rossallenbell.strifebasin.ui.effects.EffectsManager;
import com.rossallenbell.strifebasin.ui.menus.BuildMenu;
import com.rossallenbell.strifebasin.ui.resources.AnimationManager;
import com.rossallenbell.strifebasin.ui.resources.HasAnimation;
import com.rossallenbell.strifebasin.ui.resources.HasImage;
import com.rossallenbell.strifebasin.ui.resources.ImageManager;

public class Renderer {
    
    private static final String WIN_STRING = "YOU WIN";
    private static final String LOSE_STRING = "YOU LOSE";
    
    private static final int MINIMAP_WIDTH_PIXELS = 300;
    private static final int MINIMAP_HEIGHT_PIXELS = (int) ((double) MINIMAP_WIDTH_PIXELS * Game.BOARD_HEIGHT / Game.BOARD_WIDTH);
    private static final double MINIMAP_PIXELS_PER_BOARD_UNIT = MINIMAP_WIDTH_PIXELS / (double) Game.BOARD_WIDTH;
    private static final int PIXELS_PER_BOARD_UNIT = 15;
    private static final int PIXELS_PER_PAN_TICK = PIXELS_PER_BOARD_UNIT * 2;
    
    private static final int FPS_HISTORY = 10;
    
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
    
    private List<Long> loopTimes;
    
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
        
        loopTimes = Collections.synchronizedList(new ArrayList<Long>(FPS_HISTORY));
    }
    
    public void render(Graphics2D destinationGraphics) {
        currentTime = System.currentTimeMillis();
        
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
            
            synchronized (loopTimes) {
                if (loopTimes.size() > FPS_HISTORY - 1) {
                    loopTimes.remove(0);
                }
                loopTimes.add(currentTime);
            }
        }
    }
    
    private void drawBackground(Graphics2D graphics) {
        graphics.drawImage(background, viewCornerPixelX, viewCornerPixelY, viewCornerPixelX + viewDimensions.width - 1, viewCornerPixelY + viewDimensions.height - 1, viewCornerPixelX, viewCornerPixelY, viewCornerPixelX + viewDimensions.width - 1, viewCornerPixelY + viewDimensions.height - 1, null);
    }
    
    private void drawContent(Graphics2D graphics) {
        Map<Long, Building> myBuildings = Game.getInstance().getMyBuildings();
        synchronized (myBuildings) {
            for (Building building : myBuildings.values()) {
                graphics.setColor(new Color(0, 255, 0));
                drawBuilding(graphics, building);
            }
        }
        
        for (NetworkAsset building : Game.getInstance().getTheirBuildings().values()) {
            graphics.setColor(new Color(255, 0, 0));
            drawBuilding(graphics, building);
        }
        
        Map<Long, PlayerUnit> myUnits = Game.getInstance().getMyUnits();
        synchronized (myUnits) {
            for (PlayerUnit unit : myUnits.values()) {
                graphics.setColor(new Color(0, 255, 0));
                drawUnit(graphics, unit);
            }
        }
        
        for (NetworkUnit unit : Game.getInstance().getTheirUnits().values()) {
            graphics.setColor(new Color(255, 0, 0));
            drawUnit(graphics, unit);
        }
        
        List<Effect> effects = EffectsManager.getInstance().getEffects();
        synchronized (effects) {
            for (Effect effect : effects) {
                drawEffect(graphics, effect);
            }
        }
    }
    
    private void drawEffect(Graphics2D graphics, Effect effect) {
        int x = (int) (effect.getLocation().x * PIXELS_PER_BOARD_UNIT);
        int y = (int) (effect.getLocation().y * PIXELS_PER_BOARD_UNIT);
        int width = (int) (effect.getSize() * PIXELS_PER_BOARD_UNIT);
        int height = (int) (effect.getSize() * PIXELS_PER_BOARD_UNIT);
        
        AffineTransform oldXForm = graphics.getTransform();
        graphics.rotate(effect.getDirection(), x, y);
        
        BufferedImage image = effect.getImage();
        x = x - (width / 2);
        y = y - (height / 2);
        graphics.drawImage(image, x, y, x + width, y + height, 0, 0, image.getWidth() - 1, image.getHeight() - 1, null);
        
        graphics.setTransform(oldXForm);
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
    
    private void drawUnit(Graphics2D graphics, Unit unit) {
        int x = (int) (unit.getLocation().x * PIXELS_PER_BOARD_UNIT);
        int y = (int) (unit.getLocation().y * PIXELS_PER_BOARD_UNIT);
        int width = (int) (unit.getSize() * PIXELS_PER_BOARD_UNIT);
        int height = (int) (unit.getSize() * PIXELS_PER_BOARD_UNIT);
        
        AffineTransform oldXForm = graphics.getTransform();
        graphics.rotate(Pathing.getDirection(unit.getLocation(), unit.getCurrentDestination()), x, y);
        
        Class<? extends Asset> animatedClass = unit.getAnimationClass();
        if (animatedClass.isAnnotationPresent(HasAnimation.class)) {
            BufferedImage image = AnimationManager.getInstance().getFrame(animatedClass, unit.getAnimationFrame());
            x = x - (width / 2);
            y = y - (height / 2);
            graphics.drawImage(image, x, y, x + width, y + height, 0, 0, image.getWidth() - 1, image.getHeight() - 1, null);
        } else {
            Ellipse2D.Double circle = new Ellipse2D.Double(x - (unit.getSize() * PIXELS_PER_BOARD_UNIT / 2), y - (unit.getSize() * PIXELS_PER_BOARD_UNIT / 2), width, height);
            graphics.fill(circle);
        }
        
        graphics.setTransform(oldXForm);
        
        if (unit.getHealthRatio() < 1) {
            drawHealthbar(graphics, unit);
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
        
        drawMinimap(graphics);        
        drawStats(graphics);        
        drawSystemStats(graphics);
        drawBuildMenu(graphics);
        drawNewBuildingPreview(graphics);
        drawGameResult(graphics);
    }

    private void drawGameResult(Graphics2D graphics) {
        FontMetrics fm = graphics.getFontMetrics();
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

    private void drawNewBuildingPreview(Graphics2D graphics) {
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
                if (Game.getInstance().getMe().getMoney() >= building.cost()) {
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
    }

    private void drawBuildMenu(Graphics2D graphics) {
        FontMetrics fm = graphics.getFontMetrics();
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
    }

    private void drawSystemStats(Graphics2D graphics) {
        FontMetrics fm = graphics.getFontMetrics();
        graphics.setColor(new Color(0, 255, 0));
        String pingString = "Ping: " + ConnectionToOpponent.getInstance().getPing();
        graphics.drawString(pingString, viewDimensions.width - fm.stringWidth(pingString) - 10, viewDimensions.height - 10);
        String fpsString = "FPS: " + getFps();
        graphics.drawString(fpsString, viewDimensions.width - fm.stringWidth(fpsString) - 10, viewDimensions.height - 10 - fm.getHeight());
    }

    private void drawStats(Graphics2D graphics) {
        FontMetrics fm = graphics.getFontMetrics();
        graphics.setColor(new Color(0, 255, 0));
        String moneyString = "Money: " + (int) Game.getInstance().getMe().getMoney();
        graphics.drawString(moneyString, 10, fm.getHeight());
        String incomeString = "Income: " + (int) Game.getInstance().getMe().getIncome();
        graphics.drawString(incomeString, 10, fm.getHeight() * 2);
        graphics.drawRect(10, (fm.getHeight()) * 2 + 10, fm.stringWidth("Income"), 10);
        double incomeProgress = fm.stringWidth("Income") * (double) (currentTime - Game.getInstance().getMe().getLastIncomeTime()) / Game.INCOME_COOLDOWN;
        graphics.fillRect(10, (fm.getHeight()) * 2 + 10, (int) incomeProgress, 10);
    }

    private void drawMinimap(Graphics2D graphics) {
        //border
        graphics.setColor(new Color(255, 255, 255, 64));
        graphics.setStroke(new BasicStroke(3));
        graphics.drawRect((viewDimensions.width / 2) - (MINIMAP_WIDTH_PIXELS / 2), 0, MINIMAP_WIDTH_PIXELS, MINIMAP_HEIGHT_PIXELS);
        
        drawMinimapContents(graphics);
        
        //view indicator
        graphics.setColor(new Color(0, 128, 255, 255));
        graphics.setStroke(new BasicStroke(1));
        int minimapViewX = (viewDimensions.width / 2) - (MINIMAP_WIDTH_PIXELS / 2) + (int) ((double) viewCornerPixelX / image.getWidth() * MINIMAP_WIDTH_PIXELS);
        int minimapViewY = (int) ((double) viewCornerPixelY / image.getHeight() * MINIMAP_HEIGHT_PIXELS);
        int minimapViewWidth = (int) ((double) viewDimensions.width / image.getWidth() * MINIMAP_WIDTH_PIXELS);
        int minimapViewHeight = (int) ((double) viewDimensions.height / image.getHeight() * MINIMAP_HEIGHT_PIXELS);
        graphics.drawRect(minimapViewX, minimapViewY, minimapViewWidth, minimapViewHeight);
    }

    private void drawMinimapContents(Graphics2D graphics) {
        AffineTransform oldXForm = graphics.getTransform();
        Composite oldComposite = graphics.getComposite();
        
        graphics.translate((viewDimensions.width / 2) - (MINIMAP_WIDTH_PIXELS / 2), 0);
        graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
        
        graphics.setColor(new Color(0, 255, 0));
        Map<Long, Building> myBuildings = Game.getInstance().getMyBuildings();
        synchronized (myBuildings) {
            for (Building building : myBuildings.values()) {
                drawMinimapAsset(graphics, building);
            }
        }
        
        Map<Long, PlayerUnit> myUnits = Game.getInstance().getMyUnits();
        synchronized (myUnits) {
            for (PlayerUnit unit : myUnits.values()) {
                drawMinimapAsset(graphics, unit);
            }
        }
        
        graphics.setColor(new Color(255, 0, 0));
        for (NetworkAsset building : Game.getInstance().getTheirBuildings().values()) {
            drawMinimapAsset(graphics, building);
        }
        
        for (NetworkUnit unit : Game.getInstance().getTheirUnits().values()) {
            drawMinimapAsset(graphics, unit);
        }
        
        graphics.setTransform(oldXForm);
        graphics.setComposite(oldComposite);
    }
    
    private void drawMinimapAsset(Graphics2D graphics, Asset asset) {
        int x = (int) ((asset.getHitLocation().x - asset.getSize() / 2.0) * MINIMAP_PIXELS_PER_BOARD_UNIT);
        int y = (int) ((asset.getHitLocation().y - asset.getSize() / 2.0) * MINIMAP_PIXELS_PER_BOARD_UNIT);
        int size = (int) Math.max(1, asset.getSize() * MINIMAP_PIXELS_PER_BOARD_UNIT);
        
        graphics.fillRect(x, y, size, size);
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
    
    public long getFps() {
        if (loopTimes.size() < 2) {
            return 0;
        }
        
        synchronized (loopTimes) {
            return 1000 / ((loopTimes.get(loopTimes.size() - 1) - loopTimes.get(0)) / (loopTimes.size() - 1));
        }
    }
    
}
