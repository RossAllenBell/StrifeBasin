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
    
    public static final long DEFAULT_FRAME_DURATION = 150;
    
    private Map<Class<?>, ArrayList<BufferedImage>> myImages;
    private Map<Class<?>, ArrayList<BufferedImage>> theirImages;
    
    private static AnimationManager theInstance;
    
    public static AnimationManager getInstance() {
        if (theInstance == null) {
            theInstance = new AnimationManager();
        }
        return theInstance;
    }
    
    private AnimationManager() {
        myImages = new HashMap<Class<?>, ArrayList<BufferedImage>>();
        theirImages = new HashMap<Class<?>, ArrayList<BufferedImage>>();
        
        try {
            Reflections reflections = new Reflections("com.rossallenbell.strifebasin");
            for (Class<?> imagedClass : reflections.getTypesAnnotatedWith(HasAnimation.class)) {
                myImages.put(imagedClass, new ArrayList<BufferedImage>());
                theirImages.put(imagedClass, new ArrayList<BufferedImage>());
                String folderPath = "images/" + imagedClass.getSimpleName().toLowerCase() + "/";
                for (int i = 0; ImageManager.class.getClassLoader().getResource(folderPath + i + ".png") != null; i++) {
                    URL resourceURL = ImageManager.class.getClassLoader().getResource(folderPath + i + ".png");
                    BufferedImage image = ImageIO.read(resourceURL);
                    BufferedImage myImage = ImageManager.deepCopy(image);
                    BufferedImage theirImage = ImageManager.deepCopy(image);
                    ImageManager.tintImage(myImage, ImageManager.MY_TINT);
                    ImageManager.tintImage(theirImage, ImageManager.THEIR_TINT);
                    myImages.get(imagedClass).add(myImage);
                    theirImages.get(imagedClass).add(theirImage);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public int getFrameCount(Class<?> clazz) {
        return myImages.get(clazz).size();
    }
    
    public BufferedImage getFrame(Class<?> clazz, int frameNumber, boolean isMine) {
        if (!myImages.containsKey(clazz) || getFrameCount(clazz) <= frameNumber) {
            return null;
        }
        
        return isMine? myImages.get(clazz).get(frameNumber) : theirImages.get(clazz).get(frameNumber);
    }
    
}
