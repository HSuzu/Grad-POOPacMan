package elements;

import utils.Drawing;
import java.awt.Graphics;
import java.io.Serializable;
import javax.swing.ImageIcon;
import utils.Animation;
import utils.Consts;
import utils.ImageCollection;
import utils.Sprite;

/**
 * Projeto de POO 2017
 * 
 * @author Luiz Eduardo
 * Baseado em material do Prof. Jose Fernando Junior
 */
public class Lolo extends Element  implements Serializable{
    
    public static final int STOP = 0;
    public static final int MOVE_LEFT = 1;
    public static final int MOVE_RIGHT = 2;
    public static final int MOVE_UP = 3;
    public static final int MOVE_DOWN = 4;
    
    private int movDirection = STOP;
    
    public Lolo(String imageName) {
        super(imageName);
    }
    
    public Lolo(ImageIcon image) {
        super(image);
    }
    
    public Lolo(Sprite sprite, int defaultImage) {
        super(sprite, defaultImage);
    }
    
    public Lolo(Animation animation) {
        super(animation);
    }
    
    public Lolo(ImageCollection collection, int defaultImage) {
        super(collection, defaultImage);
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
