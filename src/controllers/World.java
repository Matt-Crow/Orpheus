package controllers;

import battle.Team;
import graphics.Tile;
import java.awt.Color;
import java.awt.Graphics;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * The World class will act as a controller for the game.
 * It will keep track of all Entities in the game through one of 2 ways:
 * either (a): by keeping track of each Team, which in turn keeps track of various entities
 * or (b): Keeping track of all Entities as one gigantic linked list (of my own design).
 * (a) has the advantage of being faster to check for collisions, as team members should collide with one another (or should they?)
 * (b) suits a less structured world where Entities can be unaffilliated with a team, and allows me to search faster if the linked list is sorted by ID.
 * 
 * The World will handle all the drawing and updating as well.
 * @author Matt Crow
 */
public class World {
    private final int worldSize;
    private final int[][] map;
    
    private final HashMap<Integer, Color> colors; //will have to choose 1 to use
    private final HashMap<Integer, Tile> tileSet;
    private final HashMap<Integer, Class> tileClasses;
    
    private final ArrayList<Tile> tiles;
    
    private final ArrayList<Team> teams; //makes it faster to find nearest enemies
    //maybe just keep track of everything in one linked list?
    
    public World(int size){
        worldSize = size;
        map = new int[size][size];
        for(int i = 0; i < size; i++){
            for(int j = 0; j < size; j++){
                map[i][j] = 0;
            }
        }
        colors = new HashMap<>();
        tileClasses = new HashMap<>();
        tileSet = new HashMap<>();
        tiles = new ArrayList<>();
        teams = new ArrayList<>();
    }
    
    public World setTile(int x, int y, int value){
        map[x][y] = value;
        return this;
    }
    
    public World setBlock(int valueInMap, Class t){
        //todo add constructor checking
        tileClasses.put(valueInMap, t);
        return this;
    }
    
    public World setBlock(int valueInMap, Tile t){
        tileSet.put(valueInMap, t);
        return this;
    }
    
    public World setColor(int valueInMap, Color c){
        class C extends Tile{
            public C(int x, int y){
                super(x, y, c);
            }
        }
        
        colors.put(valueInMap, c);
        tileClasses.put(valueInMap, C.class);
        return this;
    }
    
    public int getSize(){
        return Tile.TILE_SIZE * worldSize;
    }
    
    public void initTiles(){
        tiles.clear();
        Constructor c;
        Tile t;
        
        for(int x = 0; x < worldSize; x++){
            for(int y = 0; y < worldSize; y++){
                if(tileSet.containsKey(map[x][y])){
                    t = tileSet.get(map[x][y]).copy(x, y);
                    tiles.add(t);
                }
                /*
                if(tileClasses.containsKey(map[x][y])){
                    try {
                        for(Constructor con : tileClasses.get(map[x][y]).getConstructors()){
                            System.out.println("Parameters:");
                            System.out.println(Arrays.toString(con.getParameterTypes()));
                        }
                        
                        c = tileClasses.get(map[x][y]).getConstructor(int.class, int.class);
                        t = (Tile)c.newInstance(x, y);
                        tiles.add(t);
                    } catch (NoSuchMethodException ex) {
                        ex.printStackTrace();
                    } catch (SecurityException ex) {
                        ex.printStackTrace();
                    } catch (InstantiationException ex) {
                        ex.printStackTrace();
                    } catch (IllegalAccessException ex) {
                        ex.printStackTrace();
                    } catch (IllegalArgumentException ex) {
                        ex.printStackTrace();
                    } catch (InvocationTargetException ex) {
                        ex.printStackTrace();
                    }
                }*/
            }
        }
    }
    
    public void update(){
        teams.forEach((t)->t.update());
    }
    
    public void draw(Graphics g){
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getSize(), getSize());
        
        tiles.forEach((tile)->tile.draw(g));
        teams.forEach((t)->t.draw(g));
    }
}
