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
import utils.Animation;
import utils.Consts;
import utils.ImageCollection;
import utils.Sprite;

public class Stage extends KeyAdapter {
    BackgroundElement bkElem;
    
    private final PacMan pacman;
    private final ArrayList<Phanton> phantons;
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
        
        bkElem = new BackgroundElement();
        
        pacman = new PacMan(imgCollections.get(Consts.ImgCollection.PACMAN), Consts.Animation.PACMAN_RIGHT.ordinal());
        pacman.setPosition(0, 0);
        
        phantons = new ArrayList<>();
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
                        Items powerPellet = new Items(sprite.getImage(Consts.Sprite.POWER_PELLETS), "Power Pellets", 100);
                        powerPellet.setPosition(i, j);
                        powerPellets.add(powerPellet);
                    } break;
                    case '.': {
                        Items pacDot = new Items(sprite.getImage(Consts.Sprite.PACDOTS), "PacDots", 100);
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
        elem.addAll(fruits);
        elem.addAll(pacDots);
        elem.addAll(powerPellets);
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
        sprite = new Sprite("sprite2.png");
        animations = new HashMap<>();
        imgCollections = new HashMap<>();
        
        sprite.setDefaultParameters(16, 16, Consts.CELL_SIZE/16.0f);

        sprite.newImage(Consts.Sprite.CHERRY, 2, 3);
        sprite.newImage(Consts.Sprite.STRAWBERRY, 3, 3);
        
        sprite.newImage(Consts.Sprite.WALL_HORIZONTAL, 9, 3);
        sprite.newImage(Consts.Sprite.WALL_VERTICAL, 9, 3);
                
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
        
        Animation anLeft = new Animation(125);
        anLeft.addImage(sprite.getImage(Consts.Sprite.PACMAN_LEFT_1));
        anLeft.addImage(sprite.getImage(Consts.Sprite.PACMAN_LEFT_0));
        anLeft.addImage(sprite.getImage(Consts.Sprite.PACMAN_CLOSE));
        anLeft.addImage(sprite.getImage(Consts.Sprite.PACMAN_LEFT_0));

        animations.put(Consts.Animation.PACMAN_LEFT, anLeft);
        
        Animation anRight = new Animation(125);
        anRight.addImage(sprite.getImage(Consts.Sprite.PACMAN_RIGHT_1));
        anRight.addImage(sprite.getImage(Consts.Sprite.PACMAN_RIGHT_0));
        anRight.addImage(sprite.getImage(Consts.Sprite.PACMAN_CLOSE));
        anRight.addImage(sprite.getImage(Consts.Sprite.PACMAN_RIGHT_0));

        animations.put(Consts.Animation.PACMAN_RIGHT, anRight);
        
        Animation anTop = new Animation(125);
        anTop.addImage(sprite.getImage(Consts.Sprite.PACMAN_TOP_1));
        anTop.addImage(sprite.getImage(Consts.Sprite.PACMAN_TOP_0));
        anTop.addImage(sprite.getImage(Consts.Sprite.PACMAN_CLOSE));
        anTop.addImage(sprite.getImage(Consts.Sprite.PACMAN_TOP_0));

        animations.put(Consts.Animation.PACMAN_UP, anTop);
        
        Animation anBottom = new Animation(125);
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

    @Override
    public void keyReleased(KeyEvent e) {
        switch(e.getKeyCode()) {
            case KeyEvent.VK_UP:
                if(pacman.getMovimentDirection() == PacMan.MOVE_UP) {
                    pacman.resetMovDirection();
                }
            break;
            case KeyEvent.VK_DOWN:
                if(pacman.getMovimentDirection() == PacMan.MOVE_DOWN) {
                    pacman.resetMovDirection();
                }
            break;
            case KeyEvent.VK_LEFT:
                if(pacman.getMovimentDirection() == PacMan.MOVE_LEFT) {
                    pacman.resetMovDirection();
                }
            break;
            case KeyEvent.VK_RIGHT:
                if(pacman.getMovimentDirection() == PacMan.MOVE_RIGHT) {
                    pacman.resetMovDirection();
                }
            break;
        }
    }

}
