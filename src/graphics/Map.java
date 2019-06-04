package graphics;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * The Map class is used to store Tiles together to form 
 * a 2-dimensional playing field for players to play on.
 * @author Matt
 */
public class Map {
    private final int size;
    private final int[][] tileMap;
    //the base Tiles that are copied to construct the map
    private final HashMap<Integer, Tile> tileSet;
    private final ArrayList<Tile> allTiles;
    private final ArrayList<Tile> tangibleTiles;
    
    public Map(int s){
        size = s;
        tileMap = new int[size][size];
        for(int i = 0; i < size; i++){
            for(int j = 0; j < size; j++){
                tileMap[i][j] = 0;
            }
        }
        tileSet = new HashMap<>();
        allTiles = new ArrayList<>();
        tangibleTiles = new ArrayList<>();
    }
    
    /**
     * Adds a Tile to this' set.
     * Any array indexes with a 
     * value equal to the first parameter will
     * generate the given Tile upon initializing tiles
     * @param valueInMap the key for this tile
     * @param t the Tile to copy when generating the map.
     * @return this, for chaining purposes
     */
    public Map addToTileSet(int valueInMap, Tile t){
        tileSet.put(valueInMap, t);
        return this;
    }
    
    /**
     * Sets a tile in this' tile map to a given value.
     * for example, calling setTile(1, 0, 2) will cause
     * the tile at x index 1, y index 0, to become the type
     * of tile with a key of 2 in this' tile set.
     * @param xIdx
     * @param yIdx
     * @param value
     * @return 
     */
    public Map setTile(int xIdx, int yIdx, int value){
        if(xIdx < 0 || xIdx >= size || yIdx < 0 || yIdx >= size){
            throw new IndexOutOfBoundsException();
        }
        tileMap[xIdx][yIdx] = value;
        return this;
    }
}
