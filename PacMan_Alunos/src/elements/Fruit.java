/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package elements;

import java.io.Serializable;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.ImageIcon;
import utils.Drawing;
import utils.ImageCollection;

/**
 *
 * @author aribeiro
 */
public class Fruit extends Items implements Serializable{
    private int time;
    private Timer timer;
    
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

    public Fruit(String imageName, String name, int value, int time) {
      super(imageName, name, value);
      this.time = time;
      
      setTimer();
    }

    public Fruit(ImageIcon image, String name, int value, int time) {
      super(image, name, value);
      this.time = time;

      setTimer();
    }
    
    public int getTime() {
        return this.time;
    }
    
}
