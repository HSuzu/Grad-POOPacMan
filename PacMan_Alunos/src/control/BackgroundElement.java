package control;

import java.awt.Color;
import java.awt.Graphics;
import java.io.File;
import java.io.IOException;
import javax.sound.sampled.LineUnavailableException;
import utils.AudioControl;
import utils.Consts;

public class BackgroundElement {
    // MAP
    private WorldMap map;
    // estado atual
    private byte stage = 1;
    // audio
    AudioControl audioBackground;
    
    public BackgroundElement() {
        // instancia do singleton
        map = WorldMap.getInstance();
        
        // carrega o arquivo de mapa
        try {
            map.loadFile("maps/default");
        } catch(IOException ex) {
            System.out.println(ex.getMessage());
        }

        // inicializa o sistema de audio
        try {
            audioBackground = new AudioControl();
        } catch (LineUnavailableException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    public void loadNextStage() throws IOException {
        // carrega um novo mapa de acordo com o estado atual
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
        // desenha o fundo preto
        g.setColor(Color.BLACK);

        int height = Consts.NUM_CELLS_Y;
        int width = Consts.NUM_CELLS_X;
        g.fillRect(0, Consts.HEADER_SIZE * Consts.CELL_SIZE,
                 width * Consts.CELL_SIZE, height * Consts.CELL_SIZE);
        
    }
}
