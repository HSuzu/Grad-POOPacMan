/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package elements;

import control.WorldMap;
import elements.phanton.Phantom;
import static elements.phanton.Phantom.MOVE_DOWN;
import static elements.phanton.Phantom.MOVE_LEFT;
import static elements.phanton.Phantom.MOVE_RIGHT;
import static elements.phanton.Phantom.MOVE_UP;
import javax.swing.ImageIcon;
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
    
    public Inky(ImageIcon image, int value) {
        super(image, value);
        hysteresisCoef = 1.0f;
    }
    @Override
    public String name() {
        return "Inky";
    }

    private void followPos(Position desiredPos) {

        
       System.out.println("DesiredPos: "+desiredPos.toString());
       
       if(this.pos == desiredPos) {
           System.out.println("Peguei");
           return;
       }
       
       freeSides = wm.freePath((int)Math.round(this.pos.getX()), (int)Math.round(this.pos.getY()));
       
       //Tomada de decisão:
       if(Math.random()*100 <= 98.0f*hysteresisCoef) {
            hysteresisCoef = 1.0f;
            if(desiredPos.getX() > this.pos.getX() && ((freeSides & WorldMap.RIGHT) == WorldMap.RIGHT)) {
               this.setNextMovDirection(MOVE_RIGHT);
               System.out.println("r");
            }
            else if(desiredPos.getX() < this.pos.getX() && ((freeSides & WorldMap.LEFT) == WorldMap.LEFT)) {
                this.setNextMovDirection(MOVE_LEFT);
                System.out.println("l");
            }
            else if(desiredPos.getY() > this.pos.getY() && ((freeSides & WorldMap.DOWN) == WorldMap.DOWN)) {
                this.setNextMovDirection(MOVE_DOWN);
                System.out.println("d");
            }
            else if(desiredPos.getY() < this.pos.getY() && ((freeSides & WorldMap.UP) == WorldMap.UP)) {
                this.setNextMovDirection(MOVE_UP);
                System.out.println("u");
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
                   System.out.println("D");
                   break;
               case WorldMap.LEFT:
                   this.setNextMovDirection(MOVE_LEFT);
                   System.out.println("L");
                   break;
               case WorldMap.RIGHT:
                   this.setNextMovDirection(MOVE_RIGHT);
                   System.out.println("R");
                   break;
               case WorldMap.UP:
                   this.setNextMovDirection(MOVE_UP);
                   System.out.println("U");
                   break;
               default:
                   this.setNextMovDirection(MOVE_UP);
                   System.out.println("ELSE");
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
                   System.out.println("D");
                   break;
               case WorldMap.LEFT:
                   this.setNextMovDirection(MOVE_LEFT);
                   System.out.println("L");
                   break;
               case WorldMap.RIGHT:
                   this.setNextMovDirection(MOVE_RIGHT);
                   System.out.println("R");
                   break;
               case WorldMap.UP:
                   this.setNextMovDirection(MOVE_UP);
                   System.out.println("U");
                   break;
               default:
                   this.setNextMovDirection(MOVE_UP);
                   System.out.println("ELSE");
                   break;
            }
        }
    }
}
