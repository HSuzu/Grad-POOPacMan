package utils;

import java.io.Serializable;
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

    public void addImage(Consts.Sprite key, ImageIcon image) {
        staticImages.put(key.ordinal(), image);
    }
    
    public void addAnimation(int key, Animation animation) {
        animations.put(key, animation);
    }

    public void addAnimation(Consts.Animation key, Animation animation) {
        animations.put(key.ordinal(), animation);
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
    
    public ImageIcon getImage(Consts.Animation key) {
        lastKey = key.ordinal();
        
        return animations.get(key.ordinal()).getAnimation();
    }

    public ImageIcon getImage(Consts.Sprite key) {
        lastKey = key.ordinal();
        
        return staticImages.get(key.ordinal());
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
    
    public void reset() {
        for(Animation a : animations.values()) {
            if(a.isRunning()) {
                a.forceStart();
            }
        }
    }
}
