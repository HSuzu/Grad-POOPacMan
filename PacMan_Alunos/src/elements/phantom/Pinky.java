/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package elements.phantom;

import control.WorldMap;
import elements.PacMan;
import javax.swing.ImageIcon;
import utils.Consts;
import utils.ImageCollection;
import utils.Position;

public class Pinky extends Phantom {
    private float hysteresisCoef;
    
    public Pinky(String imageName, int value) {
        super(imageName, value);
        hysteresisCoef = 1.0f;
    }
    
    public Pinky(ImageCollection collection, int defaultImage, int value) {
        super(collection, defaultImage, value);
        hysteresisCoef = 1.0f;
    }
    
    @Override
    public String name() {
        return "Pinky";
    }

    @Override
    protected void navigation() {
        int desiredDirection = wm.getPacManDirection();
        
        byte direction = wm.freePath((int)Math.round(this.pos.getX()), (int)Math.round(this.pos.getY()));
        
        //Tomada de decisão:
        if(/*Math.random()*100 <= 98.0f*hysteresisCoef*/true) {
            hysteresisCoef = 1.0f;
            if(desiredDirection == PacMan.MOVE_LEFT && (direction & WorldMap.LEFT) == WorldMap.LEFT) {
                this.setNextMovDirection(MOVE_LEFT);
            } else if(desiredDirection == PacMan.MOVE_DOWN && (direction & WorldMap.DOWN) == WorldMap.DOWN) {
                this.setNextMovDirection(MOVE_DOWN);
            } else if(desiredDirection == PacMan.MOVE_UP && (direction & WorldMap.UP) == WorldMap.UP) {
                this.setNextMovDirection(MOVE_UP);
            } else if(desiredDirection == PacMan.MOVE_RIGHT && (direction & WorldMap.RIGHT) == WorldMap.RIGHT) {
                this.setNextMovDirection(MOVE_RIGHT);
            } else {
                Position desiredPos = wm.getPacManPosition();
                if(desiredPos.getX() > this.pos.getX() && ((direction & WorldMap.RIGHT) == WorldMap.RIGHT)) {
                    this.setNextMovDirection(MOVE_RIGHT);
                }
                else if(desiredPos.getX() < this.pos.getX() && ((direction & WorldMap.LEFT) == WorldMap.LEFT)) {
                    this.setNextMovDirection(MOVE_LEFT);
                }
                else if(desiredPos.getY() > this.pos.getY() && ((direction & WorldMap.DOWN) == WorldMap.DOWN)) {
                    this.setNextMovDirection(MOVE_DOWN);
                }
                else if(desiredPos.getY() < this.pos.getY() && ((direction & WorldMap.UP) == WorldMap.UP)) {
                    this.setNextMovDirection(MOVE_UP);
                }
            }
        }
        else {
            hysteresisCoef = 0.2f;
            byte newPath = wm.freePath((int)Math.round(this.pos.getX()), (int)Math.round(this.pos.getY()));

            direction = (byte) ((byte)(Math.random()*16) & newPath);
            while(direction == 0) {
                direction = (byte) ((byte)(16*Math.random()) & newPath);
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
    protected ImageIcon getImage(int movDirection) {
        if(this.state == State.EDIBLE) {
            return collection.getImage(Consts.Animation.EDIBLE);
        } else if(this.state == State.ENDING_EDIBLE) {
            return collection.getImage(Consts.Animation.EDIBLE_ENDING);
        }

        switch(movDirection) {
            case Phantom.MOVE_LEFT:
                return this.collection.getImage(Consts.Animation.PINKY_LEFT);
            case Phantom.MOVE_RIGHT:
                return this.collection.getImage(Consts.Animation.PINKY_RIGHT);
            case Phantom.MOVE_UP:
                return this.collection.getImage(Consts.Animation.PINKY_UP);
            case Phantom.MOVE_DOWN:
                return this.collection.getImage(Consts.Animation.PINKY_DOWN);
        }
        return this.collection.getImage(Consts.Animation.PINKY_UP);

    }
}
