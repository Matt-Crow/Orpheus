package world;

import gui.graphics.Map;
import gui.graphics.Tile;
import java.awt.Graphics;
import util.Random;
import world.battle.Battle;
import world.battle.Team;
import world.entities.AbstractEntity;
import world.entities.Projectile;
import world.entities.particles.Particle;

/**
 * Using this to phase out WorldContent & AbstractWorldShell
 * in exchange for World, SerializableWorldPart & NonSerializableWorldPart
 * @author Matt Crow
 */
public class TempWorld {
    private volatile WorldContent ser;
    private final AbstractWorldShell noser;
    
    protected TempWorld(WorldContent ser, AbstractWorldShell noser){
        this.ser = ser;
        this.noser = noser;
        ser.setTempWorld(this);
        noser.setTempWorld(this);
    }
    
    public AbstractWorldShell getShell(){
        return noser;
    }
    public void setContent(WorldContent content){
        if(content == null){
            throw new NullPointerException();
        }
        ser = content;
    }
    public WorldContent getContent(){
        return ser;
    }
    
    /*
    Everything below here is copy-pasted from World
    */
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
    
    public void update(){
        ser.update();
        noser.update();
    }
    
    public void draw(Graphics g){
        ser.draw(g);
        noser.draw(g);
    }

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
