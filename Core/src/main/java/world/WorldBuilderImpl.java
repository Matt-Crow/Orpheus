package world;

import gui.graphics.CustomColors;
import java.awt.Color;
import java.io.IOException;
import world.battle.Team;
import world.game.Game;
import world.game.Onslaught;

/**
 *
 * @author Matt Crow
 */
public class WorldBuilderImpl implements WorldBuilder {
    private Team players = Team.ofPlayers();
    private Team ai = Team.ofPlayers();
    private Game game = new Onslaught(1);
    
    @Override
    public WorldBuilderImpl withPlayers(Team players){
        this.players = players;
        return this;
    }
    
    @Override
    public WorldBuilderImpl withAi(Team ai){
        this.ai = ai;
        return this;
    }
    
    @Override
    public WorldBuilderImpl withGame(Game game){
        this.game = game;
        return this;
    }
    
    @Override
    public WorldImpl build(){
        if(players == null){
            throw new RuntimeException("players not set");
        }
        if(ai == null){
            throw new RuntimeException("ai team not set");
        }
        if(game == null){
            throw new RuntimeException("game not set");
        }
        
        Map defaultMap = null;
        try {
            defaultMap = MapLoader.readCsv(WorldBuilderImpl.class.getResourceAsStream("/testMap.csv"));
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        Tile block = new Tile(0, 0, CustomColors.GRAY);
        block.setBlocking(true);
        defaultMap.addToTileSet(0, new Tile(0, 0, Color.BLUE));
        defaultMap.addToTileSet(1, block);
                
        var world = new WorldImpl(defaultMap, players, ai, game);
        game.setHost(world);
        players.setWorld(world);
        ai.setWorld(world);
        
        return world;
    }
}
