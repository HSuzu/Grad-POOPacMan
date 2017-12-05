package elements.phanton;

import utils.Position;
import javax.swing.ImageIcon;

public class Blinky extends Phanton {

    public Blinky(String imageName, int value) {
        super(imageName, value);
    }
    
    public Blinky(ImageIcon image, int value) {
        super(image, value);
    }
    
    @Override
    public String name() {
        return "Blinky";
    }

    @Override
    protected void navigation(Position PacManPosition) {
        
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
