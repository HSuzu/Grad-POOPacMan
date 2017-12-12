/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package elements.phantom;

import control.WorldMap;
import static elements.phantom.Phantom.MOVE_DOWN;
import static elements.phantom.Phantom.MOVE_LEFT;
import static elements.phantom.Phantom.MOVE_RIGHT;
import static elements.phantom.Phantom.MOVE_UP;
import javax.swing.ImageIcon;
import utils.Consts;
import utils.ImageCollection;
import utils.Position;

/**
 *
 * @author aribeiro
 */
public class Inky extends Phantom {
    private float hysteresisCoef;
    private byte freeSides;
    private int direction;
    
    public Inky(String imageName, int value) {
        super(imageName, value);
        hysteresisCoef = 1.0f;
    }
    
    public Inky(ImageCollection collection, int defaultImage, int value) {
        super(collection, defaultImage, value);
        hysteresisCoef = 1.0f;
    }
    
    @Override
    public String name() {
        return "Inky";
    }

    private void followPos(Position desiredPos) {
       
       if(this.pos == desiredPos) {
           return;
       }
       
       freeSides = wm.freePath((int)Math.round(this.pos.getX()), (int)Math.round(this.pos.getY()));
       
       //Tomada de decisão:
       if(Math.random()*100 <= 98.0f*hysteresisCoef) {
            hysteresisCoef = 1.0f;
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
       }
       //Aleatório:
       else {
           double aux = (this.pos.getX()*desiredPos.getY())%3;
           byte possibleDirection = (byte)Math.pow(2.0d, aux);
           hysteresisCoef = 0.20f;
           
           switch (possibleDirection & freeSides) {
               case WorldMap.DOWN:
                   this.setNextMovDirection(MOVE_DOWN);
                   break;
               case WorldMap.LEFT:
                   this.setNextMovDirection(MOVE_LEFT);
                   break;
               case WorldMap.RIGHT:
                   this.setNextMovDirection(MOVE_RIGHT);
                   break;
               case WorldMap.UP:
                   this.setNextMovDirection(MOVE_UP);
                   break;
               default:
                   this.setNextMovDirection(MOVE_UP);
                   break;
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
            if(freeSides != newPath) {
                freeSides = newPath;
                double aux = (Math.random()*100)%3;
                direction = (byte)Math.pow(2.0d, aux) & freeSides;

            }
            switch(direction) {
                case WorldMap.DOWN:
                   this.setNextMovDirection(MOVE_DOWN);
                   break;
               case WorldMap.LEFT:
                   this.setNextMovDirection(MOVE_LEFT);
                   break;
               case WorldMap.RIGHT:
                   this.setNextMovDirection(MOVE_RIGHT);
                   break;
               case WorldMap.UP:
                   this.setNextMovDirection(MOVE_UP);
                   break;
               default:
                   this.setNextMovDirection(MOVE_UP);
                   break;
            }
        }
    }

    @Override
    protected ImageIcon getImage(int movDirection) {
        switch(movDirection) {
            case Phantom.MOVE_LEFT:
                return this.collection.getImage(Consts.Animation.INKY_LEFT);
            case Phantom.MOVE_RIGHT:
                return this.collection.getImage(Consts.Animation.INKY_RIGHT);
            case Phantom.MOVE_UP:
                return this.collection.getImage(Consts.Animation.INKY_UP);
            case Phantom.MOVE_DOWN:
                return this.collection.getImage(Consts.Animation.INKY_DOWN);
        }
        return this.collection.getImage(Consts.Animation.INKY_UP);
    }
}