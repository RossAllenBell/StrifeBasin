package com.rossallenbell.strifebasin.ui.resources;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import org.reflections.Reflections;

public class ImageManager {
    
    public static final int TINT_LEVEL = 52;
    public static final Color MY_TINT = new Color(0, TINT_LEVEL, 0, 255);
    public static final Color THEIR_TINT = new Color(TINT_LEVEL, 0, 0, 255);
    
    private Map<Class<?>, BufferedImage> myImages;
    private Map<Class<?>, BufferedImage> theirImages;
    
    private static ImageManager theInstance;
    
    public static ImageManager getInstance() {
        if (theInstance == null) {
            synchronized (ImageManager.class) {
                if (theInstance == null) {
                    theInstance = new ImageManager();
                }
            }
        }
        return theInstance;
    }
    
    private ImageManager() {
        myImages = new HashMap<Class<?>, BufferedImage>();
        theirImages = new HashMap<Class<?>, BufferedImage>();
        
        try {
            Reflections reflections = new Reflections("com.rossallenbell.strifebasin");
            for (Class<?> imagedClass : reflections.getTypesAnnotatedWith(HasImage.class)) {
                String imagePath = "images/" + imagedClass.getSimpleName().toLowerCase() + ".png";
                URL resourceURL = ImageManager.class.getClassLoader().getResource(imagePath);
                BufferedImage image = ImageIO.read(resourceURL);
                BufferedImage myImage = deepCopy(image);
                BufferedImage theirImage = deepCopy(image);
                tintImage(myImage, MY_TINT);
                tintImage(theirImage, THEIR_TINT);
                myImages.put(imagedClass, myImage);
                theirImages.put(imagedClass, theirImage);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public BufferedImage getImage(Class<?> clazz, boolean isMine) {
        return isMine ? myImages.get(clazz) : theirImages.get(clazz);
    }
    
    public static BufferedImage deepCopy(BufferedImage bufferedImage) {
        ColorModel colorModel = bufferedImage.getColorModel();
        boolean isAlphaPremultiplied = colorModel.isAlphaPremultiplied();
        WritableRaster raster = bufferedImage.copyData(null);
        return new BufferedImage(colorModel, raster, isAlphaPremultiplied, null);
    }
    
    public static void tintImage(BufferedImage image, Color tint) {
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                int pixelColorInt = image.getRGB(x, y);
                if (pixelColorInt >> 24 != 0x00) {
                    Color pixelColor = new Color(pixelColorInt, true);
                    Color newPixelColor = new Color(Math.min(255, tint.getRed() + pixelColor.getRed()), Math.min(255, tint.getGreen() + pixelColor.getGreen()), Math.min(255, tint.getBlue() + pixelColor.getBlue()), pixelColor.getAlpha());
                    image.setRGB(x, y, newPixelColor.getRGB());
                }
            }
        }
    }
    
}
