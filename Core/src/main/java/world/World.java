package world;

import gui.graphics.Map;
import java.awt.Graphics;
import world.battle.Team;
import world.entities.AbstractEntity;
import world.entities.particles.Particle;
import world.game.Game;


/**
 * enables the proxy design pattern for providing a stable reference to the
 * volatile world content
 * 
 * @author Matt Crow <mattcrow19@gmail.com>
 */
public interface World {
    
    /**
     * @return the serializable content of this world
     */
    public WorldContent getSerializableContent();
    
    public void setSerializableContent(WorldContent wc);
    
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
    
    /**
     * inserts the given particle into this world
     * 
     * @param p the particle to insert into this world 
     */
    public void spawn(Particle p);
    
    public void init();
    
    public void update();
    
    public void updateParticles();
    
    public void draw(Graphics g);
}
