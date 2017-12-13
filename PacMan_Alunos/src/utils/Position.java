package utils;

import java.io.Serializable;

/**
 * Projeto de POO 2017
 * 
 * @author Luiz Eduardo
 * Baseado em material do Prof. Jose Fernando Junior
 */
public class Position implements Serializable {
    /* Elements are positioned in a grid layout (integers).
       However, walking is implemented with float steps (continuous).
       This is why x and y are double types.
       x and y ranges from 0 to CELL_SIZE*NUM_CELLS.
       The real pixel positioning is converted by the Drawing class.
       As consequence, any element has size 1x1 (x and y). */
    private double x;
    private double y;
    
    private double previousX;
    private double previousY;

    public Position(double x, double y){
        this.setPosition(x,y);
    }

    public final boolean setPosition(double x, double y){
        int factor = (int)Math.pow(10, Consts.WALK_STEP_DEC_PLACES+1);
        x = (double)Math.round(x * factor) / factor;
        y = (double)Math.round(y * factor) / factor;
        
        previousX = this.x;
        if(x < -1) {
            this.x = utils.Consts.NUM_CELLS_X + x;
        } else if(x >= utils.Consts.NUM_CELLS_X) {
            this.x = x - utils.Consts.NUM_CELLS_X-1;
        } else {
            this.x = x;
        }
        
        previousY = this.y;
        if(y < -1) {
            this.y = utils.Consts.NUM_CELLS_Y + y;
        } else if(y >= utils.Consts.NUM_CELLS_Y ) {
            this.y = y-utils.Consts.NUM_CELLS_Y-1;
        } else {
            this.y = y;
        }
        return true;
    }
    
    public final void setPosition(Position pos) {
        setPosition(pos.x, pos.y);
    }
    
    public final void roundPosition() {
        x = Math.round(x);
        y = Math.round(y);
    }
    
    public final boolean isRoundPosition(double error) {
        if(x >= 0 && x <= utils.Consts.NUM_CELLS_X-1) { 
            if(y >= 0 && y <= utils.Consts.NUM_CELLS_Y-1) { 
                double dx = x - (long)x;
                double dy = y - (long)y;

                return (dx <= 0.5*error || dx >= 1-0.5*error) && (dy <= 0.5*error || dy >= 1-0.5*error);
            }
        }
        return false;
    }
    
    @Override
    public final String toString() {
        return "(" + x + ", " + y + ")";
    }
    
    public double getX(){
        return x;
    }
   
    public double getY(){
        return y;
    }

    public boolean comeBack(){
        return this.setPosition(previousX,previousY);
    }
    
    public boolean moveUp(){
        return this.setPosition(this.getX(), this.getY()-Consts.WALK_STEP);
    }
    public boolean moveDown(){
        return this.setPosition(this.getX(), this.getY()+Consts.WALK_STEP);
    }
    public boolean moveRight(){
        return this.setPosition(this.getX()+Consts.WALK_STEP, this.getY());
    }
    public boolean moveLeft(){
        return this.setPosition(this.getX()-Consts.WALK_STEP, this.getY());        
    }
    public double getDistanceTo(Position desiredPos) {
        return Math.pow(Math.pow(this.getX() - desiredPos.getX(), 2)+Math.pow(this.getY() - desiredPos.getX(), 2), 0.5d);
    }
    
    public boolean isNear(Position desiredPos, double err) {
        if(getDistanceTo(desiredPos) < err) {
            return true;
        }
        
        return false;
    }
}
