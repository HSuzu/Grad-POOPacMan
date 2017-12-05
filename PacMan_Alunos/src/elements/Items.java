/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package elements;

import java.awt.Graphics;
import java.io.Serializable;
import javax.swing.ImageIcon;
import utils.Drawing;

/**
 *
 * @author aribeiro
 */
public class Items extends Element implements Serializable {
    private String name;
    
    public Items(String imageName, String name, int value) {
        super(imageName);
        this.name = name;
        this.setScore(value);
    }
    
    public Items(ImageIcon image, String name, int value) {
        super(image);
        this.setScore(value);
        this.name = name;
    }
    
    public String getName() {
        return this.name;
    }
    
    @Override
    public void autoDraw(Graphics g) {
        Drawing.draw(g, this.imageIcon, pos.getY(), pos.getX());
    }
    
}
