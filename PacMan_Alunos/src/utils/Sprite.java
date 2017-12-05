package utils;

import java.awt.Graphics;
import java.awt.Image;
import java.util.HashMap;
import java.awt.image.BufferedImage;

import java.io.IOException;
import java.io.Serializable;
import javax.swing.ImageIcon;

public class Sprite implements Serializable {
    private Image sprite = null;
    private int height;
    private int width;
    private HashMap<Integer, ImageIcon> icons;
    
    private int iconHeight;
    private int iconWidth;
    private float defaultResizeFactor;
    
    public Sprite(String imageName) {
        icons = new HashMap<>();
        defaultResizeFactor = 1.0f;
        
        try {
            ImageIcon icon = new ImageIcon(new java.io.File(".").getCanonicalPath() + Consts.PATH + imageName);
            height = icon.getIconHeight();
            width = icon.getIconWidth();
            
            iconHeight = height;
            iconWidth = width;
            
            sprite = icon.getImage();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    public void setDefaultParameters(int iconHeight, int iconWidth, float resizeFactor) {
        this.iconHeight = iconHeight;
        this.iconWidth = iconWidth;
        this.defaultResizeFactor = resizeFactor;
    }
    
    public void newImage(int key, int x, int y, int sizeX, int sizeY, float resizeFactor) {
        BufferedImage bi = new BufferedImage((int)(sizeX*resizeFactor), (int)(sizeY*resizeFactor), BufferedImage.TYPE_INT_ARGB);
        Graphics g = bi.createGraphics();
        g.drawImage(sprite, -(int)(x*resizeFactor),
                            -(int)(y*resizeFactor),
                             (int)(width*resizeFactor),
                             (int)(height*resizeFactor), null);
        icons.put(key, new ImageIcon(bi));
    }
    
    public void newImage(Consts.Sprite key, int x, int y, int sizeX, int sizeY, float resizeFactor) {
        newImage(key.ordinal(), x, y, sizeX, sizeY, resizeFactor);
    }
    
    public void newImage(int key, int x, int y) {
        newImage(key, x, y, iconWidth, iconHeight, defaultResizeFactor);
    }
    
    public void newImage(Consts.Sprite key, int x, int y) {
        newImage(key.ordinal(), x*iconWidth, y*iconHeight, iconWidth, iconHeight, defaultResizeFactor);
    }
    
    public ImageIcon getImage(int key) {
        return icons.get(key);
    }
    
    public ImageIcon getImage(Consts.Sprite key) {
        return icons.get(key.ordinal());
    }
}
