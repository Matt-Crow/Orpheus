package world;

import gui.graphics.CustomColors;
import gui.graphics.MapLoader;
import gui.graphics.Tile;
import java.awt.Color;
import java.io.IOException;

/**
 *
 * @author Matt Crow
 */
public class TempWorldBuilder {
    private WorldContent content;
    
    public TempWorldBuilder withContent(WorldContent content){
        this.content = content;
        return this;
    }
    
    /**
     * Creates the classic WorldShell where battles take place: 
 a 20x20 square.
     * 
     * Handles most of the initialization for you,
     * all you need to do is add teams,
     * then set it's minigame to a Battle
     * 
     * @return the newly created world.
     */
    public WorldContent createDefaultBattle(){
        WorldContent content = new WorldContent(20);
        try {
            content.setMap(MapLoader.readCsv(WorldContent.class.getResourceAsStream("/testMap.csv")));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        Tile block = new Tile(0, 0, CustomColors.GRAY);
        block.setBlocking(true);
        content.getMap().addToTileSet(0, new Tile(0, 0, Color.BLUE));
        content.getMap().addToTileSet(1, block);
        
        return content;
    }
    
    public TempWorld build(){
        WorldContent ser = (content == null) 
            ? createDefaultBattle()
            : content;
        
        TempWorld world = new TempWorld(ser, new NonSerializableWorldPart());
        
        return world;
    }
}
