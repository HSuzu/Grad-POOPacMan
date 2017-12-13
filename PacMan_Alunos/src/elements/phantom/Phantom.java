package elements.phantom;

import control.WorldMap;
import elements.Element;
import elements.PacMan;
import java.awt.Graphics;
import java.io.Serializable;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.ImageIcon;
import utils.Consts;
import utils.Drawing;
import utils.ImageCollection;
import utils.Position;

public abstract class Phantom extends Element implements Serializable {
    public static enum State {
        DEADLY,
        EDIBLE,
        ENDING_EDIBLE,
        EYE
    }
    
    public static final int STOP = 0;
    public static final int MOVE_LEFT = 1;
    public static final int MOVE_RIGHT = 2;
    public static final int MOVE_UP = 3;
    public static final int MOVE_DOWN = 4;
    
    private static final Position defaultPosition = new Position(Consts.MID_FIELD_X, Consts.MID_FIELD_Y);
        
    protected static int phantomCounter = 0;
    
    private byte counterStep;
    private byte path;
    private byte oldDirection;
    
    public static void increasePhantonCounter() {
        phantomCounter++;
    }
    
    @Override
    public int getScore() {
        return (int) (this.score * Math.pow(2, phantomCounter));
    }
    
    private int nextMovDirection = MOVE_LEFT;
    private int movDirection = MOVE_LEFT;
    private int forbiddenDirection;
    transient protected State state;
        
    transient private Timer edibleTimer = null;
    
    protected static WorldMap wm;
    
    public Phantom(String imageName, int value) {
        super(imageName);
                
        this.isTransposable = false;
        this.score = value;
        state = State.DEADLY;
        
        setPosition(Consts.MID_FIELD_X, Consts.MID_FIELD_Y);
        this.forbiddenDirection = STOP;
        
        Phantom.wm = WorldMap.getInstance();
    }
    
    public Phantom(ImageCollection collection, int defaultImage, int value) {
        super(collection, defaultImage);
        
        this.score = value;
        state = State.DEADLY;
        
        setPosition(Consts.MID_FIELD_X, Consts.MID_FIELD_Y);
        this.forbiddenDirection = STOP;
        
        Phantom.wm = WorldMap.getInstance();
    }
    
    public State getState() {
        return state;
    }
    
    public int getMovDirection() {
        return movDirection;
    }
    
    public void setForbiddenDirection(int direction) {
        forbiddenDirection = direction;
    }
    
    public void reset() {
        if(edibleTimer != null) {
            edibleTimer.cancel();
        }
    }
    
    public void backToLastPosition() {
        pos.comeBack();
    }
    
