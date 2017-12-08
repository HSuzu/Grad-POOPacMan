/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package elements;


import java.awt.Graphics;
import java.io.Serializable;
import utils.Consts;
import utils.Drawing;
import utils.ImageCollection;

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
    
    private int nextMovDirection = STOP;
    private int movDirection = STOP;
    private int numLifes = 3;
    private int lifeControl = 1;
    
    public PacMan(String imageName) {
        super(imageName);
        this.isTransposable = false;
    }
        
    public PacMan(ImageCollection collection, int defaultImage) {
        super(collection, defaultImage);
        this.isTransposable = false;
    }

    public int getNumLifes() {
        return this.numLifes;
    }
    
    public void winPoints(int value) {
        this.setScore(this.getScore()+value);
        if(this.getScore()/(10000*this.lifeControl) >= 1) {
            this.numLifes += 1;
            this.lifeControl += 1;
        }
    }
    
    public void die() {
        this.numLifes -= 1;
    }
    
    @Override
    public void autoDraw(Graphics g){
        Drawing.draw(g, this.imageIcon, pos.getX(), pos.getY());
    }
    
    public void backToLastPosition(){
        this.collection.stopAnimation();
        
        this.pos.comeBack();
    }
    
    public void setMovDirection(int direction) {
        nextMovDirection = direction;
    }
    
    public void resetMovDirection() {
        nextMovDirection = movDirection;
    }
    
    public int getMovimentDirection() {
        return nextMovDirection;
    }
    
    public void move() {
        System.out.println("PacMan: "+this.pos.toString());
        if(nextMovDirection == STOP) {
            movDirection = STOP;
        } else if(pos.isRoundPosition(3.0*Consts.WALK_STEP)) {
            if(movDirection != nextMovDirection) {
                pos.roundPosition();
                movDirection = nextMovDirection;
            }
        }
        
        switch (movDirection) {
            case MOVE_LEFT:
                imageIcon = this.collection.getImage(Consts.Animation.PACMAN_LEFT);
                if(this.moveLeft() == false) {
                    this.collection.stopAnimation();
                } else {
                   this.collection.startAnimation();
                }
                break;
            case MOVE_RIGHT:
                imageIcon = this.collection.getImage(Consts.Animation.PACMAN_RIGHT);
                if(this.moveRight() == false) {
                    this.collection.stopAnimation();
                } else {
                    this.collection.startAnimation();
                }
                break;
            case MOVE_UP:
                imageIcon = this.collection.getImage(Consts.Animation.PACMAN_UP);
                if(this.moveUp() == false) {
                    this.collection.stopAnimation();
                } else {
                    this.collection.startAnimation();
                }
                break;
            case MOVE_DOWN:
                imageIcon = this.collection.getImage(Consts.Animation.PACMAN_DOWN);
                if(this.moveDown() == false) {
                    this.collection.stopAnimation();
                } else {
                    this.collection.startAnimation();
                }
                break;
            default:
                this.collection.stopAnimation();
                break;
        }
    }
}
