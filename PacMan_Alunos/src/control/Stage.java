package control;

import elements.*;
import elements.phanton.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import utils.Animation;
import utils.Consts;
import utils.ImageCollection;
import utils.Sprite;

public class Stage {
    private final PacMan pacman;
    private final ArrayList<Phanton> phantons;
    private final ArrayList<Fruit> fruits;
    private final ArrayList<Items> pacDots;
    private final ArrayList<Items> powerPellets;
    private final ArrayList<Items> walls;
    
    private Sprite sprite;
    private HashMap<Consts.Animation, Animation> animations;
    private HashMap<Consts.ImgCollection, ImageCollection> imgCollections;
    
    public Stage() {
        loadImages();
        
        pacman = new PacMan(imgCollections.get(Consts.ImgCollection.PACMAN), Consts.Animation.PACMAN_RIGHT.ordinal());
        pacman.setPosition(Consts.HEADER_SIZE, 0);
        
        phantons = new ArrayList<>();
        fruits = new ArrayList<>();
        pacDots = new ArrayList<>();
        powerPellets = new ArrayList<>();
        walls = new ArrayList<>();
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
    
    private void loadImages() {
        sprite = new Sprite("sprite.png");
        animations = new HashMap<>();
        imgCollections = new HashMap<>();
        
        sprite.setDefaultParameters(96, 96, 0.5f*Consts.CELL_SIZE/48.0f);
        
        sprite.newImage(Consts.Sprite.PACMAN_LEFT_0, 192, 288);
        sprite.newImage(Consts.Sprite.PACMAN_LEFT_1, 0, 288);
        sprite.newImage(Consts.Sprite.PACMAN_TOP_0, 288, 288);
        sprite.newImage(Consts.Sprite.PACMAN_TOP_1, 96, 288);
        sprite.newImage(Consts.Sprite.PACMAN_RIGHT_0, 576, 288);
        sprite.newImage(Consts.Sprite.PACMAN_RIGHT_1, 384, 288);
        sprite.newImage(Consts.Sprite.PACMAN_BOTTOM_0, 672, 288);
        sprite.newImage(Consts.Sprite.PACMAN_BOTTOM_1, 480, 288);
        sprite.newImage(Consts.Sprite.PACMAN_CLOSE, 0, 672);
        
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
