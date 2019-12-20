package windows.WorldSelect;

import battle.Battle;
import battle.Team;
import controllers.Master;
import controllers.World;
import java.awt.Color;
import windows.world.WorldPage;

/**
 *
 * @author Matt Crow
 */
public class WSSolo extends AbstractWSNewWorld{
    
    public WSSolo(){
        super();
    }
    
    @Override
    public void start(){
        Team team1 = new Team("Players", Color.green);
        Team team2 = Team.constructRandomTeam("AI", Color.red, getTeamSize(),  getEnemyLevel());
        
        
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
        
        WorldPage wp = new WorldPage();
        wp.setCanvas(battleWorld.getCanvas());
        getHost().switchToPage(wp);
    }
}
