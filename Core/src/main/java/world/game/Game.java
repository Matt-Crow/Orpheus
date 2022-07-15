package world.game;

import world.World;

/**
 * classes implementing this should have access to a World.
 * 
 * @author Matt Crow <mattcrow19@gmail.com>
 */
public interface Game {
    /**
     * games will need access to the world they're running in
     * @param w the world this is running in
     */
    public void setHost(World w);
    
    /**
     * called during world initialization.
     * should reset the Game
     */
    public void play();
    
    /**
     * called at the end of each frame in the world.
     */
    public void update();
    
    /**
     * @return whether the game is over 
     */
    public boolean isOver();
    
    /**
     * @return whether or not the players have won 
     */
    public boolean isPlayerWin();
}
