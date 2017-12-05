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
    
    public Sprite(String imageName) {
        icons = new HashMap<>();
        
        try {
            ImageIcon icon = new ImageIcon(new java.io.File(".").getCanonicalPath() + Consts.PATH + imageName);
            height = icon.getIconHeight();
            width = icon.getIconWidth();
            sprite = icon.getImage();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
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
        
    public ImageIcon getImage(int key) {
        return icons.get(key);
    }
}