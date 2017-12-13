package control;

import elements.Element;
import elements.Items;
import elements.PacMan;
import elements.Wall;
import elements.phantom.Blinky;
import elements.phantom.Phantom;
import elements.phantom.Phantom.State;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Projeto de POO 2017
 * 
 * @author Luiz Eduardo
 * Baseado em material do Prof. Jose Fernando Junior
 */
public class GameController {
    private Stage stage;
    
    public void addStage(Stage stage) {
        this.stage = stage;
    }
    private static final  WorldMap wm = WorldMap.getInstance();
    
    public void drawAllElements(ArrayList<Element> elemArray, Graphics g){
        for(int i=elemArray.size()-1; i >= 0; i--){
            elemArray.get(i).autoDraw(g);
        }
    }
    
    public void processAllElements(ArrayList<Element> e){
        if(e.isEmpty())
            return;
        
        Element eTemp;
        Iterator<Element> it = e.iterator();

        // primeiro elemento da lista
        PacMan pacman = (PacMan) it.next();
        pacman.move();
        
        // seta as posições no worldMap
        wm.setPacManPosition(pacman.getPosition());
        wm.setPacManDirection(pacman.getMovimentDirection());
        
        // checa colisão do pacman
        if (!isValidPosition(e, pacman)) {
            pacman.backToLastPosition();
            pacman.setMovDirection(PacMan.STOP);
        }
        
        while(it.hasNext()) {
            eTemp = it.next();
            
            if(eTemp instanceof Blinky) {
                // seta posição no world map
                wm.setBlinkyPosition(eTemp.getPosition());
            }
            
            // erro da colisão
            float err = 0.3f;
            if(eTemp instanceof Wall) {
                // incrementa o erro no caso de parede para evitar overlap
                err = 1.0f;
            } else if(eTemp instanceof Phantom) {
                // move fantasma e checa colisão
                Phantom p = (Phantom) eTemp;
                p.move();
                if (!isValidPosition(e, p) && p.getState() != State.EYE) {
                    p.backToLastPosition();
                    p.setForbiddenDirection(p.getMovDirection());
                }
            }
            
            if(pacman.overlap(eTemp, err)) {
                stage.overlapListener(eTemp);
                if(eTemp instanceof Phantom) {
                    Phantom p = (Phantom) eTemp;
                    if(p.state() == Phantom.State.DEADLY) {
                        pacman.die();
                        
                        stage.setState(Stage.State.DYING_PAUSE);
                        
                        // fantasma comestível
                    } else if(p.state() == Phantom.State.EDIBLE || p.state() == Phantom.State.ENDING_EDIBLE) {
                        pacman.winPoints(p.getScore());
                        
                        p.setState(Phantom.State.EYE);
                    }
                } else {
                    // itens
                    if(eTemp.isMortal()) {
                        pacman.winPoints(eTemp.getScore());
                        it.remove();
                        stage.removeElement(eTemp);
                    }
                }
            }
        }
    }
    public boolean isValidPosition(ArrayList<Element> elemArray, Element elem){
        Element elemAux;
        for(int i = 0; i < elemArray.size(); i++){
            elemAux = elemArray.get(i);
            if(elemAux == elem || elemAux instanceof PacMan) {
                continue;
            }
            if(!elemAux.isTransposable())
                if(elemAux.overlap(elem, 1.0f))
                    return false;
        }        
        return true;
    }
}
