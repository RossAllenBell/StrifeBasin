package com.rossallenbell.strifebasin.ui.resources;

import java.awt.image.BufferedImage;
import java.io.Serializable;

import com.rossallenbell.strifebasin.domain.Asset;
import com.rossallenbell.strifebasin.ui.resources.AnimationManager.Action;

public class FrameHelper implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private final Class<? extends Asset> animatedClass;
    private final boolean isMine;
    private AnimationManager.Action currentAction;
    private long lastActionChangeTime;
    private long lastFrameChangeTime;
    private int currentFrameNumber;
    
    public FrameHelper(Class<? extends Asset> animatedClass, boolean isMine) {
        this.animatedClass = animatedClass;
        this.isMine = isMine;
        currentAction = AnimationManager.Action.IDLING;
    }
    
    public BufferedImage getCurrentFrame() {
        return AnimationManager.getInstance().getFrame(animatedClass, currentAction, currentFrameNumber, isMine);
    }
    
    public void setAction(Action action) {
        setAction(action, System.currentTimeMillis());
    }
    
    public void setAction(AnimationManager.Action action, long asOfTime) {
        if (currentAction != action && (currentAction != AnimationManager.Action.ATTACKING || asOfTime - lastActionChangeTime >= 1000)) {
            currentAction = action;
            lastFrameChangeTime = asOfTime;
            lastActionChangeTime = asOfTime;
            currentFrameNumber = 0;
        }
    }
    
    public void update(long updateTime) {
        if (lastFrameChangeTime + AnimationManager.DEFAULT_FRAME_DURATION <= updateTime) {
            lastFrameChangeTime = updateTime;
            currentFrameNumber = ++currentFrameNumber % AnimationManager.getInstance().getFrameCount(animatedClass, currentAction);
        }
    }
    
}
