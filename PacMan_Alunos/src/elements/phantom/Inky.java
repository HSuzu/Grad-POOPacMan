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
    
    public Inky(String imageName, int value) {
        super(imageName, value);
        hysteresisCoef = 1.0f;
        this.resetCounterStep();
        this.setPath((byte)0);
        this.setOldDirection((byte)0);
    }
    
    public Inky(ImageCollection collection, int defaultImage, int value) {
        super(collection, defaultImage, value);
        hysteresisCoef = 1.0f;
        this.resetCounterStep();
        this.setPath((byte)0);
        this.setOldDirection((byte)0);
    }
    
    @Override
    public String name() {
        return "Inky";
    }

    @Override
    protected void navigation() {
        Position desiredPos = wm.getPacManPosition();
        Position blinkyPos = wm.getBlinkyPosition();
        
        if(blinkyPos != null && this.pos.getDistanceTo(blinkyPos) < 8.0d) {
            followPos(desiredPos);
        }
        else {
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
                return this.collection.getImage(Consts.Animation.INKY_LEFT);
            case Phantom.MOVE_RIGHT:
                if(this.state == State.EYE) {
                    return this.collection.getImage(Consts.Sprite.PHANTOM_EYE_RIGHT);
                }
                return this.collection.getImage(Consts.Animation.INKY_RIGHT);
            case Phantom.MOVE_UP:
                if(this.state == State.EYE) {
                    return this.collection.getImage(Consts.Sprite.PHANTOM_EYE_UP);
                }
                return this.collection.getImage(Consts.Animation.INKY_UP);
            case Phantom.MOVE_DOWN:
                if(this.state == State.EYE) {
                    return this.collection.getImage(Consts.Sprite.PHANTOM_EYE_DOWN);
                }
                return this.collection.getImage(Consts.Animation.INKY_DOWN);
        }
        return this.collection.getImage(Consts.Animation.INKY_UP);
    }
}
