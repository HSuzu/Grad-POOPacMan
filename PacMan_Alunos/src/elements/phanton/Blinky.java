package elements.phanton;

import utils.Position;
import javax.swing.ImageIcon;

public class Blinky extends Phanton {

    public Blinky(String imageName) {
        super(imageName);
    }
    
    public Blinky(ImageIcon image) {
        super(image);
    }

    @Override
    protected void navigation(Position PacManPosition) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
