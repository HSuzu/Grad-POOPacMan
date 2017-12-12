package control;

import elements.Element;
import elements.Items;
import elements.PacMan;
import elements.Wall;
import elements.phanton.Phantom;
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
        
        PacMan pacman = (PacMan)e.get(0);
        pacman.move();

        wm.setPacManPosition(pacman.getPosition());
        wm.setPacManDirection(pacman.getMovimentDirection());
        
        if (!isValidPosition(e, pacman)) {
            pacman.backToLastPosition();
            pacman.setMovDirection(PacMan.STOP);
            return;
        }
        
        Element eTemp;
        Iterator<Element> it = e.iterator();
        it.next(); // remove pacman
        
        while(it.hasNext()) {
            eTemp = it.next();
            
            float err = 0.3f;
            if(eTemp instanceof Wall) {
                err = 1.0f;
            } else if(eTemp instanceof Phantom) {
                ((Phantom) eTemp).move();
            }
            
            if(pacman.overlap(eTemp, err)) {
                stage.overlapListener(eTemp);
                if(eTemp instanceof Phantom) {
                    if(((Phantom) eTemp).state() == Phantom.State.DEADLY) {
                        // TODO
                        pacman.die();
                        pacman.setPosition(7.0, 3.0);
                        eTemp.setPosition(1.0, 1.0);
                    } else {
                        pacman.winPoints(eTemp.getScore());
                        it.remove();
                        stage.removeElement(eTemp);
                    }
                } else {
                    if(eTemp.isTransposable()) {
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
        for(int i = 1; i < elemArray.size(); i++){
            elemAux = elemArray.get(i);            
            if(!elemAux.isTransposable())
                if(elemAux.overlap(elem, 1.0f))
                    return false;
        }        
        return true;
    }
}
