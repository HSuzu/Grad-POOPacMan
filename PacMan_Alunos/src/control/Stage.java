package control;

import elements.*;
import elements.phanton.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.font.TextAttribute;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.AttributedString;
import java.util.ArrayList;
import java.util.HashMap;
import javax.sound.sampled.LineUnavailableException;
import utils.Animation;
import utils.AudioControl;
import utils.Consts;
import utils.ImageCollection;
import utils.Sprite;

public class Stage extends KeyAdapter {
    private BackgroundElement bkElem;
    
    AudioControl audio;
    
    private PacMan pacman;
    private ArrayList<Phantom> phantoms;
    private ArrayList<Fruit> fruits;
    private ArrayList<Items> pacDots;
    private ArrayList<Items> powerPellets;
    private ArrayList<Wall> walls;
    
    ArrayList<Element> elem;
    
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
        
        phantoms = new ArrayList<>();
        Pinky blinky = new Pinky(sprite.getImage(Consts.Sprite.CHERRY), 100);
        blinky.setPosition(11.0, 8.0);
        phantoms.add(blinky);
        fruits = new ArrayList<>();
        Fruit cherry = new Fruit(sprite.getImage(Consts.Sprite.CHERRY), "Cherry", 100, 20000);
        cherry.setPosition(10.0, 10.0);
        fruits.add(cherry);
                        
        pacDots = new ArrayList<>();
        powerPellets = new ArrayList<>();
        walls = new ArrayList<>();
        
