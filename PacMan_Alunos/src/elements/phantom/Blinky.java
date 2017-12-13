package elements.phantom;

import control.WorldMap;
import java.io.Serializable;
import utils.Position;
import javax.swing.ImageIcon;
import utils.Consts;
import utils.ImageCollection;

public class Blinky extends Phantom implements Serializable {
    private float hysteresisCoef;
    private int counterStep;
    private byte path;
    private byte oldDirection;
    
    public Blinky(String imageName, int value) {
        super(imageName, value);
        hysteresisCoef = 1.0f;
        counterStep = 0;
        path = 0;
        oldDirection = 0;
    }
    
    public Blinky(ImageCollection collection, int defaultImage, int value) {
        super(collection, defaultImage, value);
        hysteresisCoef = 1.0f;
        counterStep = 0;
        path = 0;
        oldDirection = 0;
    }
    
    @Override
    public String name() {
        return "Blinky";
    }
    
    @Override
    public void navigation() {
       Position desiredPos = wm.getPacManPosition();
        
       if(this.pos == desiredPos) {
           return;
       }
       
        byte freeSides = wm.freePath((int)Math.round(this.pos.getX()), (int)Math.round(this.pos.getY()));
       
       
       //Tomada de decisão:
       if(Math.random()*100 <= 98.0f*hysteresisCoef) {
            hysteresisCoef = 1.0f;
            counterStep = 0;
            if(desiredPos.getX() > this.pos.getX() && ((freeSides & WorldMap.RIGHT) == WorldMap.RIGHT)) {
               this.setNextMovDirection(MOVE_RIGHT);
            }
            else if(desiredPos.getX() < this.pos.getX() && ((freeSides & WorldMap.LEFT) == WorldMap.LEFT)) {
                this.setNextMovDirection(MOVE_LEFT);
            }
            else if(desiredPos.getY() > this.pos.getY() && ((freeSides & WorldMap.DOWN) == WorldMap.DOWN)) {
                this.setNextMovDirection(MOVE_DOWN);
            }
            else if(desiredPos.getY() < this.pos.getY() && ((freeSides & WorldMap.UP) == WorldMap.UP)) {
                this.setNextMovDirection(MOVE_UP);
            }
            path = freeSides;
       }
       //Aleatório:
       else {
            byte direction;
            if(path == freeSides && counterStep < Consts.PHANTOMS_NUM_STEPS) {
                counterStep++;
                direction = oldDirection;
            } else if(path != freeSides) {
                path = freeSides;
                if(oldDirection == (oldDirection & freeSides)) {
                    direction = oldDirection;
                }
                else {
                    direction = (byte) ((byte)(Math.random()*16) & path);
                    while(direction == 0) {
                        direction = (byte) ((byte)(16*Math.random()) & path);
                    }
                    oldDirection = direction;
                    counterStep = 0;
                }
                
            } else {
                counterStep = 0;
                direction = (byte) ((byte)(Math.random()*16) & path);
                while(direction == 0) {
                    direction = (byte) ((byte)(16*Math.random()) & path);
                }
                oldDirection = direction;
            }
    
            if((direction & WorldMap.DOWN) != 0) {
                this.setNextMovDirection(MOVE_DOWN);
            } else if((direction & WorldMap.LEFT) != 0) {
                this.setNextMovDirection(MOVE_LEFT);
            } else if((direction & WorldMap.RIGHT) != 0) {
                this.setNextMovDirection(MOVE_RIGHT);                
            } else if((direction & WorldMap.UP) != 0) {
                this.setNextMovDirection(MOVE_UP);  
            }
       }
       
    }

    @Override
    public ImageIcon getImage(int movDirection) {
        if(this.state == State.EDIBLE) {
            return collection.getImage(Consts.Animation.EDIBLE);
        } else if(this.state == State.ENDING_EDIBLE) {
            return collection.getImage(Consts.Animation.EDIBLE_ENDING);
        }

        switch(movDirection) {
            case Phantom.MOVE_LEFT:
                return this.collection.getImage(Consts.Animation.BLINKY_LEFT);
            case Phantom.MOVE_RIGHT:
                return this.collection.getImage(Consts.Animation.BLINKY_RIGHT);
            case Phantom.MOVE_UP:
                return this.collection.getImage(Consts.Animation.BLINKY_UP);
            case Phantom.MOVE_DOWN:
                return this.collection.getImage(Consts.Animation.BLINKY_DOWN);
        }
        return this.collection.getImage(Consts.Animation.BLINKY_UP);
    }
}
