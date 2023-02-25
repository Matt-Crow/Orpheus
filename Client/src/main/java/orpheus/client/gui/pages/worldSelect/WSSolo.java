package orpheus.client.gui.pages.worldselect;

import orpheus.client.gui.pages.PlayerControls;
import net.protocols.SoloWorldUpdater;
import world.battle.Team;
import world.entities.HumanPlayer;
import java.awt.Color;

import orpheus.client.ClientAppContext;
import orpheus.client.gui.pages.play.LocalWorldRenderer;
import orpheus.client.gui.pages.play.WorldCanvas;
import orpheus.client.gui.pages.play.WorldPage;
import start.AbstractOrpheusCommandInterpreter;
import orpheus.client.gui.pages.PageController;
import start.SoloOrpheusCommandInterpreter;
import world.*;

/**
 *
 * @author Matt Crow
 */
public class WSSolo extends AbstractWSNewWorld{
    public WSSolo(ClientAppContext context, PageController host){
        super(context, host);
    }
    
    @Override
    public void start(){
        AbstractOrpheusCommandInterpreter orpheus = new SoloOrpheusCommandInterpreter();
        
        Team team1 = new Team("Players", Color.green);
        Team team2 = new Team("AI", Color.red);
        
        World world = new WorldBuilderImpl()
                .withGame(createGame())
                .withPlayers(team1)
                .withAi(team2)
                .build();
        
        SoloWorldUpdater updater = new SoloWorldUpdater(world);
        
        HumanPlayer player = new HumanPlayer(
            world,
            "local user"
        );
        
        player.applyBuild(getSelectedBuild());
        team1.addMember(player);
        
        // model must have teams set before WorldCanvas init, as WC relies on getting the player team
        WorldCanvas renderer = new WorldCanvas(
            new LocalWorldRenderer(world),
            world, 
            new PlayerControls(world, player.id, orpheus),
            true
        );
        
        world.init();
        
        WorldPage wp = new WorldPage(getContext(), getHost());
        wp.setCanvas(renderer);
        getHost().switchToPage(wp);
        renderer.start();
        updater.start();
    }
}
