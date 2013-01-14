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
    
    public static final long DEFAULT_FRAME_DURATION = 250;
    
    private Map<Class<?>, ArrayList<BufferedImage>> images;
    
    private static AnimationManager theInstance;
    
    public static AnimationManager getInstance() {
        if (theInstance == null) {
            theInstance = new AnimationManager();
        }
        return theInstance;
    }
    
    private AnimationManager() {
        images = new HashMap<Class<?>, ArrayList<BufferedImage>>();
        
        try {
            Reflections reflections = new Reflections("com.rossallenbell.strifebasin");
            for (Class<?> imagedClass : reflections.getTypesAnnotatedWith(HasAnimation.class)) {
                images.put(imagedClass, new ArrayList<BufferedImage>());
                String folderPath = "images/" + imagedClass.getSimpleName().toLowerCase() + "/";
                for(int i=0; ImageManager.class.getClassLoader().getResource(folderPath + i + ".png") != null; i++) {
                    URL resourceURL = ImageManager.class.getClassLoader().getResource(folderPath + i + ".png");           
                    BufferedImage image = ImageIO.read(resourceURL);
                    images.get(imagedClass).add(image);         
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public int getFrameCount(Class<?> clazz) {
        return images.get(clazz).size();
    }
    
    public BufferedImage getFrame(Class<?> clazz, int frameNumber) {
        if (!images.containsKey(clazz) || getFrameCount(clazz) <= frameNumber) {
            return null;
        }
        
        return images.get(clazz).get(frameNumber);
    }
    
}
