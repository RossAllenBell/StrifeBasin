package com.rossallenbell.strifebasin.ui.resources;

import java.awt.image.BufferedImage;
import java.io.Serializable;

import com.rossallenbell.strifebasin.domain.Asset;

public class FrameHelper implements Serializable {
    
    private static final long serialVersionUID = 1L;

    public enum Action {
        IDLING, MOVING, ATTACKING
    }
    
    private final Class<? extends Asset> animatedClass;
    private final boolean isMine;
    private Action currentAction;
    private long lastFrameChangeTime;
    private int currentFrameNumber;
    
    public FrameHelper(Class<? extends Asset> animatedClass, boolean isMine) {
        this.animatedClass = animatedClass;
        this.isMine = isMine;
        currentAction = Action.IDLING;
    }
    
    public BufferedImage getCurrentFrame() {
        BufferedImage frameImage;
        
        if (currentAction == Action.IDLING) {
            frameImage = AnimationManager.getInstance().getFrame(animatedClass, 0, isMine);
        } else {
            frameImage = AnimationManager.getInstance().getFrame(animatedClass, currentFrameNumber, isMine);
        }
        return frameImage;
    }
    
    public void setAction(Action action, long asOfTime) {
        if (currentAction != action) {
            currentAction = action;
            lastFrameChangeTime = asOfTime;
            currentFrameNumber = 0;
        }
    }
    
    public void update(long updateTime) {
        if (lastFrameChangeTime + AnimationManager.DEFAULT_FRAME_DURATION <= updateTime) {
            lastFrameChangeTime = updateTime;
            currentFrameNumber = ++currentFrameNumber % AnimationManager.getInstance().getFrameCount(animatedClass);
        }
    }
    
}
