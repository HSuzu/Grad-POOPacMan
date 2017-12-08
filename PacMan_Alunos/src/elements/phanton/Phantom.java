package elements.phanton;

import control.WorldMap;
import elements.Element;
import java.awt.Graphics;
import javax.swing.ImageIcon;
import utils.Consts;
import utils.Drawing;

public abstract class Phantom extends Element {
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
        this.setScore(value);
        state = State.DEADLY;
        
        this.wm = WorldMap.getInstance();
        
    }
    
    public Phantom(ImageIcon image, int value) {
        super(image);
        
        this.setScore(value);
        state = State.DEADLY;
        
        this.wm = WorldMap.getInstance();
        
    }
    
    public void setState(State state) {
        this.state = state;
    }
    
    public State state() {
        return state;
    }
    
    abstract public String name();
    
    abstract protected void navigation();

    @Override
    public void autoDraw(Graphics g) {
        Drawing.draw(g, this.imageIcon, pos.getX(), pos.getY());
    }
    
    public void setNextMovDirection(int direction) {
        this.nextMovDirection = direction;
    }
    
    public void move() {
       // System.out.println("Next move direction: "+this.nextMovDirection);
       // System.out.println("Move direction: "+this.movDirection);
        System.out.println("Phantom: "+this.pos.toString());
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
                this.moveLeft();
                break;
            case MOVE_RIGHT:
                this.moveRight();
                break;
            case MOVE_UP:
                this.moveUp();
                break;
            case MOVE_DOWN:
                this.moveDown();
                break;
            default:
                break;
        }        
        
    }
}
