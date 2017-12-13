package control;

import java.awt.Color;
import java.awt.Graphics;
import java.io.File;
import java.io.IOException;
import javax.sound.sampled.LineUnavailableException;
import utils.AudioControl;
import utils.Consts;

public class BackgroundElement {
    private WorldMap map;
    private byte stage = 1;
    AudioControl audioBackground;
    
    public BackgroundElement() {
        map = WorldMap.getInstance();
        
        try {
            map.loadFile("maps/default");
        } catch(IOException ex) {
            System.out.println(ex.getMessage());
        }

        try {
            audioBackground = new AudioControl();
        } catch (LineUnavailableException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    public void loadNextStage() throws IOException {
        WorldMap.getInstance().loadFile("maps" + File.separator + "stage" + stage);
        stage++;
    }
    
    public byte getStage() {
        return stage;
    }
    
    public AudioControl getBkAudio() {
        return audioBackground;
    }
    
    public void setStage(byte stage) {
        this.stage = stage;
    }
    
    public void drawBackground(Graphics g) {
        g.setColor(Color.BLACK);

        int height = Consts.NUM_CELLS_Y;
        int width = Consts.NUM_CELLS_X;
        g.fillRect(0, Consts.HEADER_SIZE * Consts.CELL_SIZE,
                 width * Consts.CELL_SIZE, height * Consts.CELL_SIZE);
        
    }
}
