package com.rossallenbell.strifebasin.ui.resources;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import org.reflections.Reflections;

public class AnimationManager {
    
    public enum Action {
        IDLING, MOVING, ATTACKING
    }
    
    public static final long DEFAULT_FRAME_DURATION = 150;
    public static final long DEFAULT_ATTACK_DURATION = 1000;
    
    private Map<Class<?>, Map<Action, ArrayList<BufferedImage>>> myImages;
    private Map<Class<?>, Map<Action, ArrayList<BufferedImage>>> theirImages;
    
    private static AnimationManager theInstance;
    
    public static AnimationManager getInstance() {
        if (theInstance == null) {
            synchronized (AnimationManager.class) {
                if (theInstance == null) {
                    theInstance = new AnimationManager();
                }
            }
        }
        return theInstance;
    }
    
    private AnimationManager() {
        myImages = new HashMap<Class<?>, Map<Action, ArrayList<BufferedImage>>>();
        theirImages = new HashMap<Class<?>, Map<Action, ArrayList<BufferedImage>>>();
        
        try {
            Reflections reflections = new Reflections("com.rossallenbell.strifebasin");
            for (Class<?> imagedClass : reflections.getTypesAnnotatedWith(HasAnimation.class)) {
                myImages.put(imagedClass, new HashMap<Action, ArrayList<BufferedImage>>());
                theirImages.put(imagedClass, new HashMap<Action, ArrayList<BufferedImage>>());
                for (Action action : Action.values()) {
                    myImages.get(imagedClass).put(action, new ArrayList<BufferedImage>());
                    theirImages.get(imagedClass).put(action, new ArrayList<BufferedImage>());
                }
                
                String folderPath;
                
                folderPath = "images/" + imagedClass.getSimpleName().toLowerCase() + "/";
                for (int i = 0; ImageManager.class.getClassLoader().getResource(folderPath + i + ".png") != null; i++) {
                    URL resourceURL = ImageManager.class.getClassLoader().getResource(folderPath + i + ".png");
                    BufferedImage image = ImageIO.read(resourceURL);
                    BufferedImage myImage = ImageManager.deepCopy(image);
                    BufferedImage theirImage = ImageManager.deepCopy(image);
                    ImageManager.tintImage(myImage, ImageManager.MY_TINT);
                    ImageManager.tintImage(theirImage, ImageManager.THEIR_TINT);
                    myImages.get(imagedClass).get(Action.MOVING).add(myImage);
                    theirImages.get(imagedClass).get(Action.MOVING).add(theirImage);
                    if (i == 0) {
                        myImages.get(imagedClass).get(Action.IDLING).add(myImage);
                        theirImages.get(imagedClass).get(Action.IDLING).add(theirImage);
                    }
                }
                
                folderPath = "images/" + imagedClass.getSimpleName().toLowerCase() + "/attack/";
                for (int i = 0; ImageManager.class.getClassLoader().getResource(folderPath + i + ".png") != null; i++) {
                    URL resourceURL = ImageManager.class.getClassLoader().getResource(folderPath + i + ".png");
                    BufferedImage image = ImageIO.read(resourceURL);
                    BufferedImage myImage = ImageManager.deepCopy(image);
                    BufferedImage theirImage = ImageManager.deepCopy(image);
                    ImageManager.tintImage(myImage, ImageManager.MY_TINT);
                    ImageManager.tintImage(theirImage, ImageManager.THEIR_TINT);
                    myImages.get(imagedClass).get(Action.ATTACKING).add(myImage);
                    theirImages.get(imagedClass).get(Action.ATTACKING).add(theirImage);
                }
                
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public int getFrameCount(Class<?> clazz, Action action) {
        return myImages.get(clazz).get(action).size();
    }
    
    public BufferedImage getFrame(Class<?> clazz, Action action, int frameNumber, boolean isMine) {
        if (!myImages.containsKey(clazz) || getFrameCount(clazz, action) <= frameNumber) {
            return null;
        }
        
        return isMine ? myImages.get(clazz).get(action).get(frameNumber) : theirImages.get(clazz).get(action).get(frameNumber);
    }
    
}
