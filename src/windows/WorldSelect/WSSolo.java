package windows.WorldSelect;

import battle.Battle;
import battle.Team;
import controllers.Master;
import controllers.World;
import entities.TruePlayer;
import graphics.Map;
import graphics.Tile;
import java.awt.Color;
import javax.swing.*;
import windows.Page;
import windows.world.WorldPage;

/**
 *
 * @author Matt Crow
 */
public class WSSolo extends AbstractWSNewWorld{
    
    public WSSolo(Page p){
        super(p);
    }
    
    @Override
    public void start(){
        Team team1 = Team.constructRandomTeam("Team 1", Color.green, getTeamSize() - 1);
        Team team2 = Team.constructRandomTeam("Team 2", Color.red, getTeamSize());
        
        
        Master.getUser().initPlayer();
        Master.getUser().getPlayer().applyBuild(getSelectedBuild());
        team1.addMember(Master.getUser().getPlayer());

        World battleWorld = new World(20);
        //it's like a theme park or something
        battleWorld.createCanvas();
        
        Map m = new Map(20, 20);
        Tile block = new Tile(0, 0, Color.red);
        block.setBlocking(true);
        m
            .addToTileSet(0, new Tile(0, 0, Color.BLUE))
            .addToTileSet(1, block);
        m
            .setTile(8, 10, 1)
            .setTile(8, 11, 1)
            .setTile(8, 12, 1)
            .setTile(7, 12, 1)
            .setTile(7, 13, 1)
            .setTile(7, 14, 1)
            .setTile(8, 14, 1)
            .setTile(9, 14, 1)
            .setTile(10, 14, 1)
            .setTile(10, 13, 1)
            .setTile(10, 10, 1)
            .setTile(10, 11, 1)
            .setTile(10, 12, 1);
        battleWorld.setMap(m);
        
        battleWorld
            .addTeam(team1)
            .addTeam(team2);
        
        Battle b = new Battle();
        battleWorld.setCurrentMinigame(b);
        b.setHost(battleWorld);
        
        battleWorld.init();
        
        WorldPage wp = new WorldPage();
        wp.setCanvas(battleWorld.getCanvas());
        getHostingPage().switchToPage(wp);
    }
}
