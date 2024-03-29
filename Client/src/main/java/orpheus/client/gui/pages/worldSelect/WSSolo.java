package orpheus.client.gui.pages.worldselect;

import orpheus.client.gui.pages.PlayerControls;
import world.battle.Team;
import orpheus.client.ClientAppContext;
import orpheus.client.gui.pages.play.HeadsUpDisplay;
import orpheus.client.gui.pages.play.LocalWorldSupplier;
import orpheus.client.gui.pages.play.SoloWorldUpdater;
import orpheus.client.gui.pages.play.WorldCanvas;
import orpheus.client.gui.pages.play.WorldPage;
import orpheus.core.commands.executor.LocalExecutor;
import orpheus.core.world.graph.particles.Particles;
import orpheus.core.world.occupants.players.Player;
import orpheus.client.gui.pages.PageController;
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
        var players = Team.ofPlayers();        
        World world = new WorldBuilderImpl()
                .withGame(createGame())
                .withPlayers(players)
                .withAi(Team.ofAi())
                .build();
        
        var graph = new LocalWorldSupplier(world);
        var particles = new Particles();
        SoloWorldUpdater updater = new SoloWorldUpdater(graph, particles, world);
        
        
        var selected = getSelectedSpecification().get();
        var assembledBuild = getContext().getSpecificationResolver().resolve(selected);
        var player = Player.makeHuman(world, assembledBuild);
        players.addMember(player);
        
        // model must have teams set before WorldCanvas init, as WC relies on getting the player team
        WorldCanvas renderer = new WorldCanvas(
            graph,
            particles,
            new PlayerControls(player.getId(), new LocalExecutor(world))
        );
        
        world.init();

        var hud = new HeadsUpDisplay(graph, player.getId());
        updater.addEndOfFrameListener(hud);
        
        WorldPage wp = new WorldPage(getContext(), getHost(), hud);
        wp.setCanvas(renderer);
        getHost().switchToPage(wp);
        renderer.start();
        updater.start();
    }
}