    public void setState(State state) {
        if(null != state)
            switch (state) {
            case DEADLY:
                this.isMortal = false;
                this.isTransposable = true;
                break;
            case EDIBLE:
                this.isMortal = true;
                this.isTransposable = true;
                if(this.state != State.DEADLY) {
                    if(edibleTimer != null) {
                        edibleTimer.cancel();
                        edibleTimer = null;
                    }
                } else {
                    phantomCounter = 0;
                }

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
                            edibleTimer = null;
                        }
                    }

                }, Consts.Timer.POWERPELLET_EFFECT, Consts.Timer.POWERPELLET_EFFECT_ENDIND);
                break;
            case ENDING_EDIBLE:
                this.isMortal = true;
                this.isTransposable = true;
                break;
            case EYE:
                edibleTimer.cancel();;
                edibleTimer = null;
                this.isMortal = false;
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

    abstract public ImageIcon getImage(int movDirection);
    
    protected void runAway() {
        int desiredDirection = wm.getPacManDirection();
        
        byte direction = wm.freePath((int)Math.round(this.pos.getX()), (int)Math.round(this.pos.getY())); 
        if((direction & WorldMap.RIGHT) == WorldMap.RIGHT && (direction & WorldMap.LEFT) == WorldMap.LEFT && this.getCounterStep() < 0.8*Consts.PHANTOMS_NUM_STEPS && this.forbiddenDirection != MOVE_RIGHT && this.forbiddenDirection != MOVE_LEFT) {
            if(this.getOldDirection() == WorldMap.RIGHT) {
                this.setNextMovDirection(MOVE_LEFT);
                this.setForbiddenDirection(MOVE_RIGHT);
                this.increaseCounterStep();
            }
            else {
                this.setNextMovDirection(MOVE_RIGHT);
                this.setForbiddenDirection(MOVE_LEFT);
                this.increaseCounterStep();
            }
        }
        else {        
            if(this.getPath() == direction && this.getCounterStep() < 0.8*Consts.PHANTOMS_NUM_STEPS) {
                this.increaseCounterStep();
                direction = this.getOldDirection();
            } else if(this.getPath() != direction) {
                this.setPath(direction);
                if(this.getOldDirection() == (this.getOldDirection() & direction)) {
                    direction = this.getOldDirection();
                } else {
                    this.setOldDirection(direction);
                    this.resetCounterStep();
                }
            } else {
                this.setOldDirection(direction);
                this.resetCounterStep();
            }

            Position desiredPos = wm.getPacManPosition();
            if(desiredPos.getX() < this.pos.getX() && ((direction & WorldMap.RIGHT) == WorldMap.RIGHT) && this.forbiddenDirection != MOVE_RIGHT) {
                this.setNextMovDirection(MOVE_RIGHT);
                this.setForbiddenDirection(MOVE_LEFT);
            }
            else if(desiredPos.getY() < this.pos.getY() && ((direction & WorldMap.DOWN) == WorldMap.DOWN) && this.forbiddenDirection != MOVE_DOWN) {
                this.setNextMovDirection(MOVE_DOWN);
                this.setForbiddenDirection(MOVE_UP);
            }
            else if(desiredPos.getX() > this.pos.getX() && ((direction & WorldMap.LEFT) == WorldMap.LEFT) && this.forbiddenDirection != MOVE_LEFT) {
                this.setNextMovDirection(MOVE_LEFT);
                this.setForbiddenDirection(MOVE_RIGHT);
            }
            else if(desiredPos.getY() > this.pos.getY() && ((direction & WorldMap.UP) == WorldMap.UP) && this.forbiddenDirection != MOVE_UP) {
                this.setNextMovDirection(MOVE_UP);
                this.setForbiddenDirection(MOVE_DOWN);
            } else {
                if(desiredDirection == PacMan.MOVE_LEFT && (direction & WorldMap.LEFT) == WorldMap.LEFT) {
                    this.setNextMovDirection(MOVE_LEFT);
                    this.setForbiddenDirection(MOVE_RIGHT);
                } else if(desiredDirection == PacMan.MOVE_DOWN && (direction & WorldMap.DOWN) == WorldMap.DOWN) {
                    this.setNextMovDirection(MOVE_DOWN);
                    this.setForbiddenDirection(MOVE_UP);
                } else if(desiredDirection == PacMan.MOVE_UP && (direction & WorldMap.UP) == WorldMap.UP) {
                    this.setNextMovDirection(MOVE_UP);
                    this.setForbiddenDirection(MOVE_DOWN);
                } else if(desiredDirection == PacMan.MOVE_RIGHT && (direction & WorldMap.RIGHT) == WorldMap.RIGHT) {
                    this.setNextMovDirection(MOVE_RIGHT);
                    this.setForbiddenDirection(MOVE_LEFT);
                } else {
                    if((direction & WorldMap.LEFT) == WorldMap.LEFT && this.forbiddenDirection != MOVE_LEFT) {
                        this.setNextMovDirection(MOVE_LEFT);
                        this.setForbiddenDirection(MOVE_RIGHT);
                    } else if((direction & WorldMap.DOWN) == WorldMap.DOWN && this.forbiddenDirection != MOVE_DOWN) {
                        this.setNextMovDirection(MOVE_DOWN);
                        this.setForbiddenDirection(MOVE_UP);
                    } else if((direction & WorldMap.UP) == WorldMap.UP && this.forbiddenDirection != MOVE_UP) {
                        this.setNextMovDirection(MOVE_UP);
                        this.setForbiddenDirection(MOVE_DOWN);
                    } else {
                        this.setNextMovDirection(MOVE_RIGHT);
                        this.setForbiddenDirection(MOVE_LEFT);
                    }
                }
            }

        }
    }

    public void increaseCounterStep() {
        counterStep++;
    }
    
    public void resetCounterStep() {
        counterStep = 0;
    }
    
    public byte getCounterStep() {
        return counterStep;
    }
    
    public void setPath(byte path) {
        this.path = path;
    }
    
    public byte getPath() {
        return path;
    }
    
    public void setOldDirection(byte value) {
        this.oldDirection = value;
    }
    
    public byte getOldDirection() {
        return oldDirection;
    }
    
    public void randomMove() {
        byte direction;
        byte freeSides = wm.freePath((int)Math.round(this.pos.getX()), (int)Math.round(this.pos.getY()));
        if(this.getPath() == freeSides && this.getCounterStep() < Consts.PHANTOMS_NUM_STEPS) {
            this.increaseCounterStep();
            direction = this.getOldDirection();
        } else if(this.getPath() != freeSides) {
            this.setPath(freeSides);
            if(this.getOldDirection() == (this.getOldDirection() & freeSides)) {
                direction = this.getOldDirection();
            }
            else {
                direction = (byte) ((byte)(Math.random()*16) & this.getPath());
                while(direction == 0) {
                    direction = (byte) ((byte)(16*Math.random()) & this.getPath());
                }
                this.setOldDirection(direction);
                this.resetCounterStep();
            }

        } else {
            this.resetCounterStep();;
            direction = (byte) ((byte)(Math.random()*16) & this.getPath());
            while(direction == 0) {
                direction = (byte) ((byte)(16*Math.random()) & this.getPath());
            }
            this.setOldDirection(direction);
        }

        if((direction & WorldMap.DOWN) != 0 && this.forbiddenDirection != MOVE_DOWN) {
            this.setNextMovDirection(MOVE_DOWN);
            this.setForbiddenDirection(MOVE_UP);
        } else if((direction & WorldMap.LEFT) != 0 && this.forbiddenDirection != MOVE_LEFT) {
            this.setNextMovDirection(MOVE_LEFT);
            this.setForbiddenDirection(MOVE_RIGHT);
        } else if((direction & WorldMap.RIGHT) != 0 && this.forbiddenDirection != MOVE_RIGHT) {
            this.setNextMovDirection(MOVE_RIGHT);                
            this.setForbiddenDirection(MOVE_LEFT);
        } else if((direction & WorldMap.UP) != 0 && this.forbiddenDirection != MOVE_UP) {
            this.setNextMovDirection(MOVE_UP);  
            this.setForbiddenDirection(MOVE_DOWN);
        }
    }
    
    public void followPos(Position desiredPos) {
       if(this.pos == desiredPos) {
           return;
       }
       path = wm.freePath((int)Math.round(pos.getX()), (int)Math.round(pos.getY()));
       
       if(desiredPos.getX() > this.pos.getX() && ((path & WorldMap.RIGHT) == WorldMap.RIGHT) && this.forbiddenDirection != MOVE_RIGHT) {
            this.setNextMovDirection(MOVE_RIGHT);
            this.forbiddenDirection = MOVE_LEFT;
       }
       else if(desiredPos.getX() < this.pos.getX() && ((path & WorldMap.LEFT) == WorldMap.LEFT) && this.forbiddenDirection != MOVE_LEFT) {
            this.setNextMovDirection(MOVE_LEFT);
            this.forbiddenDirection = MOVE_RIGHT;
       }
       else if(desiredPos.getY() > this.pos.getY() && ((path & WorldMap.DOWN) == WorldMap.DOWN) && this.forbiddenDirection != MOVE_DOWN) {
            this.setNextMovDirection(MOVE_DOWN);
            this.forbiddenDirection = MOVE_UP;
       }
       else if(desiredPos.getY() < this.pos.getY() && ((path & WorldMap.UP) == WorldMap.UP) && this.forbiddenDirection != MOVE_UP) {
            this.setNextMovDirection(MOVE_UP);
            this.forbiddenDirection = MOVE_DOWN;
       }
       else if(((path & WorldMap.RIGHT) == WorldMap.RIGHT) && this.forbiddenDirection != MOVE_RIGHT) {
            this.setNextMovDirection(MOVE_RIGHT);
            this.forbiddenDirection = MOVE_LEFT;
       }
       else if(((path & WorldMap.LEFT) == WorldMap.LEFT) && this.forbiddenDirection != MOVE_LEFT) {
            this.setNextMovDirection(MOVE_LEFT);
            this.forbiddenDirection = MOVE_RIGHT;           
       }
       else if(((path & WorldMap.DOWN) == WorldMap.DOWN) && this.forbiddenDirection != MOVE_DOWN) {
            this.setNextMovDirection(MOVE_DOWN);
            this.forbiddenDirection = MOVE_UP;           
       }
       else if(((path & WorldMap.UP) == WorldMap.UP) && this.forbiddenDirection != MOVE_UP) {
            this.setNextMovDirection(MOVE_UP);
            this.forbiddenDirection = MOVE_DOWN;           
       }
    }
    
    public void goToPos(Position desiredPos) {
       if(this.pos == desiredPos) {
           return;
       }  
      
       int posx = (int)Math.round(this.pos.getX());
       int posy = (int)Math.round(this.pos.getY());
       
       int desiredX = (int)Math.round(desiredPos.getX());
       int desiredY = (int)Math.round(desiredPos.getY());
       
       if(desiredX >= posx && this.forbiddenDirection != MOVE_RIGHT) {
            this.setNextMovDirection(MOVE_RIGHT);
            this.forbiddenDirection = MOVE_LEFT;
       }
       else if(desiredY >= posy && this.forbiddenDirection != MOVE_DOWN) {
            this.setNextMovDirection(MOVE_DOWN);
            this.forbiddenDirection = MOVE_UP;
       }
       else if(desiredX < posx && this.forbiddenDirection != MOVE_LEFT) {
            this.setNextMovDirection(MOVE_LEFT);
            this.forbiddenDirection = MOVE_RIGHT;
       }
       else if(desiredY < posy && this.forbiddenDirection != MOVE_UP) {
            this.setNextMovDirection(MOVE_UP);   
            this.forbiddenDirection = MOVE_DOWN;
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
        if(state == State.EYE) {
            if(pos.isNear(defaultPosition, 1.5) && wm.isValidPosition(wm.getMap()[(int)pos.getX()][(int)pos.getY()])) {
                state = State.DEADLY;
            }
        }
        
        if(movDirection == nextMovDirection) {
            switch(state) {
                case DEADLY:
                    navigation();
                break;
                case EYE:
                    goToPos(defaultPosition);
                break;
                case EDIBLE:
                case ENDING_EDIBLE:
                    runAway();
                break;
            }
        } else {
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
