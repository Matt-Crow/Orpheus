package world;

import world.battle.Team;
import world.game.Game;

/**
 * used for polymorphism
 * @author Matt Crow <mattcrow19@gmail.com>
 */
public interface WorldContent {
    /**
     * this method is called by worlds after they set their content after
     * deserialization. This ensures objects in this are notified their world
     * has changed
     * 
     * @param w the world this was deserialized into
     */
    public void setWorld(World w);
    
    public Map getMap();
    public Team getPlayers();
    public Team getAi();
    public Game getGame();
    
    public void init();
    public void update();
}
