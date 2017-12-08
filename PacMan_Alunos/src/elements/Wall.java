package elements;

import java.awt.Graphics;
import javax.swing.ImageIcon;
import utils.Drawing;
import java.io.Serializable;

public class Wall extends Element implements Serializable {
    public Wall(ImageIcon image) {
        super(image);

        this.isTransposable = false;
        this.imageIcon = image;
    }
    
    @Override
    public void autoDraw(Graphics g) {
        Drawing.draw(g, this.imageIcon, pos.getX(), pos.getY());
    }
    
}
