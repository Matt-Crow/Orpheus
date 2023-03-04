package world;

import orpheus.core.world.graph.Graphable;
import world.battle.Team;
import world.entities.AbstractEntity;
import world.game.Game;


/**
 * enables the proxy design pattern for providing a stable reference to the
 * volatile world content
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
    public void spawn(AbstractEntity e);
    
    public void init();
    
    public void update();

    public orpheus.core.world.graph.World toGraph();
}
