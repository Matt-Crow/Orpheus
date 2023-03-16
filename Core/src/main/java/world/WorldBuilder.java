package world;

import world.battle.Team;
import world.game.Game;

/**
 *
 * @author Matt Crow <mattcrow19@gmail.com>
 */
public interface WorldBuilder {
    public WorldBuilder withGame(Game g);
    public WorldBuilder withPlayers(Team t);
    public WorldBuilder withAi(Team t);
    public World build();
}
