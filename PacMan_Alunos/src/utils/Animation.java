/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.util.ArrayList;
import java.util.Timer;
import java.util.List;
import java.util.TimerTask;
import javax.swing.ImageIcon;

/**
 *
 * @author suzukawa
 */
public class Animation {
    private Timer _timer;
    private final long _animationDelay;
    
    private List<ImageIcon> _images;
    
    private boolean _isRunning;
    
    private int _currFrame;
    private ImageIcon _currImage;
    
    public Animation(long animationDelay) {
        _animationDelay = animationDelay;
        _isRunning = false;
        
        _timer = new Timer();
        
        _images = new ArrayList<>();

        _currFrame = -1;
        _currImage = new ImageIcon();
    }
    
    public void start() {
        if(_isRunning == false) {
            _isRunning = true;

            _timer = new Timer();
            _timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if(++_currFrame >= _images.size()) {
                    _currFrame = 0;
                }

                try {
                    _currImage.setImage(_images.get(_currFrame).getImage());
                } catch(java.lang.IndexOutOfBoundsException err) {
                    System.err.println("Empty animation.\n");
                }
            }
        }, 0, _animationDelay);
        }
    }
    
    public void stop() {
        if(_isRunning) {
            _isRunning = false;

            _timer.cancel();
            _timer.purge();
        }
    }
    
    public ImageIcon getAnimation() {
        return _currImage;
    }
    
    public void addImage(ImageIcon img) {
        _images.add(img);
    }
}
