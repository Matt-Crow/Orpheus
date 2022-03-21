package world;

import gui.graphics.Map;
import gui.graphics.Tile;
import java.awt.Graphics;
import util.Random;
import world.battle.Team;
import world.entities.AbstractEntity;
import world.entities.Projectile;
import world.entities.particles.Particle;
import world.game.Game;

/**
 * This abstractifies the process of handling both parts of the world, both the
 * serialized and non-serialized portions.
 * 
 * @author Matt Crow
 */
public class WorldImpl implements World{
    private volatile SerializableWorldPart ser;
    private final NonSerializableWorldPart noser;
    
    protected WorldImpl(SerializableWorldPart ser, NonSerializableWorldPart noser){
        this.ser = ser;
        this.noser = noser;
    }
    
    @Override
    public Map getMap(){
        return ser.getMap();
    }
    
    @Override
    public Team getPlayers(){
        return ser.getPlayers();
    }
    
    @Override
    public Team getAi(){
        return ser.getAi();
    }
    
    @Override
    public Game getGame(){
        return ser.getGame();
    }
    
    @Override
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
    
    @Override
    public void spawn(Particle p){
        noser.addParticle(p);
    }
    
    @Override
    public void init(){
        ser.init();
        noser.init();
        getPlayers().init(this);
        getAi().init(this);
    }
    
    @Override
    public void update(){ // may need to split off into server / client updates
        ser.update();
        noser.update();
    }
    
    public void draw(Graphics g){
        ser.draw(g);
        noser.draw(g);
    }

    @Override
    public void updateParticles() {
        noser.update();
        spawnParticles(ser.getPlayers());
        spawnParticles(ser.getAi());
    }
    
    private void spawnParticles(Team t){
        t.forEach((AbstractEntity member)->{
            if(member instanceof Projectile){
                ((Projectile)member).spawnParticles();
            }
        });
    }
}
