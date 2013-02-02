package com.rossallenbell.strifebasin.ui.effects.imaged;

import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

import com.rossallenbell.strifebasin.ui.effects.Effect;
import com.rossallenbell.strifebasin.ui.resources.ImageManager;

public abstract class ImagedEffect extends Effect {
    
    public ImagedEffect(long startTime, Point2D.Double location) {
        super(startTime, location);
    }

    @Override
    public BufferedImage getImage() {
        return ImageManager.getInstance().getImage(this.getClass(), true);
    }
    
}
