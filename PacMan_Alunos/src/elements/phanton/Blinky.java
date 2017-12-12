package elements.phanton;

import control.WorldMap;
import java.io.Serializable;
import java.util.ArrayList;
import utils.Position;
import javax.swing.ImageIcon;
import utils.Consts;
import utils.ImageCollection;

public class Blinky extends Phantom implements Serializable {
    private float hysteresisCoef;
    
    public Blinky(String imageName, int value) {
        super(imageName, value);
        hysteresisCoef = 1.0f;
    }
    
    public Blinky(ImageCollection collection, int defaultImage, int value) {
        super(collection, defaultImage, value);
        hysteresisCoef = 1.0f;
    }
    
    @Override
    public String name() {
        return "Blinky";
    }
    
    protected void navigationAlgorithm(Position bPos, char map[][]) {
        //Condição de erro:
        
    
    }
    
    @Override
    public void navigation() {
       Position desiredPos = wm.getPacManPosition();
        
       if(this.pos == desiredPos) {
           return;
       }
       
       byte direction = wm.freePath((int)Math.round(this.pos.getX()), (int)Math.round(this.pos.getY()));
       
       //Tomada de decisão:
       if(Math.random()*100 <= 98.0f*hysteresisCoef) {
            hysteresisCoef = 1.0f;
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
       //Aleatório:
       else {
           double aux = (this.pos.getX()*desiredPos.getY())%3;
           byte possibleDirection = (byte)Math.pow(2.0d, aux);
           hysteresisCoef = 0.20f;
           
           switch (possibleDirection & direction) {
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
