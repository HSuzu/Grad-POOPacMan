/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package elements.phantom;

import control.WorldMap;
import static elements.phantom.Phantom.wm;
import javax.swing.ImageIcon;
import utils.Consts;
import utils.ImageCollection;
import utils.Position;

/**
 *
 * @author aribeiro
 */
public class Clyde extends Phantom {
    private float hysteresisCoef;
    private int counterStep;
    private byte path;
    private byte oldDirection;
    
    public Clyde(String imageName, int value) {
        super(imageName, value);
        hysteresisCoef = 1.0f;
        counterStep = 0;
        path = 0;
        oldDirection = 0;
    }

    public Clyde(ImageCollection collection, int defaultImage, int value) {
        super(collection, defaultImage, value);
        hysteresisCoef = 1.0f;
        counterStep = 0;
        path = 0;
        oldDirection = 0;
    }
    
    @Override
    public String name() {
        return "Clyde";
    }

    private void followPos(Position desiredPos) {
       
       if(this.pos == desiredPos) {
           return;
       }
       
       path = wm.freePath((int)Math.round(this.pos.getX()), (int)Math.round(this.pos.getY()));
       
       
       //Tomada de decisão:
       if(Math.random()*100 <= 98.0f*hysteresisCoef) {
            hysteresisCoef = 1.0f;
            if(desiredPos.getX() > this.pos.getX() && ((path & WorldMap.RIGHT) == WorldMap.RIGHT)) {
               this.setNextMovDirection(MOVE_RIGHT);
            }
            else if(desiredPos.getX() < this.pos.getX() && ((path & WorldMap.LEFT) == WorldMap.LEFT)) {
                this.setNextMovDirection(MOVE_LEFT);
            }
            else if(desiredPos.getY() > this.pos.getY() && ((path & WorldMap.DOWN) == WorldMap.DOWN)) {
                this.setNextMovDirection(MOVE_DOWN);
            }
            else if(desiredPos.getY() < this.pos.getY() && ((path & WorldMap.UP) == WorldMap.UP)) {
                this.setNextMovDirection(MOVE_UP);
            }
       }
       //Aleatório:
       else {
            byte newPath = wm.freePath((int)Math.round(this.pos.getX()), (int)Math.round(this.pos.getY()));
            if(path != newPath) {
                path = newPath;
            }
            oldDirection = (byte) ((byte)(Math.random()*16) & path);
            while(oldDirection == 0) {
                oldDirection = (byte) ((byte)(16*Math.random()) & path);
            }
            
            if((oldDirection & WorldMap.DOWN) != 0) {
                this.setNextMovDirection(MOVE_DOWN);
            } else if((oldDirection & WorldMap.LEFT) != 0) {
                this.setNextMovDirection(MOVE_LEFT);
            } else if((oldDirection & WorldMap.RIGHT) != 0) {
                this.setNextMovDirection(MOVE_RIGHT);                
            } else if((oldDirection & WorldMap.UP) != 0) {
                this.setNextMovDirection(MOVE_UP);  
            }
       }
    }
 
    @Override
    protected void navigation() {
        Position desiredPos = wm.getPacManPosition();
        if(this.pos.getDistanceTo(desiredPos) > 8.0d) {
            followPos(desiredPos);
        }
        else {
            byte newPath = wm.freePath((int)Math.round(this.pos.getX()), (int)Math.round(this.pos.getY()));
            if(path != newPath) {
                path = newPath;
                oldDirection = (byte) ((byte)(16*Math.random()) & path);
                while(oldDirection == 0) {
                    oldDirection = (byte) ((byte)(16*Math.random()) & path);
                }
            }
            if((oldDirection & WorldMap.DOWN) != 0) {
                this.setNextMovDirection(MOVE_DOWN);
                
            } else if((oldDirection & WorldMap.LEFT) != 0) {
                this.setNextMovDirection(MOVE_LEFT);
                
            } else if((oldDirection & WorldMap.RIGHT) != 0) {
                this.setNextMovDirection(MOVE_RIGHT);
                
            } else if((oldDirection & WorldMap.UP) != 0) {
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
                return this.collection.getImage(Consts.Animation.CLYDE_LEFT);
            case Phantom.MOVE_RIGHT:
                return this.collection.getImage(Consts.Animation.CLYDE_RIGHT);
            case Phantom.MOVE_UP:
                return this.collection.getImage(Consts.Animation.CLYDE_UP);
            case Phantom.MOVE_DOWN:
                return this.collection.getImage(Consts.Animation.CLYDE_DOWN);
        }
        return this.collection.getImage(Consts.Animation.CLYDE_UP);
    }
}
