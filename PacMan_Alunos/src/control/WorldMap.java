/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import utils.Consts;

public class WorldMap {
    char[][] map;
    private static WorldMap worldMap = null;
    
    private WorldMap() {
        map = new char[Consts.NUM_CELLS_X][Consts.NUM_CELLS_Y];
    }
    
    public static WorldMap map() {
        if(WorldMap.worldMap == null) {
            worldMap = new WorldMap();
        }
        
        return worldMap;
    }
}
