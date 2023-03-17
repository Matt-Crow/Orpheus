package world;

import orpheus.core.world.graph.Graphable;
import orpheus.core.world.occupants.WorldOccupant;
import world.battle.Team;
import world.game.Game;


/**
 * A world is where games occur.
 * 
 * @author Matt Crow <mattcrow19@gmail.com>
 */
public interface World extends Graphable {
    
    public Map getMap();
    
    public Game getGame();
    
    public Team getPlayers();
    
    public Team getAi();
    
    /**
     * inserts the given entity into this world
     * 
     * @param e the entity to insert into this world 
     */
    public void spawn(WorldOccupant e);
    
    public void init();
    
    public void update();

    public orpheus.core.world.graph.World toGraph();
}
