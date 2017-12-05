package control;

import elements.Element;
import elements.Items;
import elements.PacMan;
import elements.phanton.Phanton;
import java.awt.Graphics;
import java.util.ArrayList;

/**
 * Projeto de POO 2017
 * 
 * @author Luiz Eduardo
 * Baseado em material do Prof. Jose Fernando Junior
 */
public class GameController {
    public void drawAllElements(ArrayList<Element> elemArray, Graphics g){
        for(int i=0; i<elemArray.size(); i++){
            elemArray.get(i).autoDraw(g);
        }
    }
    public void processAllElements(ArrayList<Element> e){
        if(e.isEmpty())
            return;
        
        PacMan pacman = (PacMan)e.get(0);
        pacman.move();

        if (!isValidPosition(e, pacman)) {
            pacman.backToLastPosition();
            pacman.setMovDirection(PacMan.STOP);
            return;
        }
        
        Element eTemp;
        for(int i = 1; i < e.size(); i++){
            eTemp = e.get(i);
            if(pacman.overlap(eTemp)) {
                if(eTemp instanceof Items) {
                    // TODO
                } else if(eTemp instanceof Phanton) {
                    if(((Phanton) eTemp).state() == Phanton.State.DEADLY) {
                        // TODO
                    } else {
                        // TODO
                    }
                } else {
                    if(eTemp.isTransposable()) {
                        pacman.winPoints(eTemp.getScore());
                        e.remove(eTemp);
                        i--;
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
                if(elemAux.overlap(elem))
                    return false;
        }        
        return true;
    }
}
