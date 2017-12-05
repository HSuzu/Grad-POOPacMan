package elements;

import java.awt.Graphics;
import javax.swing.ImageIcon;

public class Wall extends Element {
    public static enum Type {
        I
    }
    
    Type type;

    public Wall(Type type) {
        super();
        
        this.isTransposable = false;
        this.type = type;
        this.imageIcon = new ImageIcon();
    }
    
    public Wall(ImageIcon image) {
        super(image);
        type = Type.I;
    }
    
    public Type getType() {
        return type;
    }
    
    public void setImage(ImageIcon image) {
        this.imageIcon = image;
    }

    @Override
    public void autoDraw(Graphics g) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
