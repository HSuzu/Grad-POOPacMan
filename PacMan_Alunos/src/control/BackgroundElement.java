package control;

import java.awt.Color;
import java.awt.Graphics;
import utils.Consts;

public class BackgroundElement {
    private WorldMap map;
    
    public BackgroundElement() {
        map = WorldMap.getInstance();
        
        map.loadFile("maps/default");
    }
    
    public void drawBackground(Graphics g) {
        g.setColor(Color.BLACK);

        int height = Consts.NUM_CELLS_Y;
        int width = Consts.NUM_CELLS_X;
        g.fillRect(0, Consts.HEADER_SIZE * Consts.CELL_SIZE,
                 width * Consts.CELL_SIZE, height * Consts.CELL_SIZE);
        
    }
}
