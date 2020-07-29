package gui.windows.WorldSelect;

import battle.Battle;
import battle.Team;
import controls.SoloPlayerControls;
import entities.HumanPlayer;
import java.awt.Color;
import users.LocalUser;
import gui.windows.world.WorldCanvas;
import gui.windows.world.WorldPage;
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
        LocalUser user = LocalUser.getInstance();
        HumanPlayer player = new HumanPlayer(user.getName());
        
        Team team1 = new Team("Players", Color.green);
        Team team2 = new Team("AI", Color.red);
        
        player.applyBuild(getSelectedBuild());
        team1.addMember(player);

        SoloWorld battleWorld = new SoloWorld(WorldContent.createDefaultBattle());
        
        //it's like a theme park or something
        battleWorld.createCanvas();
        
        battleWorld
            .setPlayerTeam(team1)
            .setEnemyTeam(team2);
        
        Battle b = createBattle();
        battleWorld.setCurrentMinigame(b);
        b.setHost(battleWorld.getContent());
        
        battleWorld.init();
        
        WorldCanvas canv = battleWorld.getCanvas();
        canv.addPlayerControls(new SoloPlayerControls(battleWorld, player.id));
        
        WorldPage wp = new WorldPage();
        wp.setCanvas(canv);
        getHost().switchToPage(wp);
    }
}
