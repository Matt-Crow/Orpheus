package world;

import gui.graphics.Map;
import gui.graphics.Tile;
import java.awt.Graphics;
import util.Random;
import world.battle.Battle;
import world.battle.Team;
import world.entities.AbstractEntity;
import world.entities.particles.Particle;

/**
 * This abstractifies the process of handling both parts of the world, both the
 * serialized and non-serialized portions.
 * 
 * @author Matt Crow
 */
public class World {
    private volatile SerializableWorldPart ser;
    private final NonSerializableWorldPart noser;
    
    protected World(SerializableWorldPart ser, NonSerializableWorldPart noser){
        this.ser = ser;
        this.noser = noser;
    }
    
    public Map getMap(){
        return ser.getMap();
    }
    
    public Team getPlayers(){
        return ser.getPlayers();
    }
    
    public Team getAi(){
        return ser.getAi();
    }
    
    public Battle getGame(){
        return ser.getGame();
    }
    
    public void spawn(AbstractEntity e){
        int minX = 0;
        int maxX = getMap().getWidth() / Tile.TILE_SIZE;
        int minY = 0;
        int maxY = getMap().getHeight() / Tile.TILE_SIZE;
        int rootX = 0;
        int rootY = 0;
        
        do{
            rootX = Random.choose(minX, maxX);
            rootY = Random.choose(minY, maxY);
        } while(!getMap().isOpenTile(rootX * Tile.TILE_SIZE, rootY * Tile.TILE_SIZE));
        
        e.setX(rootX * Tile.TILE_SIZE + Tile.TILE_SIZE / 2);
        e.setY(rootY * Tile.TILE_SIZE + Tile.TILE_SIZE / 2);
    }
    
    public void spawn(Particle p){
        noser.addParticle(p);
    }
    
    public void init(){
        ser.init();
        noser.init();
    }
    
    public void update(){ // may need to split off into server / client updates
        ser.update();
        noser.update();
    }
    
    public void draw(Graphics g){
        ser.draw(g);
        noser.draw(g);
    }
}
