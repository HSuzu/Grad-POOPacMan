/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.util.HashMap;
import javax.swing.ImageIcon;

/**
 *
 * @author suzukawa
 */
public class ImageCollection {
    private final HashMap<Integer, Animation> _animations;
    private final HashMap<Integer, ImageIcon> _staticImages;
    private int _lastKey;
    
    public ImageCollection() {
        _animations = new HashMap<>();
        _staticImages = new HashMap<>();
        _lastKey = -1;
    }
    
    public void addImage(int key, ImageIcon image) {
        _staticImages.put(key, image);
    }
    
    public void addAnimation(int key, Animation animation) {
        _animations.put(key, animation);
    }
    
    public ImageIcon getImage(int key) {
        _lastKey = key;
        
        ImageIcon rtrn;
        rtrn = _staticImages.get(key);
        
        if(rtrn == null) {
            rtrn = _animations.get(key).getAnimation();
        }
        
        return rtrn;
    }
    
    public void stopAnimation() {
        Animation curr = _animations.get(_lastKey);
        
        if(curr != null) {
            curr.stop();
        }
    }
    
    public void startAnimation() {
        Animation curr = _animations.get(_lastKey);
        
        if(curr != null) {
            curr.start();
        }
    }
}
