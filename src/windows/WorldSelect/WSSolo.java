package windows.WorldSelect;

import battle.Battle;
import battle.Team;
import controllers.MainWindow;
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

        World battleWorld = World.createDefaultBattle();
        //it's like a theme park or something
        battleWorld.createCanvas();
        battleWorld
            .addTeam(team1)
            .addTeam(team2);
        
        Battle b = new Battle();
        battleWorld.setCurrentMinigame(b);
        b.setHost(battleWorld);
        
        battleWorld.init();
        
        WorldPage wp = new WorldPage(getHostingPage().getHost());
        wp.setCanvas(battleWorld.getCanvas());
        getHostingPage().getHost().switchToPage(wp);
    }
}
