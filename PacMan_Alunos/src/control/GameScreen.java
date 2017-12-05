package control;

import elements.Skull;
import elements.Lolo;
import elements.Element;
import utils.Consts;
import utils.Drawing;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import utils.Animation;
import utils.ImageCollection;
import utils.Sprite;

/**
 * Projeto de POO 2017
 * 
 * @author Luiz Eduardo
 * Baseado em material do Prof. Jose Fernando Junior
 */
public class GameScreen extends javax.swing.JFrame implements KeyListener {
    
    private final Lolo lolo;
    private final ArrayList<Element> elemArray;
    private final GameController controller = new GameController();
    private final Sprite sp;
    private final Animation an;
    private final ImageCollection ic;

    public GameScreen() {
        Drawing.setGameScreen(this);
        initComponents();
        
        this.addKeyListener(this);   /*teclado*/
        
        /*Cria a janela do tamanho do tabuleiro + insets (bordas) da janela*/
        this.setSize(Consts.NUM_CELLS * Consts.CELL_SIZE + getInsets().left + getInsets().right,
                     Consts.NUM_CELLS * Consts.CELL_SIZE + getInsets().top + getInsets().bottom);

        elemArray = new ArrayList<Element>();
        
        sp = new Sprite("sprite.png");
        sp.newImage(Consts.Animation.PacMan.LEFT_0.ordinal(), 192, 288, 96, 96, 0.5f*Consts.CELL_SIZE/48.0f);
        sp.newImage(Consts.Animation.PacMan.LEFT_1.ordinal(), 0, 288, 96, 96, 0.5f*Consts.CELL_SIZE/48.0f);
        sp.newImage(Consts.Animation.PacMan.TOP_0.ordinal(), 288, 288, 96, 96, 0.5f*Consts.CELL_SIZE/48.0f);
        sp.newImage(Consts.Animation.PacMan.TOP_1.ordinal(), 96, 288, 96, 96, 0.5f*Consts.CELL_SIZE/48.0f);
        sp.newImage(Consts.Animation.PacMan.RIGHT_0.ordinal(), 576, 288, 96, 96, 0.5f*Consts.CELL_SIZE/48.0f);
        sp.newImage(Consts.Animation.PacMan.RIGHT_1.ordinal(), 384, 288, 96, 96, 0.5f*Consts.CELL_SIZE/48.0f);
        sp.newImage(Consts.Animation.PacMan.BOTTOM_0.ordinal(), 672, 288, 96, 96, 0.5f*Consts.CELL_SIZE/48.0f);
        sp.newImage(Consts.Animation.PacMan.BOTTOM_1.ordinal(), 480, 288, 96, 96, 0.5f*Consts.CELL_SIZE/48.0f);
        sp.newImage(Consts.Animation.PacMan.CLOSE.ordinal(), 0, 672, 96, 96, 0.5f*Consts.CELL_SIZE/48.0f);
        
        an = new Animation(175);
        an.addImage(sp.getImage(Consts.Animation.PacMan.LEFT_1.ordinal()));
        an.addImage(sp.getImage(Consts.Animation.PacMan.LEFT_0.ordinal()));
        an.addImage(sp.getImage(Consts.Animation.PacMan.CLOSE.ordinal()));
        an.addImage(sp.getImage(Consts.Animation.PacMan.LEFT_0.ordinal()));
        an.start();
        
        ic = new ImageCollection();
        ic.addAnimation(0, an);
        ic.addImage(1, sp.getImage(Consts.Animation.PacMan.TOP_1.ordinal()));
        ic.addImage(2, sp.getImage(Consts.Animation.PacMan.RIGHT_1.ordinal()));
        ic.addImage(3, sp.getImage(Consts.Animation.PacMan.BOTTOM_1.ordinal()));

        /*Cria e adiciona elementos*/
        lolo = new Lolo(ic, 2);
        lolo.setPosition(0, 0);
        this.addElement(lolo);
        
        Skull skull = new Skull("caveira.png");
        skull.setPosition(9, 1);
        this.addElement(skull);  
    }
    
    public final void addElement(Element elem) {
        elemArray.add(elem);
    }
    
    public void removeElement(Element elem) {
        elemArray.remove(elem);
    }
    
    @Override
    public void paint(Graphics gOld) {
        Graphics g = getBufferStrategy().getDrawGraphics();
        
        /*Criamos um contexto grafico*/
        Graphics g2 = g.create(getInsets().right, getInsets().top, getWidth() - getInsets().left, getHeight() - getInsets().bottom);
        
        /* DESENHA CENARIO
           Trocar essa parte por uma estrutura mais bem organizada
           Utilizando a classe Stage
        */
        for (int i = 0; i < Consts.NUM_CELLS; i++) {
            for (int j = 0; j < Consts.NUM_CELLS; j++) {
                try {
                    Image newImage = Toolkit.getDefaultToolkit().getImage(new java.io.File(".").getCanonicalPath() + Consts.PATH + "bricks.png");
                    g2.drawImage(newImage,
                            j * Consts.CELL_SIZE, i * Consts.CELL_SIZE, Consts.CELL_SIZE, Consts.CELL_SIZE, null);
                    
                } catch (IOException ex) {
                    Logger.getLogger(GameScreen.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        
        this.controller.drawAllElements(elemArray, g2);
        this.controller.processAllElements(elemArray);
        this.setTitle("-> Cell: " + lolo.getStringPosition());
        
        g.dispose();
        g2.dispose();
        if (!getBufferStrategy().contentsLost()) {
            getBufferStrategy().show();
        }
    }
    
    public void go() {
        TimerTask task = new TimerTask() {
            
            public void run() {
                repaint();
            }
        };
        Timer timer = new Timer();
        timer.schedule(task, 0, Consts.DELAY);
    }
    
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            lolo.setMovDirection(Lolo.MOVE_UP);
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            lolo.setMovDirection(Lolo.MOVE_DOWN);
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            lolo.setMovDirection(Lolo.MOVE_LEFT);
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            lolo.setMovDirection(Lolo.MOVE_RIGHT);
        } else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            lolo.setMovDirection(Lolo.STOP);
        }
        
        //repaint(); /*invoca o paint imediatamente, sem aguardar o refresh*/
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("SCC0604 - Pacman");
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setLocation(new java.awt.Point(20, 20));
        setResizable(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 500, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 500, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
    
    @Override
    public void keyTyped(KeyEvent e) {
    }
    
    @Override
    public void keyReleased(KeyEvent e) {
    }
}
