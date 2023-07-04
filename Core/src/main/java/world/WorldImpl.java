package world;

import orpheus.core.world.occupants.WorldOccupant;
import util.Random;
import world.battle.Team;
import world.entities.Projectile;
import world.game.Game;

/**
 * Implements the world games occur in.
 * 
 * @author Matt Crow
 */
public class WorldImpl implements World {
    private final Map map;
    private final Team players;
    private final Team ai;
    private final Game game;
    
    protected WorldImpl(Map map, Team players, Team ai, Game game){
        this.map = map;
        this.players = players;
        this.ai = ai;
        this.game = game;
    }
    
    @Override
    public Map getMap(){
        return map;
    }
    
    @Override
    public Team getPlayers(){
        return players;
    }
    
    @Override
    public Team getAi(){
        return ai;
    }
    
    @Override
    public Game getGame(){
        return game;
    }
    
    /**
     * spawns the given entity at a random valid point in the world
     * 
     * @param e 
     */
    @Override
    public void spawn(WorldOccupant e){
        int minX = 0;
        int maxX = map.getWidth() / Tile.TILE_SIZE;
        int minY = 0;
        int maxY = map.getHeight() / Tile.TILE_SIZE;
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
        map.init();
        game.play();
        players.init(this);
        ai.init(this);
    }
    
    @Override
    public void update(){
        players.update();
        ai.update();
        game.update();
        checkForCollisions(players, ai);
        checkForCollisions(ai, players);
    }
    
    private void checkForCollisions(Team t1, Team t2) {
        t1.forEach((e)->{
            map.checkForTileCollisions(e);
            if (e instanceof Projectile) {
                var p = (Projectile)e;
                t2.getMembersRem().forEach(p::hitIfColliding);
            }
        });
    }

    @Override
    public orpheus.core.world.graph.World toGraph() {
        return new orpheus.core.world.graph.World(
            map.toGraph(),
            players.toGraph(),
            ai.toGraph(),
            game.toGraph()
        );
    }
}