        elem = new ArrayList<>();
        elem.add(pacman);
        elem.addAll(phantoms);
        elem.addAll(pacDots);
        elem.addAll(powerPellets);
        elem.addAll(fruits);
        elem.addAll(walls);
        
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
        elem.clear();
        elem.add(pacman);
        elem.addAll(phantoms);
        elem.addAll(pacDots);
        elem.addAll(powerPellets);
        elem.addAll(fruits);
        elem.addAll(walls);
        return elem;
    }
    
    public void removeElement(Element e) {
        if(e instanceof Phantom) {
            phantoms.remove(e);
        } else if(e instanceof Fruit) {
            fruits.remove(e);
        } else if(e instanceof Items) {
            if(!pacDots.remove(e)) {
                powerPellets.remove(e);
            }
        } else if(e instanceof Wall) {
            walls.remove(e);
        }
    }
    
    public void saveStage(String file) {
        ObjectOutputStream stream = null;
        
        FileOutputStream f = null;
        try {
            f = new FileOutputStream(file);
        } catch(FileNotFoundException e) {
            System.out.println("Creating new save file.");
        } finally {
            try {
                stream = new ObjectOutputStream(f);

                stream.writeObject(WorldMap.getInstance());
                stream.writeObject(pacman);
                stream.writeObject(phantoms);
                stream.writeObject(fruits);
                stream.writeObject(pacDots);
                stream.writeObject(powerPellets);
                stream.writeObject(walls);
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }
    
    public void loadStage(String file) throws FileNotFoundException {
        ObjectInputStream stream = null;
        
        try {
            stream = new ObjectInputStream(new FileInputStream(file));

            phantoms.clear();
            fruits.clear();
            pacDots.clear();
            powerPellets.clear();
            walls.clear();
            
            WorldMap.getInstance().loadWorldMap((WorldMap) stream.readObject());
            pacman = (PacMan) stream.readObject();
            phantoms = (ArrayList) stream.readObject();
            fruits = (ArrayList) stream.readObject();
            pacDots = (ArrayList) stream.readObject();
            powerPellets = (ArrayList) stream.readObject();
            walls = (ArrayList) stream.readObject();
        } catch (IOException | ClassNotFoundException ex) {
            System.out.println(ex.getMessage());
        } finally {
            resetAnimation();
            elem.clear();
            elem.add(pacman);
            elem.addAll(phantoms);
            elem.addAll(pacDots);
            elem.addAll(powerPellets);
            elem.addAll(fruits);
            elem.addAll(walls);
        }
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
    
    private void resetAnimation() {
        ImageCollection icPacMan = imgCollections.get(Consts.ImgCollection.PACMAN);
        pacman.setImageCollection(icPacMan);
        pacman.resetMovDirection();
        switch(pacman.getMovimentDirection()) {
            case PacMan.MOVE_DOWN:
                pacman.setImage(icPacMan.getImage(Consts.Animation.PACMAN_DOWN));
            break;
            case PacMan.MOVE_UP:
                pacman.setImage(icPacMan.getImage(Consts.Animation.PACMAN_UP));
            break;
            case PacMan.MOVE_LEFT:
                pacman.setImage(icPacMan.getImage(Consts.Animation.PACMAN_LEFT));
            break;
            case PacMan.MOVE_RIGHT:
                pacman.setImage(icPacMan.getImage(Consts.Animation.PACMAN_RIGHT));
            break;
            default:
                pacman.setImage(sprite.getImage(Consts.Sprite.PACMAN_CLOSE));
            break;
        }
        
        for(Phantom p : phantoms) {
            switch(p.name()) {
                case "Clyde":
                    p.setImage(sprite.getImage(Consts.Sprite.CHERRY));
                break;
                case "Pinky":
                    p.setImage(sprite.getImage(Consts.Sprite.CHERRY));
                break;
                case "Inky":
                    p.setImage(sprite.getImage(Consts.Sprite.CHERRY));
                break;
                case "Blinky":
                    p.setImage(sprite.getImage(Consts.Sprite.CHERRY));
                break;
                default:
                    p.setImage(sprite.getImage(Consts.Sprite.CHERRY));
                break;
            }
        }
        
        for(Fruit f : fruits) {
            switch(f.getName()) {
                case "Cherry":
                    f.setImage(sprite.getImage(Consts.Sprite.CHERRY));
                break;
                default:
                    f.setImage(sprite.getImage(Consts.Sprite.CHERRY));
                break;
            }
        }
        
        for(Items p : pacDots) {
            p.setImage(sprite.getImage(Consts.Sprite.PACDOTS));
        }
        
        for(Items p : powerPellets) {
            p.setImage(sprite.getImage(Consts.Sprite.POWER_PELLETS));
        }
        
        for(Wall w : walls) {
            w.setImage(sprite.getImage(Consts.Sprite.WALL_HORIZONTAL));
        }
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
        
        Animation anLeft = new Animation(50);
        anLeft.addImage(sprite.getImage(Consts.Sprite.PACMAN_LEFT_1));
        anLeft.addImage(sprite.getImage(Consts.Sprite.PACMAN_LEFT_0));
        anLeft.addImage(sprite.getImage(Consts.Sprite.PACMAN_CLOSE));
        anLeft.addImage(sprite.getImage(Consts.Sprite.PACMAN_LEFT_0));

        animations.put(Consts.Animation.PACMAN_LEFT, anLeft);
        
        Animation anRight = new Animation(50);
        anRight.addImage(sprite.getImage(Consts.Sprite.PACMAN_RIGHT_1));
        anRight.addImage(sprite.getImage(Consts.Sprite.PACMAN_RIGHT_0));
        anRight.addImage(sprite.getImage(Consts.Sprite.PACMAN_CLOSE));
        anRight.addImage(sprite.getImage(Consts.Sprite.PACMAN_RIGHT_0));

        animations.put(Consts.Animation.PACMAN_RIGHT, anRight);
        
        Animation anTop = new Animation(50);
        anTop.addImage(sprite.getImage(Consts.Sprite.PACMAN_TOP_1));
        anTop.addImage(sprite.getImage(Consts.Sprite.PACMAN_TOP_0));
        anTop.addImage(sprite.getImage(Consts.Sprite.PACMAN_CLOSE));
        anTop.addImage(sprite.getImage(Consts.Sprite.PACMAN_TOP_0));

        animations.put(Consts.Animation.PACMAN_UP, anTop);
        
        Animation anBottom = new Animation(50);
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
