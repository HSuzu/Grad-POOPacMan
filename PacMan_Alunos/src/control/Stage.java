package control;

import elements.*;
import elements.phanton.*;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.event.KeyEvent;
import java.awt.font.TextAttribute;
import java.io.File;
import java.io.IOException;
import java.text.AttributedString;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import utils.Animation;
import utils.Consts;
import utils.ImageCollection;
import utils.Sprite;

public class Stage {
    BackgroundElement bkElem;
    
    private final PacMan pacman;
    private final ArrayList<Phanton> phantons;
    private final ArrayList<Fruit> fruits;
    private final ArrayList<Items> pacDots;
    private final ArrayList<Items> powerPellets;
    private final ArrayList<Element> walls;
    
    protected Sprite sprite;
    private HashMap<Consts.Animation, Animation> animations;
    private HashMap<Consts.ImgCollection, ImageCollection> imgCollections;
    
    private Font font;
    
    public Stage() {
        loadImages();
        
        bkElem = new BackgroundElement();
        
        pacman = new PacMan(imgCollections.get(Consts.ImgCollection.PACMAN), Consts.Animation.PACMAN_RIGHT.ordinal());
        pacman.setPosition(Consts.HEADER_SIZE +0.5, 0);
        
        phantons = new ArrayList<>();
        fruits = new ArrayList<>();
        Fruit cherry = new Fruit(sprite.getImage(Consts.Sprite.CHERRY), "Cherry", 100, 20000);
        cherry.setPosition(10.0, 10.0);
        fruits.add(cherry);
                        
        pacDots = new ArrayList<>();
        powerPellets = new ArrayList<>();
        walls = new ArrayList<>();
        
        
        
        try {
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("emulogic.ttf")));
            font = new Font("emulogic", Font.PLAIN, 18);
                
        } catch (IOException | FontFormatException ex) {
            Logger.getLogger(GameScreen.class.getName()).log(Level.SEVERE, null, ex);
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
    
    public void drawStage(Graphics g) {
        g.setColor(Color.BLACK);

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
        g.drawString(word.getIterator(), 13*Consts.CELL_SIZE, Consts.CELL_SIZE);
        
        //Imprimir mapa:
        bkElem.drawBackground(g);
    }
    
    private void loadImages() {
        sprite = new Sprite("sprite.png");
        animations = new HashMap<>();
        imgCollections = new HashMap<>();
        
        sprite.setDefaultParameters(96, 96, Consts.CELL_SIZE/96.0f);

        sprite.newImage(Consts.Sprite.CHERRY, 0, 5);
        sprite.newImage(Consts.Sprite.STRAWBERRY, 1, 5);
        
        ImageCollection icFruit = new ImageCollection();
        icFruit.addImage(Consts.Sprite.CHERRY, sprite.getImage(Consts.Sprite.CHERRY));
        icFruit.addImage(Consts.Sprite.STRAWBERRY, sprite.getImage(Consts.Sprite.STRAWBERRY));
        
        Fruit.addImages(icFruit);
        
        sprite.newImage(Consts.Sprite.WALL_HORIZONTAL, 0, 4);
        sprite.newImage(Consts.Sprite.WALL_VERTICAL, 1, 4);
        
        ImageCollection icWall = new ImageCollection();
        icWall.addImage(Consts.Sprite.WALL_HORIZONTAL, sprite.getImage(Consts.Sprite.WALL_HORIZONTAL));
        icWall.addImage(Consts.Sprite.WALL_VERTICAL, sprite.getImage(Consts.Sprite.WALL_VERTICAL));

        Wall.addImages(icWall);
        
        sprite.setDefaultParameters(48, 48, Consts.CELL_SIZE/48.0f);

        sprite.newImage(Consts.Sprite.POWER_PELLETS, 20, 0);
        sprite.newImage(Consts.Sprite.PACDOTS, 16, 0);
        
        ImageCollection icItems = new ImageCollection();
        icItems.addImage(Consts.Sprite.POWER_PELLETS, sprite.getImage(Consts.Sprite.POWER_PELLETS));
        icItems.addImage(Consts.Sprite.PACDOTS, sprite.getImage(Consts.Sprite.PACDOTS));
        
        Items.addImages(icItems);
        
        //Animação do PacMan:
        sprite.setDefaultParameters(96, 96, Consts.CELL_SIZE/96.0f);
        sprite.newImage(Consts.Sprite.PACMAN_LEFT_0, 2, 3);
        sprite.newImage(Consts.Sprite.PACMAN_LEFT_1, 0, 3);
        sprite.newImage(Consts.Sprite.PACMAN_TOP_0, 3, 3);
        sprite.newImage(Consts.Sprite.PACMAN_TOP_1, 1, 3);
        sprite.newImage(Consts.Sprite.PACMAN_RIGHT_0, 6, 3);
        sprite.newImage(Consts.Sprite.PACMAN_RIGHT_1, 4, 3);
        sprite.newImage(Consts.Sprite.PACMAN_BOTTOM_0, 7, 3);
        sprite.newImage(Consts.Sprite.PACMAN_BOTTOM_1, 5, 3);
        sprite.newImage(Consts.Sprite.PACMAN_CLOSE, 0, 7);
        
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
    
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            pacman.setMovDirection(PacMan.MOVE_UP);
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            pacman.setMovDirection(PacMan.MOVE_DOWN);
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            pacman.setMovDirection(PacMan.MOVE_LEFT);
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            pacman.setMovDirection(PacMan.MOVE_RIGHT);
        } else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            pacman.setMovDirection(PacMan.STOP);
        }
    }

}
