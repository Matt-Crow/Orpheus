package world;

import gui.graphics.CustomColors;
import gui.graphics.Map;
import gui.graphics.MapLoader;
import gui.graphics.Tile;
import java.awt.Color;
import java.io.IOException;
import world.battle.Battle;
import world.battle.Team;

/**
 *
 * @author Matt Crow
 */
public class WorldBuilder {
    private Team players;
    private Team ai;
    private Battle game;
    
    public WorldBuilder(){
        
    }
    
    public WorldBuilder withPlayers(Team players){
        this.players = players;
        return this;
    }
    
    public WorldBuilder withAi(Team ai){
        this.ai = ai;
        return this;
    }
    
    public WorldBuilder withGame(Battle game){
        this.game = game;
        return this;
    }
    
    public World build(){
        if(players == null){
            throw new RuntimeException();
        }
        if(ai == null){
            throw new RuntimeException();
        }
        if(game == null){
            throw new RuntimeException();
        }
        
        Map defaultMap = null;
        try {
            defaultMap = MapLoader.readCsv(WorldBuilder.class.getResourceAsStream("/testMap.csv"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        Tile block = new Tile(0, 0, CustomColors.GRAY);
        block.setBlocking(true);
        defaultMap.addToTileSet(0, new Tile(0, 0, Color.BLUE));
        defaultMap.addToTileSet(1, block);
        
        SerializableWorldPart ser = new SerializableWorldPart(
            defaultMap,
            players,
            ai,
            game
        );
        
        NonSerializableWorldPart noser = new NonSerializableWorldPart();
        
        World w = new World(ser, noser);
        
        return w;
    }
}
