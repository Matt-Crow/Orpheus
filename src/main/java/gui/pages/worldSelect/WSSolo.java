package gui.pages.worldSelect;

import controls.PlayerControls;
import world.battle.Battle;
import world.battle.Team;
import world.entities.HumanPlayer;
import java.awt.Color;
import users.LocalUser;
import gui.pages.worldPlay.WorldCanvas;
import gui.pages.worldPlay.WorldPage;
import start.AbstractOrpheusCommandInterpreter;
import start.SoloOrpheusCommandInterpreter;
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
        AbstractOrpheusCommandInterpreter orpheus = new SoloOrpheusCommandInterpreter(user);
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
        canv.addPlayerControls(new PlayerControls(battleWorld, player.id, orpheus));
        //canv.addPlayerControls(new SoloPlayerControls(battleWorld, player.id));
        
        WorldPage wp = new WorldPage(new SoloOrpheusCommandInterpreter(user));
        wp.setCanvas(canv);
        getHost().switchToPage(wp);
    }
}
