/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package elements;

import control.WorldMap;
import java.io.Serializable;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.ImageIcon;
import utils.Consts;
import utils.Drawing;
import utils.ImageCollection;

/**
 *
 * @author aribeiro
 */
public class Fruit extends Items implements Serializable{
    private int time;
    transient private Timer timer;
    private static WorldMap wm = WorldMap.getInstance();
    
    private void setTimer() {
        Fruit self = this;
        timer = new Timer();
        timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    Drawing.getGameScreen().removeElement(self);
                }
            }, time);
    }

    private void choosePosition() {
        int i = (int) (Math.random() * Consts.NUM_CELLS_X);
        int j = (int) (Math.random() * Consts.NUM_CELLS_Y);
        
        char c = wm.getElement(i, j);
        
        while(c != ' ' && c != '.') {
            i = (int) (Math.random() * Consts.NUM_CELLS_X);
            j = (int) (Math.random() * Consts.NUM_CELLS_Y);
            c = wm.getElement(i, j);
        }
        
        
        
        this.setPosition(i, j);
    }
    
    public Fruit(String imageName, String name, int value, int time) {
        super(imageName, name, value);
        this.time = time;

        choosePosition();
        setTimer();

        this.isMortal = true;
        this.isTransposable = true;
    }

    public Fruit(ImageIcon image, String name, int value, int time) {
        super(image, name, value);
        this.time = time;

        choosePosition();
        setTimer();
        
        this.isMortal = true;
        this.isTransposable = true;

    }
    
    public int getTime() {
        return this.time;
    }
    
}
