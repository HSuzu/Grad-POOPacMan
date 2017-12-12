/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package elements.phanton;

import control.WorldMap;
import elements.PacMan;
import java.util.ArrayList;
import utils.Position;
import javax.swing.ImageIcon;
import utils.Consts;

public class Pinky extends Phantom {
    private float hysteresisCoef;
    
    public Pinky(String imageName, int value) {
        super(imageName, value);
        hysteresisCoef = 1.0f;
    }
    
    public Pinky(ImageIcon image, int value) {
        super(image, value);
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
        if(Math.random()*100 <= 98.0f*hysteresisCoef) {
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

            }
        }
        else {
            hysteresisCoef = 0.2f;
            if(desiredDirection == PacMan.MOVE_LEFT || desiredDirection == PacMan.MOVE_RIGHT) {
                if((direction & WorldMap.UP) == WorldMap.UP && (direction & WorldMap.DOWN) == WorldMap.DOWN) {
                    if(Math.random()*100 > 50) {
                        this.setNextMovDirection(MOVE_UP);
                    }
                    else {
                        this.setNextMovDirection(MOVE_DOWN);
                    }
                } else if((direction & WorldMap.UP) == WorldMap.UP) {
                    this.setNextMovDirection(MOVE_UP);
                } else if((direction & WorldMap.DOWN) == WorldMap.DOWN) {
                    this.setNextMovDirection(MOVE_DOWN);
                } else if((direction & WorldMap.LEFT) == WorldMap.LEFT) {
                    this.setNextMovDirection(MOVE_LEFT);
                } else {
                    this.setNextMovDirection(MOVE_RIGHT);
                }
            } else {
                if((direction & WorldMap.LEFT) == WorldMap.LEFT && (direction & WorldMap.RIGHT) == WorldMap.RIGHT) {
                    if(Math.random()*100 > 50) {
                        this.setNextMovDirection(MOVE_LEFT);
                    }
                    else {
                        this.setNextMovDirection(MOVE_RIGHT);
                    }
                } else if((direction & WorldMap.LEFT) == WorldMap.LEFT) {
                    this.setNextMovDirection(MOVE_LEFT);
                } else if((direction & WorldMap.RIGHT) == WorldMap.RIGHT) {
                    this.setNextMovDirection(MOVE_RIGHT);
                } else if((direction & WorldMap.UP) == WorldMap.UP) {
                    this.setNextMovDirection(MOVE_UP);
                } else {
                    this.setNextMovDirection(MOVE_DOWN);
                }
            }
        }
    }
    
}
