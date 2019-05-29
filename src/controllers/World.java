package controllers;

import battle.Team;
import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Matt Crow
 */
public class World {
    private final int worldSize;
    private int tileSize; // the total size of each tile
    private int spacing; // the spacing between each tile
    private int rectSize; // the size of the colored portion of each tile
    private final int[][] map;
    private final HashMap<Integer, Color> colors;
    
    private final ArrayList<Team> teams; //makes it faster to find nearest enemies
    //maybe just keep track of everything in one linked list?
    
    public World(int size){
        worldSize = size;
        tileSize = 1;
        spacing = 0;
        rectSize = 1;
        map = new int[size][size];
        for(int i = 0; i < size; i++){
            for(int j = 0; j < size; j++){
                map[i][j] = 0;
            }
        }
        colors = new HashMap<>();
        teams = new ArrayList<>();
    }
    
    public World setTileSize(int i){
        tileSize = i;
        spacing = (int)(tileSize * 0.05);
        rectSize = i - 2 * spacing;
        return this;
    }
    
    public World setTile(int x, int y, int value){
        map[x][y] = value;
        return this;
    }
    
    public World setColor(int valueInMap, Color c){
        colors.put(valueInMap, c);
        return this;
    }
    
    public int getSize(){
        return tileSize * worldSize;
    }
    
    public void update(){
        teams.forEach((t)->t.update());
    }
    
    public void draw(Graphics g){
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, worldSize * tileSize, worldSize * tileSize);
        for(int x = 0; x < worldSize; x++){
            for(int y = 0; y < worldSize; y++){
                if(colors.containsKey(map[x][y])){
                    g.setColor(colors.get(map[x][y]));
                    g.fillRect(x * tileSize + spacing, y * tileSize + spacing, rectSize, rectSize);
                }
            }
        }
        teams.forEach((t)->t.draw(g));
    }
}
