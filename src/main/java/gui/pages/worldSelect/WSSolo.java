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
        
        WorldContent model = WorldContent.createDefaultBattle();
        
        //it's like a theme park or something
        SoloWorld battleWorld = new SoloWorld(model);
        
        HumanPlayer player = new HumanPlayer(
            model,
            user.getName()
        );
        
        Team team1 = new Team("Players", Color.green);
        Team team2 = new Team("AI", Color.red);
        
        player.applyBuild(getSelectedBuild());
        team1.addMember(player);
        
        model.setPlayerTeam(team1);
        model.setAITeam(team2);
        
        // model must have teams set before WorldCanvas init, as WC relies on getting the player team
        WorldCanvas renderer = new WorldCanvas(
            battleWorld, 
            new PlayerControls(battleWorld, player.id, orpheus),
            true
        );
        
        Battle b = createBattle();
        battleWorld.setCurrentMinigame(b);
        b.setHost(battleWorld.getContent());
        
        battleWorld.init();
        
        WorldPage wp = new WorldPage(new SoloOrpheusCommandInterpreter(user));
        wp.setCanvas(renderer);
        getHost().switchToPage(wp);
        renderer.start();
    }
}
