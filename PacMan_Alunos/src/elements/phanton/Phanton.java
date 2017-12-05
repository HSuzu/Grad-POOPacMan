package elements.phanton;

import elements.Element;
import java.awt.Graphics;
import javax.swing.ImageIcon;
import utils.Drawing;
import utils.Position;

public abstract class Phanton extends Element {
    
    public Phanton(String imageName, int value) {
        super(imageName);
        this.isMortal = true;
        this.isTransposable = false;
        this.setScore(value);
    }
    
    public Phanton(ImageIcon image, int value) {
        super(image);
        this.setScore(value);
    }
    
    abstract public String name();
    
    abstract protected void navigation(Position PacManPosition);

    @Override
    public void autoDraw(Graphics g) {
        Drawing.draw(g, this.imageIcon, pos.getY(), pos.getX());
    }
    
}
