/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package elements.phantom;

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
        this.resetCounterStep();
        this.setPath((byte)0);
        this.setOldDirection((byte)0);
    }

    public Clyde(ImageCollection collection, int defaultImage, int value) {
        super(collection, defaultImage, value);
        hysteresisCoef = 1.0f;
        this.resetCounterStep();
        this.setPath((byte)0);
        this.setOldDirection((byte)0);
    }
    
    @Override
    public String name() {
        return "Clyde";
    }

    @Override
    protected void navigation() {
        Position desiredPos = wm.getPacManPosition();
        if(this.pos.getDistanceTo(desiredPos) > 8.0d && Math.random()*100 <= 98.0f*hysteresisCoef) {
            hysteresisCoef = 1.0f;
            this.followPos(desiredPos);
 
        }
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
                return this.collection.getImage(Consts.Animation.CLYDE_LEFT);
            case Phantom.MOVE_RIGHT:
                if(this.state == State.EYE) {
                    return this.collection.getImage(Consts.Sprite.PHANTOM_EYE_RIGHT);
                }
                return this.collection.getImage(Consts.Animation.CLYDE_RIGHT);
            case Phantom.MOVE_UP:
                if(this.state == State.EYE) {
                    return this.collection.getImage(Consts.Sprite.PHANTOM_EYE_UP);
                }
                return this.collection.getImage(Consts.Animation.CLYDE_UP);
            case Phantom.MOVE_DOWN:
                if(this.state == State.EYE) {
                    return this.collection.getImage(Consts.Sprite.PHANTOM_EYE_DOWN);
                }
                return this.collection.getImage(Consts.Animation.CLYDE_DOWN);
        }
        return this.collection.getImage(Consts.Animation.CLYDE_UP);
    }
}
