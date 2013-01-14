package com.rossallenbell.strifebasin.ui.resources;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import org.reflections.Reflections;

public class ImageManager {
    
    private Map<Class<?>, BufferedImage> images;
    
    private static ImageManager theInstance;
    
    public static ImageManager getInstance() {
        if (theInstance == null) {
            theInstance = new ImageManager();
        }
        return theInstance;
    }
    
    private ImageManager() {
        images = new HashMap<Class<?>, BufferedImage>();
        
        try {
            Reflections reflections = new Reflections("com.rossallenbell.strifebasin");
            for (Class<?> imagedClass : reflections.getTypesAnnotatedWith(HasImage.class)) {
                String imagePath = "images/" + imagedClass.getSimpleName().toLowerCase() + ".png";
                URL resourceURL = ImageManager.class.getClassLoader().getResource(imagePath);
                BufferedImage image = ImageIO.read(resourceURL);
                images.put(imagedClass, image);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public BufferedImage getImage(Class<?> clazz) {
        return images.get(clazz);
    }
    
}
