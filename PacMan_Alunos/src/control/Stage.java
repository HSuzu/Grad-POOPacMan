package control;

import elements.phantom.Blinky;
import elements.phantom.Pinky;
import elements.phantom.Phantom;
import elements.phantom.Inky;
import elements.phantom.Clyde;
import elements.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.font.TextAttribute;
import java.io.BufferedInputStream;
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
import utils.Position;
import utils.Sprite;
import java.util.Timer;
import java.util.TimerTask;

public class Stage extends KeyAdapter {
    private BackgroundElement bkElem;
    private Timer timerCherry;
    private Timer timerStrawberry;
    private Timer timerTransition;
    private String transitionTxt = "";
    
    AudioControl audio;
    AudioControl audioBackground;
    Timer bkAudioTimer;
    
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
    
    public enum State {
        GAME_ON,
        GAME_OVER,
        DYING_PAUSE,
        TRANSITION
    }
    
    private State state = State.GAME_ON;
    
    private byte stage = 1;
    
    public State getState() {
        return state;
    }
    public void setState(State state) {
        this.state = state;
        
        if(state == State.DYING_PAUSE) {
            audioBackground.setNext("sound" + File.separator + "pacman_death.wav");
            audioBackground.start(false, false);
            

            bkAudioTimer = new Timer();
            bkAudioTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    audioBackground.stop();
                    
                    for(Phantom p : phantoms) {
                        p.resetPosition();
                    }
                    
                    if(pacman.getNumLifes() <= 0) {
                        setState(State.GAME_OVER);
                    } else {
                        setState(State.GAME_ON);
                    }
                }
            }, 3000);

        }
    };
        
    public Stage() {
        loadImages();
        
        try {
            audio = new AudioControl();
        } catch (LineUnavailableException ex) {
            System.out.println(ex.getMessage());
        }

        try {
            audioBackground = new AudioControl();
        } catch (LineUnavailableException ex) {
            System.out.println(ex.getMessage());
        }
        
        bkElem = new BackgroundElement();
        
        pacman = new PacMan(imgCollections.get(Consts.ImgCollection.PACMAN), Consts.Animation.PACMAN_RIGHT.ordinal());
        pacman.setPosition(0, 0);
        
        phantoms = new ArrayList<>();
        Blinky blinky = new Blinky(imgCollections.get(Consts.ImgCollection.BLINKY), Consts.Animation.BLINKY_RIGHT.ordinal(), 200);
        blinky.setPosition(11.0, 8.0);
        phantoms.add(blinky);
        
        Inky inky = new Inky(imgCollections.get(Consts.ImgCollection.INKY), Consts.Animation.INKY_RIGHT.ordinal(), 200);
        inky.setPosition(5.0, 6.0);
        phantoms.add(inky);
        
        Pinky pinky = new Pinky(imgCollections.get(Consts.ImgCollection.PINKY), Consts.Animation.PINKY_RIGHT.ordinal(), 200);
        pinky.setPosition(3.0, 9.0);
        phantoms.add(pinky);        
        
        Clyde clyde = new Clyde(imgCollections.get(Consts.ImgCollection.CLYDE), Consts.Animation.CLYDE_RIGHT.ordinal(), 200);
        clyde.setPosition(2.0, 11.0);
        phantoms.add(clyde);
        
        fruits = new ArrayList<>();
                        
        pacDots = new ArrayList<>();
        powerPellets = new ArrayList<>();
        walls = new ArrayList<>();
        
        updateMapElements();

        elem = new ArrayList<>();
        
        try {
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("fonts/emulogic.ttf")));
            font = new Font("emulogic", Font.PLAIN, 18);
        } catch (IOException | FontFormatException ex) {
            System.out.println(ex.getMessage());
        }
        
        timerStrawberry.schedule(new TimerTask() {
            @Override
            public void run() {
                createStrawberry();
            }
        }, Consts.STRAWBERRY_TIME);
    }
    
    private void updateMapElements() {
        char[][] map = WorldMap.getInstance().getMap();
        
        powerPellets.clear();
        pacDots.clear();
        walls.clear();
        
        int i, j;
        for(i = 0; i < Consts.NUM_CELLS_X; i++) {
            for(j = 0; j < Consts.NUM_CELLS_Y; j++) {
                switch(map[i][j]) {
                    case 'p': {
                        pacman.setDefaultPosition(new Position(i, j));
                        pacman.setPosition(i, j);
                    } break;
                    case 'o': {
                        Items powerPellet = new Items(sprite.getImage(Consts.Sprite.POWER_PELLETS), "PowerPellet", 100);
                        powerPellet.setPosition(i, j);
                        powerPellets.add(powerPellet);
                    } break;
                    case '.': {
                        Items pacDot = new Items(sprite.getImage(Consts.Sprite.PACDOTS), "PacDot", 100);
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
        
        timerCherry = new Timer();
                timerCherry.schedule(new TimerTask() {
                @Override
                public void run() {
                    createCherry();
                }
            }, Consts.CHERRY_TIME);
        timerStrawberry = new Timer();
            timerStrawberry.schedule(new TimerTask() {
            @Override
            public void run() {
                createStrawberry();
            }
        }, Consts.STRAWBERRY_TIME);
    }
    
    public ArrayList<Element> getAllElements() {
        elem.clear();
        elem.add(pacman);
        elem.addAll(phantoms);
        elem.addAll(pacDots);
        elem.addAll(fruits);
        elem.addAll(powerPellets);
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

                f.write(stage);
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
        FileInputStream f = null;
        try {
            f = new FileInputStream(file);
            stream = new ObjectInputStream(f);

            phantoms.clear();
            fruits.clear();
            pacDots.clear();
            powerPellets.clear();
            walls.clear();
            
            stage = (byte) f.read();
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
            for(Phantom p : phantoms) {
                p.setState(Phantom.State.DEADLY);
            }
            
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
        //Mostrar fase:
        word = new AttributedString("Stage: "+stage);
        word.addAttribute(TextAttribute.FONT, font);
        g.setColor(Color.green);
        g.drawString(word.getIterator(), (Consts.NUM_CELLS_X - 7)*Consts.CELL_SIZE, 2*Consts.CELL_SIZE);
    }
    
    public void drawGameOver(Graphics g) {
        drawString(g, "GAME OVER", Color.RED);
    }
        
    public void drawString(Graphics g, String txt, Color color) {
        FontMetrics metrics = g.getFontMetrics(font);
        
        int height = metrics.getHeight();
        int width = metrics.stringWidth(txt);
        
        int x = (Consts.NUM_CELLS_X*Consts.CELL_SIZE - width)/2;
        int y = (Consts.NUM_CELLS_Y*Consts.CELL_SIZE - height)/2;
        
        g.setColor(Color.decode("#222222"));
        g.fillRect(x - 10, y - height - 10, width + 20, height + 30);
        
        g.setFont(font);
        g.setColor(color);
        
        g.drawString(txt, x, y);
    }
    
    public String getTransitionTxt() {
        return transitionTxt;
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
        
        ImageCollection icPhanton;
        for(Phantom p : phantoms) {
            switch(p.name()) {
                case "Clyde":
                    icPhanton = imgCollections.get(Consts.ImgCollection.CLYDE);
                break;
                case "Pinky":
                    icPhanton = imgCollections.get(Consts.ImgCollection.PINKY);
                break;
                case "Inky":
                    icPhanton = imgCollections.get(Consts.ImgCollection.INKY);
                break;
                case "Blinky":
                    icPhanton = imgCollections.get(Consts.ImgCollection.BLINKY);
                    p.setImage(sprite.getImage(Consts.Sprite.CHERRY));
                break;
                default:
                    icPhanton = imgCollections.get(Consts.ImgCollection.BLINKY);
                break;
            }
            p.setImageCollection(icPhanton);
            p.setImage(p.getImage(p.getMovDirection()));
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
        
        sprite.newImage(Consts.Sprite.PACMAN_DYING_1, 3, 0);
        sprite.newImage(Consts.Sprite.PACMAN_DYING_2, 4, 0);
        sprite.newImage(Consts.Sprite.PACMAN_DYING_3, 5, 0);
        sprite.newImage(Consts.Sprite.PACMAN_DYING_4, 6, 0);
        sprite.newImage(Consts.Sprite.PACMAN_DYING_5, 7, 0);
        sprite.newImage(Consts.Sprite.PACMAN_DYING_6, 8, 0);
        sprite.newImage(Consts.Sprite.PACMAN_DYING_7, 9, 0);
        sprite.newImage(Consts.Sprite.PACMAN_DYING_8, 10, 0);
        sprite.newImage(Consts.Sprite.PACMAN_DYING_9, 11, 0);
        sprite.newImage(Consts.Sprite.PACMAN_DYING_10, 12, 0);
        sprite.newImage(Consts.Sprite.PACMAN_DYING_11, 13, 0);
        
        Animation dying = new Animation(Consts.ANIMATION_DELAY);
        dying.addImage(sprite.getImage(Consts.Sprite.PACMAN_DYING_1));
        dying.addImage(sprite.getImage(Consts.Sprite.PACMAN_DYING_2));
        dying.addImage(sprite.getImage(Consts.Sprite.PACMAN_DYING_3));
        dying.addImage(sprite.getImage(Consts.Sprite.PACMAN_DYING_4));
        dying.addImage(sprite.getImage(Consts.Sprite.PACMAN_DYING_5));
        dying.addImage(sprite.getImage(Consts.Sprite.PACMAN_DYING_6));
        dying.addImage(sprite.getImage(Consts.Sprite.PACMAN_DYING_7));
        dying.addImage(sprite.getImage(Consts.Sprite.PACMAN_DYING_8));
        dying.addImage(sprite.getImage(Consts.Sprite.PACMAN_DYING_9));
        dying.addImage(sprite.getImage(Consts.Sprite.PACMAN_DYING_10));
        dying.addImage(sprite.getImage(Consts.Sprite.PACMAN_DYING_11));
        
        animations.put(Consts.Animation.PACMAN_DYING, dying);
        
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
        icPacman.addAnimation(Consts.Animation.PACMAN_DYING, dying);
        icPacman.addAnimation(Consts.Animation.PACMAN_LEFT, anLeft);
        icPacman.addAnimation(Consts.Animation.PACMAN_UP, anTop);
        icPacman.addAnimation(Consts.Animation.PACMAN_RIGHT, anRight);
        icPacman.addAnimation(Consts.Animation.PACMAN_DOWN, anBottom);
        
        imgCollections.put(Consts.ImgCollection.PACMAN, icPacman);
        
        //Animação fantasma comestível:
        sprite.newImage(Consts.Sprite.PHANTOM_BLUE_0, 8, 4);
        sprite.newImage(Consts.Sprite.PHANTOM_BLUE_1, 9, 4);
        sprite.newImage(Consts.Sprite.PHANTOM_WHITE_0, 10, 4);
        sprite.newImage(Consts.Sprite.PHANTOM_WHITE_1, 11, 4);
        
        Animation phantomEdible = new Animation(Consts.ANIMATION_DELAY);
        phantomEdible.addImage(sprite.getImage(Consts.Sprite.PHANTOM_BLUE_0));
        phantomEdible.addImage(sprite.getImage(Consts.Sprite.PHANTOM_BLUE_1));
        animations.put(Consts.Animation.EDIBLE, phantomEdible);
        
        Animation edibleEnding = new Animation(3*Consts.ANIMATION_DELAY);
        edibleEnding.addImage(sprite.getImage(Consts.Sprite.PHANTOM_BLUE_0));
        edibleEnding.addImage(sprite.getImage(Consts.Sprite.PHANTOM_WHITE_1));
        edibleEnding.addImage(sprite.getImage(Consts.Sprite.PHANTOM_BLUE_1));
        edibleEnding.addImage(sprite.getImage(Consts.Sprite.PHANTOM_WHITE_0));
        animations.put(Consts.Animation.EDIBLE_ENDING, edibleEnding);
        
        //Animação Blinky:
        sprite.newImage(Consts.Sprite.BLINKY_BOTTOM_0, 6, 4);
        sprite.newImage(Consts.Sprite.BLINKY_BOTTOM_1, 7, 4);
        sprite.newImage(Consts.Sprite.BLINKY_LEFT_0 , 2, 4);
        sprite.newImage(Consts.Sprite.BLINKY_LEFT_1 , 3, 4);
        sprite.newImage(Consts.Sprite.BLINKY_RIGHT_0 , 0, 4);
        sprite.newImage(Consts.Sprite.BLINKY_RIGHT_1 , 1, 4);
        sprite.newImage(Consts.Sprite.BLINKY_TOP_0 , 4, 4);
        sprite.newImage(Consts.Sprite.BLINKY_TOP_1 , 5, 4);
        
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
        icBlinky.addAnimation(Consts.Animation.EDIBLE, phantomEdible);
        icBlinky.addAnimation(Consts.Animation.EDIBLE_ENDING, edibleEnding);
        imgCollections.put(Consts.ImgCollection.BLINKY, icBlinky);

        //Animação Pinky:
        sprite.newImage(Consts.Sprite.PINKY_BOTTOM_0, 6, 5);
        sprite.newImage(Consts.Sprite.PINKY_BOTTOM_1, 7, 5);
        sprite.newImage(Consts.Sprite.PINKY_LEFT_0 , 2, 5);
        sprite.newImage(Consts.Sprite.PINKY_LEFT_1 , 3, 5);
        sprite.newImage(Consts.Sprite.PINKY_RIGHT_0 , 0, 5);
        sprite.newImage(Consts.Sprite.PINKY_RIGHT_1 , 1, 5);
        sprite.newImage(Consts.Sprite.PINKY_TOP_0 , 4, 5);
        sprite.newImage(Consts.Sprite.PINKY_TOP_1 , 5, 5);
        
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
        icPinky.addAnimation(Consts.Animation.EDIBLE, phantomEdible);
        icPinky.addAnimation(Consts.Animation.EDIBLE_ENDING, edibleEnding);
        imgCollections.put(Consts.ImgCollection.PINKY, icPinky);
        
        //Animação Inky:
        sprite.newImage(Consts.Sprite.INKY_BOTTOM_0, 6, 6);
        sprite.newImage(Consts.Sprite.INKY_BOTTOM_1, 7, 6);
        sprite.newImage(Consts.Sprite.INKY_LEFT_0 , 2, 6);
        sprite.newImage(Consts.Sprite.INKY_LEFT_1 , 3, 6);
        sprite.newImage(Consts.Sprite.INKY_RIGHT_0 , 0, 6);
        sprite.newImage(Consts.Sprite.INKY_RIGHT_1 , 1, 6);
        sprite.newImage(Consts.Sprite.INKY_TOP_0 , 4, 6);
        sprite.newImage(Consts.Sprite.INKY_TOP_1 , 5, 6);
        
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
        icInky.addAnimation(Consts.Animation.EDIBLE, phantomEdible);
        icInky.addAnimation(Consts.Animation.EDIBLE_ENDING, edibleEnding);
        imgCollections.put(Consts.ImgCollection.INKY, icInky);
        
        //Animação Clyde:
        sprite.newImage(Consts.Sprite.CLYDE_BOTTOM_0, 6, 7);
        sprite.newImage(Consts.Sprite.CLYDE_BOTTOM_1, 7, 7);
        sprite.newImage(Consts.Sprite.CLYDE_LEFT_0 , 2, 7);
        sprite.newImage(Consts.Sprite.CLYDE_LEFT_1 , 3, 7);
        sprite.newImage(Consts.Sprite.CLYDE_RIGHT_0 , 0, 7);
        sprite.newImage(Consts.Sprite.CLYDE_RIGHT_1 , 1, 7);
        sprite.newImage(Consts.Sprite.CLYDE_TOP_0 , 4, 7);
        sprite.newImage(Consts.Sprite.CLYDE_TOP_1 , 5, 7);
        
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
        icClyde.addAnimation(Consts.Animation.EDIBLE, phantomEdible);
        icClyde.addAnimation(Consts.Animation.EDIBLE_ENDING, edibleEnding);
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
            Items item = (Items) e;
            
            switch(item.getName()) {
                case "PacDot":
                break;
                case "PowerPellet":
                    for(Phantom p : phantoms) {
                        p.setState(Phantom.State.EDIBLE);
                    }
                    audioBackground.setNext("sound"+ File.separator + "pacman_intermission.wav");
                    audioBackground.start(false, true);
                    
                    if(bkAudioTimer != null) {
                        bkAudioTimer.cancel();
                    }
                    
                    bkAudioTimer = new Timer();
                    bkAudioTimer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            audioBackground.stop();
                        }
                    }, Consts.Timer.POWERPELLET_EFFECT + Consts.Timer.POWERPELLET_EFFECT_ENDIND);
                break;
                default:
                    audio.setNext("sound"+ File.separator + "pacman_eatfruit.wav");
                    audio.start(false, false);
                break;
            }
        } else if(e instanceof Phantom) {
            Phantom p = (Phantom) e;
            if(p.getState() != Phantom.State.DEADLY) {
                Phantom.increasePhantonCounter();
            }
        }
    }
    
    public void iterationListener() {
        if(pacDots.isEmpty() && powerPellets.isEmpty() && state == State.GAME_ON) {
            if(stage < 3) {
                try {
                    WorldMap.getInstance().loadFile("maps" + File.separator + "stage" + stage);
                    stage++;
                
                    updateMapElements();
                    
                    for(Phantom p : phantoms) {
                        p.setState(Phantom.State.DEADLY);
                    }
                    
                    state = State.TRANSITION;
                    transitionTxt = "Phase " + stage;
                    
                    timerTransition = new Timer();
            
                    timerTransition.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            state = State.GAME_ON;
                        }
                    }, 4000);


                } catch(IOException ex) {
                    state = State.TRANSITION;
                    transitionTxt = "You Win";

                    System.out.println(ex.getMessage());
                }
            }
        }
    }
        
    public void createCherry() {
        Fruit cherry = new Fruit(sprite.getImage(Consts.Sprite.CHERRY), "Cherry", 100, Consts.FRUIT_LIFE);
        fruits.add(cherry);
        elem.add(cherry);
        setTimer(timerCherry, Consts.CHERRY_TIME, "Cherry");
    }
    
    public void createStrawberry() {
        Fruit strawberry = new Fruit(sprite.getImage(Consts.Sprite.STRAWBERRY), "Strawberry", 300, Consts.FRUIT_LIFE);
        fruits.add(strawberry);
        elem.add(strawberry);
        setTimer(timerStrawberry, Consts.STRAWBERRY_TIME, "Strawberry");
    }
    
    public void setTimer(Timer timer, int time, String fruit) {
        timer = new Timer();
        if("Strawberry".equals(fruit)) {
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    createStrawberry();
                }
            }, time);
        }
        else if("Cherry".equals(fruit)) {
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    createCherry();
                }
            }, time);
        }
    }
}
