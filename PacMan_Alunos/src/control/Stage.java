package control;

import elements.*;
import elements.phanton.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.font.TextAttribute;
import java.io.File;
import java.io.IOException;
import java.text.AttributedString;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import utils.Animation;
import utils.AudioControl;
import utils.Consts;
import utils.ImageCollection;
import utils.Sprite;

public class Stage extends KeyAdapter {
    private BackgroundElement bkElem;
    
    AudioControl audio;
    
    private final PacMan pacman;
    private final ArrayList<Phantom> phantons;
    private final ArrayList<Fruit> fruits;
    private final ArrayList<Items> pacDots;
    private final ArrayList<Items> powerPellets;
    private final ArrayList<Wall> walls;
    
    protected Sprite sprite;
    private HashMap<Consts.Animation, Animation> animations;
    private HashMap<Consts.ImgCollection, ImageCollection> imgCollections;
    
    private Font font;
        
    public Stage() {
        loadImages();
        
        try {
            audio = new AudioControl();
        } catch (LineUnavailableException ex) {
            System.out.println(ex.getMessage());
        }

        
        bkElem = new BackgroundElement();
        
        pacman = new PacMan(imgCollections.get(Consts.ImgCollection.PACMAN), Consts.Animation.PACMAN_RIGHT.ordinal());
        pacman.setPosition(0, 0);
        
        phantons = new ArrayList<>();
        Pinky blinky = new Pinky(sprite.getImage(Consts.Sprite.CHERRY), 100);
        blinky.setPosition(11.0, 8.0);
        phantons.add(blinky);
        fruits = new ArrayList<>();
        Fruit cherry = new Fruit(sprite.getImage(Consts.Sprite.CHERRY), "Cherry", 100, 20000);
        cherry.setPosition(10.0, 10.0);
        fruits.add(cherry);
                        
        pacDots = new ArrayList<>();
        powerPellets = new ArrayList<>();
        walls = new ArrayList<>();
        
        char[][] map = WorldMap.getInstance().getMap();
        
        int i, j;
        for(i = 0; i < Consts.NUM_CELLS_X; i++) {
            for(j = 0; j < Consts.NUM_CELLS_Y; j++) {
                switch(map[i][j]) {
                    case 'p': {
                        pacman.setPosition(i, j);
                    } break;
                    case 'o': {
                        Items powerPellet = new Items(sprite.getImage(Consts.Sprite.POWER_PELLETS), "Dots", 100);
                        powerPellet.setPosition(i, j);
                        powerPellets.add(powerPellet);
                    } break;
                    case '.': {
                        Items pacDot = new Items(sprite.getImage(Consts.Sprite.PACDOTS), "Dots", 100);
                        pacDot.setPosition(i,j);
                        pacDots.add(pacDot);
                    } break;
                    case '|': {
                        Wall hwall = new Wall(sprite.getImage(Consts.Sprite.WALL_VERTICAL));
                        hwall.setPosition(i, j);
                        walls.add(hwall);
                    } break;
                    case '-': {
                        Wall vwall = new Wall(sprite.getImage(Consts.Sprite.WALL_HORIZONTAL));
                        vwall.setPosition(i, j);
                        walls.add(vwall);
                    } break;
                    default:
                    break;
                }
            }
        }
        
        try {
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("fonts/emulogic.ttf")));
            font = new Font("emulogic", Font.PLAIN, 18);
        } catch (IOException | FontFormatException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    public ArrayList<Element> getAllElements() {
        ArrayList<Element> elem = new ArrayList<>();
        
        elem.add(pacman);
        elem.addAll(phantons);
        elem.addAll(pacDots);
        elem.addAll(powerPellets);
        elem.addAll(fruits);
        elem.addAll(walls);
        
        return elem;
    }
    
    public void drawMap(Graphics g) {
        //Imprimir mapa:
        bkElem.drawBackground(g);
    }
    
    public void drawHeader(Graphics g) {
        g.setColor(Color.decode("#333333"));

        int height = Consts.HEADER_SIZE;
        int width = Consts.NUM_CELLS_X;
        g.fillRect(0, 0, width * Consts.CELL_SIZE, height * Consts.CELL_SIZE);
        
        //Mostrar pontuação:
        AttributedString word = new AttributedString("Score: "+pacman.getScore());
        word.addAttribute(TextAttribute.FONT, font);
        g.setColor(Color.green);
        g.drawString(word.getIterator(), Consts.CELL_SIZE, Consts.CELL_SIZE);
        //Mostrar vida:
        word = new AttributedString("Lifes: "+pacman.getNumLifes());
        word.addAttribute(TextAttribute.FONT, font);
        g.setColor(Color.green);
        g.drawString(word.getIterator(), (Consts.NUM_CELLS_X - 7)*Consts.CELL_SIZE, Consts.CELL_SIZE);
    }
    
    private void loadImages() {
        sprite = new Sprite("sprite3.png");
        animations = new HashMap<>();
        imgCollections = new HashMap<>();
        
        sprite.setDefaultParameters(16, 16, Consts.CELL_SIZE/16.0f);

        sprite.newImage(Consts.Sprite.CHERRY, 2, 3);
        sprite.newImage(Consts.Sprite.STRAWBERRY, 3, 3);
        
        sprite.newImage(Consts.Sprite.WALL_HORIZONTAL, 10, 3);
        sprite.newImage(Consts.Sprite.WALL_VERTICAL, 10, 3);
                
        sprite.newImage(Consts.Sprite.POWER_PELLETS, 11, 3);
        sprite.newImage(Consts.Sprite.PACDOTS, 11, 2);
        
        //Animação do PacMan:
        sprite.newImage(Consts.Sprite.PACMAN_LEFT_0, 1, 1);
        sprite.newImage(Consts.Sprite.PACMAN_LEFT_1, 0, 1);
        sprite.newImage(Consts.Sprite.PACMAN_TOP_0, 1, 2);
        sprite.newImage(Consts.Sprite.PACMAN_TOP_1, 0, 2);
        sprite.newImage(Consts.Sprite.PACMAN_RIGHT_0, 1, 0);
        sprite.newImage(Consts.Sprite.PACMAN_RIGHT_1, 0, 0);
        sprite.newImage(Consts.Sprite.PACMAN_BOTTOM_0, 1, 3);
        sprite.newImage(Consts.Sprite.PACMAN_BOTTOM_1, 0, 3);
        sprite.newImage(Consts.Sprite.PACMAN_CLOSE, 2, 0);
        
        Animation anLeft = new Animation(Consts.ANIMATION_DELAY);
        anLeft.addImage(sprite.getImage(Consts.Sprite.PACMAN_LEFT_1));
        anLeft.addImage(sprite.getImage(Consts.Sprite.PACMAN_LEFT_0));
        anLeft.addImage(sprite.getImage(Consts.Sprite.PACMAN_CLOSE));
        anLeft.addImage(sprite.getImage(Consts.Sprite.PACMAN_LEFT_0));

        animations.put(Consts.Animation.PACMAN_LEFT, anLeft);
        
        Animation anRight = new Animation(Consts.ANIMATION_DELAY);
        anRight.addImage(sprite.getImage(Consts.Sprite.PACMAN_RIGHT_1));
        anRight.addImage(sprite.getImage(Consts.Sprite.PACMAN_RIGHT_0));
        anRight.addImage(sprite.getImage(Consts.Sprite.PACMAN_CLOSE));
        anRight.addImage(sprite.getImage(Consts.Sprite.PACMAN_RIGHT_0));

        animations.put(Consts.Animation.PACMAN_RIGHT, anRight);
        
        Animation anTop = new Animation(Consts.ANIMATION_DELAY);
        anTop.addImage(sprite.getImage(Consts.Sprite.PACMAN_TOP_1));
        anTop.addImage(sprite.getImage(Consts.Sprite.PACMAN_TOP_0));
        anTop.addImage(sprite.getImage(Consts.Sprite.PACMAN_CLOSE));
        anTop.addImage(sprite.getImage(Consts.Sprite.PACMAN_TOP_0));

        animations.put(Consts.Animation.PACMAN_UP, anTop);
        
        Animation anBottom = new Animation(Consts.ANIMATION_DELAY);
        anBottom.addImage(sprite.getImage(Consts.Sprite.PACMAN_BOTTOM_1));
        anBottom.addImage(sprite.getImage(Consts.Sprite.PACMAN_BOTTOM_0));
        anBottom.addImage(sprite.getImage(Consts.Sprite.PACMAN_CLOSE));
        anBottom.addImage(sprite.getImage(Consts.Sprite.PACMAN_BOTTOM_0));

        animations.put(Consts.Animation.PACMAN_DOWN, anBottom);
        
        ImageCollection icPacman = new ImageCollection();
        icPacman.addAnimation(Consts.Animation.PACMAN_LEFT, anLeft);
        icPacman.addAnimation(Consts.Animation.PACMAN_UP, anTop);
        icPacman.addAnimation(Consts.Animation.PACMAN_RIGHT, anRight);
        icPacman.addAnimation(Consts.Animation.PACMAN_DOWN, anBottom);
        
        imgCollections.put(Consts.ImgCollection.PACMAN, icPacman);
        
        //Animação Blinky:
        sprite.newImage(Consts.Sprite.BLINKY_BOTTOM_0, 6, 7);
        sprite.newImage(Consts.Sprite.BLINKY_BOTTOM_1, 7, 7);
        sprite.newImage(Consts.Sprite.BLINKY_LEFT_0 , 2, 7);
        sprite.newImage(Consts.Sprite.BLINKY_LEFT_1 , 3, 7);
        sprite.newImage(Consts.Sprite.BLINKY_RIGHT_0 , 0, 7);
        sprite.newImage(Consts.Sprite.BLINKY_RIGHT_1 , 1, 7);
        sprite.newImage(Consts.Sprite.BLINKY_TOP_0 , 4, 7);
        sprite.newImage(Consts.Sprite.BLINKY_TOP_1 , 5, 7);
        
        Animation blinkyBottom = new Animation(Consts.ANIMATION_DELAY);
        blinkyBottom.addImage(sprite.getImage(Consts.Sprite.BLINKY_BOTTOM_0));
        blinkyBottom.addImage(sprite.getImage(Consts.Sprite.BLINKY_BOTTOM_1));
        animations.put(Consts.Animation.BLINKY_DOWN, blinkyBottom);
        
        Animation blinkyLeft = new Animation(Consts.ANIMATION_DELAY);
        blinkyLeft.addImage(sprite.getImage(Consts.Sprite.BLINKY_LEFT_0));
        blinkyLeft.addImage(sprite.getImage(Consts.Sprite.BLINKY_LEFT_1));
        animations.put(Consts.Animation.BLINKY_LEFT, blinkyLeft);
        
        Animation blinkyRight = new Animation(Consts.ANIMATION_DELAY);
        blinkyRight.addImage(sprite.getImage(Consts.Sprite.BLINKY_RIGHT_0));
        blinkyRight.addImage(sprite.getImage(Consts.Sprite.BLINKY_RIGHT_1));
        animations.put(Consts.Animation.BLINKY_RIGHT, blinkyRight);
        
        Animation blinkyTop = new Animation(Consts.ANIMATION_DELAY);
        blinkyTop.addImage(sprite.getImage(Consts.Sprite.BLINKY_TOP_0));
        blinkyTop.addImage(sprite.getImage(Consts.Sprite.BLINKY_TOP_1));
        animations.put(Consts.Animation.BLINKY_UP, blinkyTop);
        
        ImageCollection icBlinky = new ImageCollection();
        icBlinky.addAnimation(Consts.Animation.BLINKY_DOWN, blinkyBottom);
        icBlinky.addAnimation(Consts.Animation.BLINKY_LEFT, blinkyLeft);
        icBlinky.addAnimation(Consts.Animation.BLINKY_RIGHT, blinkyRight);
        icBlinky.addAnimation(Consts.Animation.BLINKY_UP, blinkyTop);
        imgCollections.put(Consts.ImgCollection.BLINKY, icBlinky);

        //Animação Pinky:
        sprite.newImage(Consts.Sprite.PINKY_BOTTOM_0, 6, 8);
        sprite.newImage(Consts.Sprite.PINKY_BOTTOM_1, 7, 8);
        sprite.newImage(Consts.Sprite.PINKY_LEFT_0 , 2, 8);
        sprite.newImage(Consts.Sprite.PINKY_LEFT_1 , 3, 8);
        sprite.newImage(Consts.Sprite.PINKY_RIGHT_0 , 0, 8);
        sprite.newImage(Consts.Sprite.PINKY_RIGHT_1 , 1, 8);
        sprite.newImage(Consts.Sprite.PINKY_TOP_0 , 4, 8);
        sprite.newImage(Consts.Sprite.PINKY_TOP_1 , 5, 8);
        
        Animation pinkyBottom = new Animation(Consts.ANIMATION_DELAY);
        pinkyBottom.addImage(sprite.getImage(Consts.Sprite.PINKY_BOTTOM_0));
        pinkyBottom.addImage(sprite.getImage(Consts.Sprite.PINKY_BOTTOM_1));
        animations.put(Consts.Animation.PINKY_DOWN, pinkyBottom);
        
        Animation pinkyLeft = new Animation(Consts.ANIMATION_DELAY);
        pinkyLeft.addImage(sprite.getImage(Consts.Sprite.PINKY_LEFT_0));
        pinkyLeft.addImage(sprite.getImage(Consts.Sprite.PINKY_LEFT_1));
        animations.put(Consts.Animation.PINKY_LEFT, pinkyLeft);
        
        Animation pinkyRight = new Animation(Consts.ANIMATION_DELAY);
        pinkyRight.addImage(sprite.getImage(Consts.Sprite.PINKY_RIGHT_0));
        pinkyRight.addImage(sprite.getImage(Consts.Sprite.PINKY_RIGHT_1));
        animations.put(Consts.Animation.PINKY_RIGHT, pinkyRight);
        
        Animation pinkyTop = new Animation(Consts.ANIMATION_DELAY);
        pinkyTop.addImage(sprite.getImage(Consts.Sprite.PINKY_TOP_0));
        pinkyTop.addImage(sprite.getImage(Consts.Sprite.PINKY_TOP_1));
        animations.put(Consts.Animation.PINKY_UP, pinkyTop);
        
        ImageCollection icPinky = new ImageCollection();
        icPinky.addAnimation(Consts.Animation.PINKY_DOWN, pinkyBottom);
        icPinky.addAnimation(Consts.Animation.PINKY_LEFT, pinkyLeft);
        icPinky.addAnimation(Consts.Animation.PINKY_RIGHT, pinkyRight);
        icPinky.addAnimation(Consts.Animation.PINKY_UP, pinkyTop);
        imgCollections.put(Consts.ImgCollection.PINKY, icPinky);
        
        //Animação Inky:
        sprite.newImage(Consts.Sprite.INKY_BOTTOM_0, 6, 9);
        sprite.newImage(Consts.Sprite.INKY_BOTTOM_1, 7, 9);
        sprite.newImage(Consts.Sprite.INKY_LEFT_0 , 2, 9);
        sprite.newImage(Consts.Sprite.INKY_LEFT_1 , 3, 9);
        sprite.newImage(Consts.Sprite.INKY_RIGHT_0 , 0, 9);
        sprite.newImage(Consts.Sprite.INKY_RIGHT_1 , 1, 9);
        sprite.newImage(Consts.Sprite.INKY_TOP_0 , 4, 9);
        sprite.newImage(Consts.Sprite.INKY_TOP_1 , 5, 9);
        
        Animation inkyBottom = new Animation(Consts.ANIMATION_DELAY);
        inkyBottom.addImage(sprite.getImage(Consts.Sprite.INKY_BOTTOM_0));
        inkyBottom.addImage(sprite.getImage(Consts.Sprite.INKY_BOTTOM_1));
        animations.put(Consts.Animation.INKY_DOWN, inkyBottom);
        
        Animation inkyLeft = new Animation(Consts.ANIMATION_DELAY);
        inkyLeft.addImage(sprite.getImage(Consts.Sprite.INKY_LEFT_0));
        inkyLeft.addImage(sprite.getImage(Consts.Sprite.INKY_LEFT_1));
        animations.put(Consts.Animation.INKY_LEFT, inkyLeft);
        
        Animation inkyRight = new Animation(Consts.ANIMATION_DELAY);
        inkyRight.addImage(sprite.getImage(Consts.Sprite.INKY_RIGHT_0));
        inkyRight.addImage(sprite.getImage(Consts.Sprite.INKY_RIGHT_1));
        animations.put(Consts.Animation.PINKY_RIGHT, pinkyRight);
        
        Animation inkyTop = new Animation(Consts.ANIMATION_DELAY);
        inkyTop.addImage(sprite.getImage(Consts.Sprite.INKY_TOP_0));
        inkyTop.addImage(sprite.getImage(Consts.Sprite.INKY_TOP_1));
        animations.put(Consts.Animation.INKY_UP, inkyTop);
        
        ImageCollection icInky = new ImageCollection();
        icInky.addAnimation(Consts.Animation.INKY_DOWN, inkyBottom);
        icInky.addAnimation(Consts.Animation.INKY_LEFT, inkyLeft);
        icInky.addAnimation(Consts.Animation.INKY_RIGHT, inkyRight);
        icInky.addAnimation(Consts.Animation.INKY_UP, inkyTop);
        imgCollections.put(Consts.ImgCollection.INKY, icInky);
        
        //Animação Clyde:
        sprite.newImage(Consts.Sprite.CLYDE_BOTTOM_0, 6, 10);
        sprite.newImage(Consts.Sprite.CLYDE_BOTTOM_1, 7, 10);
        sprite.newImage(Consts.Sprite.CLYDE_LEFT_0 , 2, 10);
        sprite.newImage(Consts.Sprite.CLYDE_LEFT_1 , 3, 10);
        sprite.newImage(Consts.Sprite.CLYDE_RIGHT_0 , 0, 10);
        sprite.newImage(Consts.Sprite.CLYDE_RIGHT_1 , 1, 10);
        sprite.newImage(Consts.Sprite.CLYDE_TOP_0 , 4, 10);
        sprite.newImage(Consts.Sprite.CLYDE_TOP_1 , 5, 10);
        
        Animation clydeBottom = new Animation(Consts.ANIMATION_DELAY);
        clydeBottom.addImage(sprite.getImage(Consts.Sprite.CLYDE_BOTTOM_0));
        clydeBottom.addImage(sprite.getImage(Consts.Sprite.CLYDE_BOTTOM_1));
        animations.put(Consts.Animation.CLYDE_DOWN, clydeBottom);
        
        Animation clydeLeft = new Animation(Consts.ANIMATION_DELAY);
        clydeLeft.addImage(sprite.getImage(Consts.Sprite.CLYDE_LEFT_0));
        clydeLeft.addImage(sprite.getImage(Consts.Sprite.CLYDE_LEFT_1));
        animations.put(Consts.Animation.CLYDE_LEFT, clydeLeft);
        
        Animation clydeRight = new Animation(Consts.ANIMATION_DELAY);
        clydeRight.addImage(sprite.getImage(Consts.Sprite.CLYDE_RIGHT_0));
        clydeRight.addImage(sprite.getImage(Consts.Sprite.CLYDE_RIGHT_1));
        animations.put(Consts.Animation.CLYDE_RIGHT, clydeRight);
        
        Animation clydeTop = new Animation(Consts.ANIMATION_DELAY);
        clydeTop.addImage(sprite.getImage(Consts.Sprite.CLYDE_TOP_0));
        clydeTop.addImage(sprite.getImage(Consts.Sprite.CLYDE_TOP_1));
        animations.put(Consts.Animation.CLYDE_UP, clydeTop);
        
        ImageCollection icClyde = new ImageCollection();
        icClyde.addAnimation(Consts.Animation.CLYDE_DOWN, clydeBottom);
        icClyde.addAnimation(Consts.Animation.CLYDE_LEFT, clydeLeft);
        icClyde.addAnimation(Consts.Animation.CLYDE_RIGHT, clydeRight);
        icClyde.addAnimation(Consts.Animation.CLYDE_UP, clydeTop);
        imgCollections.put(Consts.ImgCollection.CLYDE, icClyde);
    }
    
    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
                pacman.setMovDirection(PacMan.MOVE_UP);
            break;
            case KeyEvent.VK_DOWN:
                pacman.setMovDirection(PacMan.MOVE_DOWN);
            break;
            case KeyEvent.VK_LEFT:
                pacman.setMovDirection(PacMan.MOVE_LEFT);
            break;
            case KeyEvent.VK_RIGHT:
                pacman.setMovDirection(PacMan.MOVE_RIGHT);
            break;
            case KeyEvent.VK_SPACE:
                pacman.setMovDirection(PacMan.STOP);
            break;
            default:
            break;
        }
    }

    public void overlapListener(Element e) {
        if(e instanceof Items) {
            if(((Items) e).getName().compareTo("Dots") != 0) {
                audio.setNext("sound/pacman_eatfruit.wav");
                audio.start(false, false);
            }
        }
    }
    
    public void iterationListener() {
        
    }
}
