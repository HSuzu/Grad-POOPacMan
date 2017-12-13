package elements.phantom;

import control.WorldMap;
import java.io.Serializable;
import utils.Position;
import javax.swing.ImageIcon;
import utils.Consts;
import utils.ImageCollection;

public class Blinky extends Phantom implements Serializable {
    private float hysteresisCoef;
    
    public Blinky(String imageName, int value) {
        super(imageName, value);
        hysteresisCoef = 1.0f;
        this.resetCounterStep();
        this.setPath((byte)0);
        this.setOldDirection((byte)0);
    }
    
    public Blinky(ImageCollection collection, int defaultImage, int value) {
        super(collection, defaultImage, value);
        hysteresisCoef = 1.0f;
        this.resetCounterStep();
        this.setPath((byte)0);
        this.setOldDirection((byte)0);
    }
    
    @Override
    public String name() {
        return "Blinky";
    }
    
    @Override
    public void navigation() {
       Position desiredPos = wm.getPacManPosition();
          
       //Tomada de decisão:
       if(Math.random()*100 <= 98.0f*hysteresisCoef) {
            hysteresisCoef = 1.0f;
            this.followPos(desiredPos);
       }
       //Aleatório:
       else {
           hysteresisCoef = 0.2f;
           this.randomMove();
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
                if(this.state == State.EYE) {
                    return this.collection.getImage(Consts.Sprite.PHANTOM_EYE_LEFT);
                }
                return this.collection.getImage(Consts.Animation.BLINKY_LEFT);
            case Phantom.MOVE_RIGHT:
                if(this.state == State.EYE) {
                    return this.collection.getImage(Consts.Sprite.PHANTOM_EYE_RIGHT);
                }
                return this.collection.getImage(Consts.Animation.BLINKY_RIGHT);
            case Phantom.MOVE_UP:
                if(this.state == State.EYE) {
                    return this.collection.getImage(Consts.Sprite.PHANTOM_EYE_UP);
                }
                return this.collection.getImage(Consts.Animation.BLINKY_UP);
            case Phantom.MOVE_DOWN:
                if(this.state == State.EYE) {
                    return this.collection.getImage(Consts.Sprite.PHANTOM_EYE_DOWN);
                }
                return this.collection.getImage(Consts.Animation.BLINKY_DOWN);
        }
        return this.collection.getImage(Consts.Animation.BLINKY_UP);
    }
}
