package elements.phantom;

import control.WorldMap;
import elements.Element;
import java.awt.Graphics;
import java.io.Serializable;
import javax.swing.ImageIcon;
import utils.Consts;
import utils.Drawing;
import utils.ImageCollection;
import utils.Position;

public abstract class Phantom extends Element implements Serializable {
    public static enum State {
        DEADLY,
        EDIBLE
    }
    
    public static final int STOP = 0;
    public static final int MOVE_LEFT = 1;
    public static final int MOVE_RIGHT = 2;
    public static final int MOVE_UP = 3;
    public static final int MOVE_DOWN = 4;
    
    private int nextMovDirection = MOVE_LEFT;
    private int movDirection = MOVE_LEFT;
    private State state;
    
    protected static WorldMap wm;
    
    public Phantom(String imageName, int value) {
        super(imageName);
        
        this.isTransposable = false;
        this.score = value;
        state = State.DEADLY;
        
        Phantom.wm = WorldMap.getInstance();
        
    }
    
    public Phantom(ImageCollection collection, int defaultImage, int value) {
        super(collection, defaultImage);
        
        this.score = value;
        state = State.DEADLY;
        
        Phantom.wm = WorldMap.getInstance();
        
    }
    
    public void setState(State state) {
        this.state = state;
    }
    
    public State state() {
        return state;
    }
    
    abstract public String name();
    
    abstract protected void navigation();

    abstract protected ImageIcon getImage(int movDirection);
    
    protected void runAway() {
        Position avoidPos = wm.getPacManPosition();
        
        byte freeSides = wm.freePath((int)Math.round(this.pos.getX()), (int)Math.round(this.pos.getY()));
        
        if(avoidPos.getX() > this.pos.getX() && ((freeSides & WorldMap.RIGHT) == WorldMap.RIGHT)) {
           this.setNextMovDirection(MOVE_RIGHT);
        }
        else if(avoidPos.getX() < this.pos.getX() && ((freeSides & WorldMap.LEFT) == WorldMap.LEFT)) {
            this.setNextMovDirection(MOVE_LEFT);
        }
        else if(avoidPos.getY() > this.pos.getY() && ((freeSides & WorldMap.DOWN) == WorldMap.DOWN)) {
            this.setNextMovDirection(MOVE_DOWN);
        }
        else if(avoidPos.getY() < this.pos.getY() && ((freeSides & WorldMap.UP) == WorldMap.UP)) {
            this.setNextMovDirection(MOVE_UP);
        }
    }

    
    @Override
    public void autoDraw(Graphics g) {
        Drawing.draw(g, this.imageIcon, pos.getX(), pos.getY());
    }
    
    public void setNextMovDirection(int direction) {
        this.nextMovDirection = direction;
    }
    
    public void move() {
        if(state == State.EDIBLE) {
            runAway();
        }
        else {
            if(movDirection == nextMovDirection) {
                navigation();
            }
            else {
                if(pos.isRoundPosition(3.0*Consts.WALK_STEP)) {
                    pos.roundPosition();
                    movDirection = nextMovDirection;
                }
            } 

            switch (movDirection) {
                case MOVE_LEFT:
                    imageIcon = getImage(MOVE_LEFT);
                    this.moveLeft();
                    this.collection.startAnimation();
                    break;
                case MOVE_RIGHT:
                    imageIcon = getImage(MOVE_RIGHT);
                    this.moveRight();
                    this.collection.startAnimation();
                    break;
                case MOVE_UP:
                    imageIcon = getImage(MOVE_UP);
                    this.moveUp();
                    this.collection.startAnimation();
                    break;
                case MOVE_DOWN:
                    imageIcon = getImage(MOVE_DOWN);
                    this.moveDown();
                    this.collection.startAnimation();
                    break;
                default:
                    break;
            } 
        }    
    }
}
