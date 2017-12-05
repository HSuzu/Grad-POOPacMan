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
    private int score; 
    
    public Items(String imageName, String name, int score) {
        super(imageName);
        this.name = name;
        this.score = score;
        
    }
    
    public Items(ImageIcon image, String name, int score) {
        super(image);

        this.name = name;
        this.score = score;
    }
    
    public String getName() {
        return this.name;
    }
    
    public int getScore() {
        return this.score;
    }
    
    @Override
    public void autoDraw(Graphics g) {
        Drawing.draw(g, this.imageIcon, pos.getY(), pos.getX());
    }
    
}
