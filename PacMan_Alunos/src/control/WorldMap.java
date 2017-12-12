/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import java.io.BufferedReader;
import utils.Consts;

import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import utils.Position;

public class WorldMap implements Serializable {
    final static public char LEFT  = 0x01;
    final static public char RIGHT = 0x02;
    final static public char UP    = 0x04;
    final static public char DOWN  = 0x08;
    
    transient private char[][] map;
    private String saveFile = null;

    transient private static WorldMap worldMap = null;
    
    protected Position PacManPosition;
    protected int PacManMovDirection;
    
    private WorldMap() {
        map = new char[Consts.NUM_CELLS_X][Consts.NUM_CELLS_Y];
    }
    
    public void loadWorldMap(WorldMap map) {
        this.saveFile = map.saveFile;
        loadFile(saveFile);
    }
    
    public void loadFile(String file) {
        saveFile = file;
        FileReader reader = null;
        BufferedReader buff = null;
        
        try {
            reader = new FileReader(file);
            buff = new BufferedReader(reader);
            
            int x = 0;
            int y = 0;
            int c;
            while((c = buff.read()) != -1) {
                if(x >= Consts.NUM_CELLS_X && (char)c != '\n') {
                    buff.readLine();
                }
                if((char)c == '\n' || x >= Consts.NUM_CELLS_X) {
                    x = 0;
                    y++;
                    
                    if(y >= Consts.NUM_CELLS_Y) {
                        break;
                    } else {
                        continue;
                    }
                }
                
                map[x][y] = (char) c;
                x++;
            }
            
            buff.close();
            reader.close();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    public byte freePath(int x, int y) {
        if(y < 0) {
            y += Consts.NUM_CELLS_Y;
        } else if(y >= Consts.NUM_CELLS_Y) {
            y -= Consts.NUM_CELLS_Y;
        }
        
        if(x < 0) {
            x += Consts.NUM_CELLS_X;
        } else if(x >= Consts.NUM_CELLS_X) {
            x -= Consts.NUM_CELLS_X;
        }
        
        byte rtrn = 0;
        
        if(x > 0 && isValidPosition(map[x-1][y])) {
            rtrn |= LEFT;
        }
        
        if(y > 0 && isValidPosition(map[x][y-1])) {
            rtrn |= UP;
        }
        
        if(y < Consts.NUM_CELLS_Y-1 && isValidPosition(map[x][y+1])) {
            rtrn |= DOWN;
        }
        
        if(x < Consts.NUM_CELLS_X - Consts.HEADER_SIZE - 1 && isValidPosition(map[x+1][y])) {
            rtrn |= RIGHT;
        }
        
        return rtrn;
    }
    
    private boolean isValidPosition(char c) {
        // free-space, pacDots, powerPallets
        return (c == ' ' || c == '.' || c == 'o');
    }
    
    public static WorldMap getInstance() {
        if(WorldMap.worldMap == null) {
            worldMap = new WorldMap();
        }
        
        return worldMap;
    }
    
    public char[][] getMap() {
        return map;
    }

    public void setPacManPosition(Position pos) {
        this.PacManPosition = pos;
    }
    
    public Position getPacManPosition() {
        return this.PacManPosition;
    }
    
    public void setPacManDirection(int movDirection) {
        this.PacManMovDirection = movDirection;
    }
    
    public int getPacManDirection() {
        return this.PacManMovDirection;
    }
}
