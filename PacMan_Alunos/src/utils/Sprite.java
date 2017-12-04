/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.awt.Graphics;
import java.awt.Image;
import java.util.HashMap;
import java.awt.image.BufferedImage;

import java.io.IOException;
import java.io.Serializable;
import javax.swing.ImageIcon;

/**
 *
 * @author suzukawa
 */
public class Sprite implements Serializable {
    private Image _sprite = null;
    private int _height;
    private int _width;
    private HashMap<Integer, ImageIcon> _icons;
    
    public Sprite(String imageName) {
        _icons = new HashMap<>();
        
        try {
            ImageIcon icon = new ImageIcon(new java.io.File(".").getCanonicalPath() + Consts.PATH + imageName);
            _height = icon.getIconHeight();
            _width = icon.getIconWidth();
            _sprite = icon.getImage();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    public void newImage(int key, int x, int y, int sizeX, int sizeY, float resizeFactor) {
        BufferedImage bi = new BufferedImage((int)(sizeX*resizeFactor), (int)(sizeY*resizeFactor), BufferedImage.TYPE_INT_ARGB);
        Graphics g = bi.createGraphics();
        g.drawImage(_sprite, -(int)(x*resizeFactor), -(int)(y*resizeFactor), (int)(_width*resizeFactor), (int)(_height*resizeFactor), null);
        _icons.put(key, new ImageIcon(bi));
    }
        
    public ImageIcon getImage(int key) {
        return _icons.get(key);
    }
}
