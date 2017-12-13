package utils;

import java.io.File;

/**
 * Projeto de POO 2017
 * 
 * @author Luiz Eduardo
 * Baseado em material do Prof. Jose Fernando Junior
 */
public class Consts {
    public static final int CELL_SIZE = 32;
    public static final int NUM_CELLS_X = 15;
    public static final int NUM_CELLS_Y = 20;
    public static final int HEADER_SIZE = 2;
    public static final int SPRITE_CELL_SIZE = 48;
    
    public static final int WALK_STEP_DEC_PLACES = 1;
    public static final double WALK_STEP = 0.1;
    
    public static final String PATH = File.separator+"imgs"+File.separator;
    
    public static final int DELAY = 20;
    public static final int ANIMATION_DELAY = 50;
    

    public static final int CHERRY_TIME = 50000;
    public static final int STRAWBERRY_TIME = 75000;
    public static final int FRUIT_LIFE = 15000;
    
    public static final int PHANTOMS_NUM_STEPS = 50;

    public static class Timer {
        public static final int POWERPELLET_EFFECT = 4000;
        public static final int POWERPELLET_EFFECT_ENDIND = 3000;
    }
    
    public static enum Sprite {
        //Para a escolha das imagens do PacMan:
        PACMAN_CLOSE,
        PACMAN_LEFT_0,
        PACMAN_LEFT_1,
        PACMAN_RIGHT_0,
        PACMAN_RIGHT_1,
        PACMAN_TOP_0,
        PACMAN_TOP_1,
        PACMAN_BOTTOM_0,
        PACMAN_BOTTOM_1,
        
        PACMAN_DYING_1,
        PACMAN_DYING_2,
        PACMAN_DYING_3,
        PACMAN_DYING_4,
        PACMAN_DYING_5,
        PACMAN_DYING_6,
        PACMAN_DYING_7,
        PACMAN_DYING_8,
        PACMAN_DYING_9,
        PACMAN_DYING_10,
        PACMAN_DYING_11,
        PACMAN_DYING_12,

        CHERRY,
        STRAWBERRY,
        
        WALL_VERTICAL,
        WALL_HORIZONTAL,
        
        POWER_PELLETS,
        PACDOTS,

        PHANTOM_BLUE_0,
        PHANTOM_BLUE_1,
        PHANTOM_WHITE_0,
        PHANTOM_WHITE_1,
        
        PHANTOM_EYE_LEFT,
        PHANTOM_EYE_RIGHT,
        PHANTOM_EYE_UP,
        PHANTOM_EYE_DOWN,
        
        CLYDE_BOTTOM_0,
        CLYDE_BOTTOM_1,
        CLYDE_LEFT_0,
        CLYDE_LEFT_1,
        CLYDE_RIGHT_0,
        CLYDE_RIGHT_1,
        CLYDE_TOP_0,
        CLYDE_TOP_1,
        
        INKY_BOTTOM_0,
        INKY_BOTTOM_1,
        INKY_LEFT_0,
        INKY_LEFT_1,
        INKY_RIGHT_0,
        INKY_RIGHT_1,
        INKY_TOP_0,
        INKY_TOP_1,
        
        PINKY_BOTTOM_0,
        PINKY_BOTTOM_1,
        PINKY_LEFT_0,
        PINKY_LEFT_1,
        PINKY_RIGHT_0,
        PINKY_RIGHT_1,
        PINKY_TOP_0,
        PINKY_TOP_1,
        
        BLINKY_BOTTOM_0,
        BLINKY_BOTTOM_1,
        BLINKY_LEFT_0,
        BLINKY_LEFT_1,
        BLINKY_RIGHT_0,
        BLINKY_RIGHT_1,
        BLINKY_TOP_0,
        BLINKY_TOP_1
    }
    
    public static enum Animation {
        PACMAN_LEFT,
        PACMAN_RIGHT,
        PACMAN_UP,
        PACMAN_DOWN,
        
        PACMAN_DYING,

        EDIBLE,
        EDIBLE_ENDING,
        
        CLYDE_DOWN,
        CLYDE_LEFT,
        CLYDE_RIGHT,
        CLYDE_UP,
        
        INKY_DOWN,
        INKY_LEFT,
        INKY_RIGHT,
        INKY_UP,
        
        PINKY_DOWN,
        PINKY_LEFT,
        PINKY_RIGHT,
        PINKY_UP,
        
        BLINKY_DOWN,
        BLINKY_LEFT,
        BLINKY_RIGHT,
        BLINKY_UP
    }
    
    public static enum ImgCollection {
        PACMAN,
        CLYDE,
        INKY,
        BLINKY,
        PINKY,
        EDIBLE,
        EDIBLE_ENDING,
        FRUIT,
        WALL
    }
}
