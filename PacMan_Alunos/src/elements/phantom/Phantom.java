package elements.phantom;

import control.WorldMap;
import elements.Element;
import java.awt.Graphics;
import java.io.Serializable;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.ImageIcon;
import utils.Consts;
import utils.Drawing;
import utils.ImageCollection;

public abstract class Phantom extends Element implements Serializable {
    public static enum State {
        DEADLY,
        EDIBLE,
        ENDING_EDIBLE
    }
    
    public static final int STOP = 0;
    public static final int MOVE_LEFT = 1;
    public static final int MOVE_RIGHT = 2;
    public static final int MOVE_UP = 3;
    public static final int MOVE_DOWN = 4;
        
    protected static int phantomCounter = 0;
    public static void increasePhantonCounter() {
        phantomCounter++;
    }
    
    @Override
    public int getScore() {
        return (int) (this.score * Math.pow(2, phantomCounter));
    }
    
    private int nextMovDirection = MOVE_LEFT;
    private int movDirection = MOVE_LEFT;
    transient protected State state;
    
    transient private Timer edibleTimer;
    
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
    
    public State getState() {
        return state;
    }
    
    public void setState(State state) {
        if(null != state)
            switch (state) {
            case DEADLY:
                this.isTransposable = true;
                break;
            case EDIBLE:
                this.isTransposable = true;
                if(this.state == State.DEADLY) {
                    phantomCounter = 0;
                    
                    edibleTimer = new Timer();
                    
                    edibleTimer.schedule(new TimerTask() {
                        int i = 0;
                        @Override
                        public void run() {
                            if(i++ == 0) {
                                setState(State.ENDING_EDIBLE);
                            } else {
                                setState(State.DEADLY);
                                edibleTimer.cancel();
                            }
                        }
                        
                    }, 5000, 3000);
                }   break;
            case ENDING_EDIBLE:
                this.isTransposable = true;
                break;
            default:
                break;
        }

        this.state = state;
    }
    
    public State state() {
        return state;
    }
    
    abstract public String name();
    
    abstract protected void navigation();

    abstract protected ImageIcon getImage(int movDirection);
    
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
