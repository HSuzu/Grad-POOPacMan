/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import elements.Element;
import elements.Items;
import elements.Wall;
import java.io.BufferedReader;
import utils.Consts;
import java.util.ArrayList;

import java.io.FileReader;
import java.io.IOException;

public class WorldMap {
    final static public char LEFT  = 0x01;
    final static public char RIGHT = 0x02;
    final static public char UP    = 0x04;
    final static public char DOWN  = 0x08;
    
    char[][] map;
    private final ArrayList<Items> pacDots;
    private final ArrayList<Items> powerPellets;
    private final ArrayList<Wall> walls;
    
    private static WorldMap worldMap = null;
    
    private WorldMap() {
        pacDots = new ArrayList<>();
        powerPellets = new ArrayList<>();
        walls = new ArrayList<>();
        
        map = new char[Consts.NUM_CELLS_X - Consts.HEADER_SIZE][Consts.NUM_CELLS_Y];
    }
    
    public void loadFile(String file) {
        FileReader reader = null;
        BufferedReader buff = null;
        
        try {
            reader = new FileReader(file);
            buff = new BufferedReader(reader);
            
            int x = 0;
            int y = 0;
            int c;
            while((c = buff.read()) != -1) {
                if(x >= Consts.NUM_CELLS_X - Consts.HEADER_SIZE) {
                    x = 0;
                    y++;
                    
                    continue;
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
        if(c == ' ' || c == '.' || c == 'o') { // free-space, pacDots, powerPallets
            return true;
        }
        
        return false;
    }
    
    public static WorldMap map() {
        if(WorldMap.worldMap == null) {
            worldMap = new WorldMap();
        }
        
        return worldMap;
    }
}
