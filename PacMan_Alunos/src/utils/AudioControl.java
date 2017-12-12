package utils;

import java.io.File;
import java.io.IOException;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class AudioControl implements LineListener {
    private Clip clip = null;
    private String nextFile;
    private AudioInputStream audioStream;
    private boolean repeat = false;
    private boolean running = false;
    
    public AudioControl() throws LineUnavailableException {
        nextFile = "";
    }
    
    public void setNext(String file) {
        nextFile = file;
    }
    
    public void stop() {
        if(clip != null && clip.isActive()) {
            repeat = false;
            running = false;
            clip.stop();
        }
    }
    
    public void start(boolean forceStart, boolean repeat) {
        this.repeat = repeat;
        if(running == false || forceStart) {
            running = true;

            try {
                audioStream = AudioSystem.getAudioInputStream(new File(nextFile));

                if(!repeat) {
                    nextFile = "";
                }

                clip = AudioSystem.getClip();
                clip.addLineListener(this);

                clip.open(audioStream);
                clip.start();
            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    @Override
    public void update(LineEvent le) {
        if(le.getType() == LineEvent.Type.CLOSE || le.getType() == LineEvent.Type.STOP) {
            running = false;
            if(repeat) {
                start(false, true);
            }
        }
    }
}
