package elements.phanton;

import elements.Element;
import java.awt.Graphics;
import javax.swing.ImageIcon;
import utils.Drawing;
import utils.Position;

public abstract class Phanton extends Element {
    public static enum State {
        DEADLY,
        EDIBLE
    }
    
    private State state;

    public Phanton(String imageName, int value) {
        super(imageName);
        
        this.isTransposable = false;
        this.setScore(value);
        state = State.DEADLY;
    }
    
    public Phanton(ImageIcon image, int value) {
        super(image);
        
        this.setScore(value);
        state = State.DEADLY;
    }
    
    public void setState(State state) {
        this.state = state;
    }
    
    public State state() {
        return state;
    }
    
    abstract public String name();
    
    abstract protected void navigation(Position PacManPosition);

    @Override
    public void autoDraw(Graphics g) {
        Drawing.draw(g, this.imageIcon, pos.getY(), pos.getX());
    }
    
}
