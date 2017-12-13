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
    private final String name;
    
    public Items(String imageName, String name, int value) {
        super(imageName);
        this.name = name;
        
        this.setScore(value);

        this.isMortal = true;
        this.isTransposable = true;
    }
    
    public Items(ImageIcon image, String name, int value) {
        super(image);
        
        this.setScore(value);
        this.name = name;

        this.isMortal = true;
        this.isTransposable = true;
    }
    
    public String getName() {
        return this.name;
    }
    
    @Override
    public void autoDraw(Graphics g) {
        Drawing.draw(g, this.imageIcon, pos.getX(), pos.getY());
    }
    
}
