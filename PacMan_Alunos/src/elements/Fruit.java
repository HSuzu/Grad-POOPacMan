/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package elements;

import java.io.Serializable;

/**
 *
 * @author aribeiro
 */
public class Fruit extends Items implements Serializable{
    private int time;
    
    public Fruit(String imageName, String name, int score, int time) {
      super(imageName, name, score);
      this.time = time;
    }
    
    public int getTime() {
        return this.time;
    }
}
