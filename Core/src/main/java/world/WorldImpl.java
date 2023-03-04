package world;

import util.Random;
import world.battle.Team;
import world.entities.AbstractEntity;
import world.game.Game;

/**
 * Handling both the serialized and non-serialized parts of the world.
 * Objects should reference World instead of this class or either of its parts
 * directly.
 * 
 * This class can be simplified to a proxy of the serialize-able world content
 * if particles can be serialized efficiently.
 * 
 * Note that references to a World or WorldImpl are stable: they will not change
 * as the world is serialized and de-serialized, unlike references to the 
 * serialized content, which is constantly changing in multiplayer.
 * 
 * @author Matt Crow
 */
public class WorldImpl implements World {
    private final WorldContent ser;
    
    protected WorldImpl(WorldContent ser){
        this.ser = ser;
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
    
    /**
     * spawns the given entity at a random valid point in the world
     * 
     * @param e 
     */
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
    public void init(){
        ser.init();
        getPlayers().init(this);
        getAi().init(this);
    }
    
    @Override
    public void update(){
        ser.update();
    }

    @Override
    public orpheus.core.world.graph.World toGraph() {
        return new orpheus.core.world.graph.World(
            getMap().toGraph(),
            getPlayers().toGraph(),
            getAi().toGraph(),
            getGame().toGraph()
        );
    }
}
