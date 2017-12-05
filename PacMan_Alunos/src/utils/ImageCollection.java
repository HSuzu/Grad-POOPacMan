package utils;

import java.util.HashMap;
import javax.swing.ImageIcon;

public class ImageCollection {
    private final HashMap<Integer, Animation> animations;
    private final HashMap<Integer, ImageIcon> staticImages;
    private int lastKey;
    
    public ImageCollection() {
        animations = new HashMap<>();
        staticImages = new HashMap<>();
        lastKey = -1;
    }
    
    public void addImage(int key, ImageIcon image) {
        staticImages.put(key, image);
    }
    
    public void addAnimation(int key, Animation animation) {
        animations.put(key, animation);
    }
    
    public ImageIcon getImage(int key) {
        lastKey = key;
        
        ImageIcon rtrn;
        rtrn = staticImages.get(key);
        
        if(rtrn == null) {
            rtrn = animations.get(key).getAnimation();
        }
        
        return rtrn;
    }
    
    public void stopAnimation() {
        Animation curr = animations.get(lastKey);
        
        if(curr != null) {
            curr.stop();
        }
    }
    
    public void startAnimation() {
        Animation curr = animations.get(lastKey);
        
        if(curr != null) {
            curr.start();
        }
    }
}
