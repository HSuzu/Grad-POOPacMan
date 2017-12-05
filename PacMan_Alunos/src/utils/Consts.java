package utils;

import java.io.File;

/**
 * Projeto de POO 2017
 * 
 * @author Luiz Eduardo
 * Baseado em material do Prof. Jose Fernando Junior
 */
public class Consts {
    public static final int CELL_SIZE = 30;
    public static final int NUM_CELLS_X = 20;
    public static final int NUM_CELLS_Y = 20;
    
    public static final int WALK_STEP_DEC_PLACES = 1;
    public static final double WALK_STEP = 0.1;
    
    public static final String PATH = File.separator+"imgs"+File.separator;
    
    public static final int DELAY = 20;
    public static final int TIMER_FOGO = 40;
    
    public static class Animation {
        public enum PacMan {
            CLOSE,
            LEFT_0,
            LEFT_1,
            RIGHT_0,
            RIGHT_1,
            TOP_0,
            TOP_1,
            BOTTOM_0,
            BOTTOM_1
        }
    }
}
