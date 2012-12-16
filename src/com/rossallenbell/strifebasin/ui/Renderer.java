package com.rossallenbell.strifebasin.ui;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.rossallenbell.strifebasin.domain.Game;

public class Renderer {
    
    private Game game;
    BufferedImage image;
    Dimension viewDimensions;
    
    public Renderer(Game game) {
        this.game = game;
    }
    
    public void render(Graphics2D graphics) {
        //BufferedImageOp scaleOp = new RescaleOp(1.0f, 0.0f, new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED));
        if(image != null){
            Graphics2D g = image.createGraphics();
            g.clearRect(0, 0, image.getWidth(), image.getHeight());
            
            graphics.drawImage(image, 0, 0, null);
        }
    }
    
    public void resizeView(Dimension viewDimensions) {
        if(viewDimensions.width > 0 && viewDimensions.height > 0){
            this.viewDimensions = viewDimensions;
            image = new BufferedImage(viewDimensions.width, viewDimensions.height, BufferedImage.TYPE_INT_RGB);
        }
    }
    
}
