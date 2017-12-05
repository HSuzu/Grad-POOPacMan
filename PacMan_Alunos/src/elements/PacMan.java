/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package elements;

import static elements.Lolo.MOVE_DOWN;
import static elements.Lolo.MOVE_LEFT;
import static elements.Lolo.MOVE_RIGHT;
import static elements.Lolo.MOVE_UP;
import static elements.Lolo.STOP;
import java.awt.Graphics;
import java.io.Serializable;
import javax.swing.ImageIcon;
import utils.Animation;
import utils.Drawing;
import utils.ImageCollection;
import utils.Sprite;

/**
 *
 * @author aribeiro
 */
public class PacMan extends Element  implements Serializable {
    public static final int STOP = 0;
    public static final int MOVE_LEFT = 1;
    public static final int MOVE_RIGHT = 2;
    public static final int MOVE_UP = 3;
    public static final int MOVE_DOWN = 4;
    
    private int movDirection = STOP;
    private int score = 0;
    private int numLifes = 3;
    private int lifeControl = 1;
    
    public PacMan(String imageName) {
        super(imageName);
    }
    
    public PacMan(ImageIcon image) {
        super(image);
    }
    
    public PacMan(Sprite sprite, int defaultImage) {
        super(sprite, defaultImage);
    }
    
    public PacMan(Animation animation) {
        super(animation);
    }
    
    public PacMan(ImageCollection collection, int defaultImage) {
        super(collection, defaultImage);
    }

    public int getScore() {
        return this.score;
    }
    
    public void winPoints(int value) {
        this.score += value;
        if(score/(10000*this.lifeControl) >= 1) {
            this.numLifes += 1;
            this.lifeControl += 1;
        }
    }
    
    public void die() {
        this.numLifes -= 1;
    }
    
    @Override
    public void autoDraw(Graphics g){
        Drawing.draw(g, this.imageIcon, pos.getY(), pos.getX());
    }
    
    public void backToLastPosition(){
        _collection.stopAnimation();
        
        this.pos.comeBack();
    }
    
    public void setMovDirection(int direction) {
        movDirection = direction;
    }
    
    public void move() {
        switch (movDirection) {
            case MOVE_LEFT:
                imageIcon = _collection.getImage(0);
                if(this.moveLeft() == false) {
                    _collection.stopAnimation();
                } else {
                    _collection.startAnimation();
                }
                break;
            case MOVE_RIGHT:
                imageIcon = _collection.getImage(2);
                this.moveRight();
                break;
            case MOVE_UP:
                imageIcon = _collection.getImage(1);
                this.moveUp();
                break;
            case MOVE_DOWN:
                imageIcon = _collection.getImage(3);
                this.moveDown();
                break;
            default:
                break;
        }
    }
}
