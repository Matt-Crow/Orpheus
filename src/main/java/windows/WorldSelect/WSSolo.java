package windows.WorldSelect;

import battle.Battle;
import battle.Team;
import controllers.Master;
import world.AbstractWorld;
import java.awt.Color;
import windows.world.WorldPage;
import world.SoloWorld;
import world.WorldContent;

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
        Team team2 = new Team("AI", Color.red);
        
        
        Master.getUser().initPlayer();
        Master.getUser().getPlayer().applyBuild(getSelectedBuild());
        team1.addMember(Master.getUser().getPlayer());

        SoloWorld battleWorld = new SoloWorld(WorldContent.createDefaultBattle());
        //it's like a theme park or something
        battleWorld.createCanvas();
        battleWorld
            .setPlayerTeam(team1)
            .setEnemyTeam(team2);
        
        Battle b = new Battle(getMaxEnemyLevel(), getNumWaves());
        battleWorld.setCurrentMinigame(b);
        b.setHost(battleWorld);
        
        battleWorld.init();
        
        WorldPage wp = new WorldPage();
        wp.setCanvas(battleWorld.getCanvas());
        getHost().switchToPage(wp);
    }
}
