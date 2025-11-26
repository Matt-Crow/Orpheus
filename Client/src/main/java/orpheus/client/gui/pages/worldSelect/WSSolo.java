package orpheus.client.gui.pages.worldselect;

import orpheus.client.gui.pages.PlayerControls;
import world.battle.Team;
import orpheus.client.ClientAppContext;
import orpheus.client.gui.pages.play.HeadsUpDisplay;
import orpheus.client.gui.pages.play.WorldCanvas;
import orpheus.client.gui.pages.play.WorldGraphSupplier;
import orpheus.client.gui.pages.play.WorldPage;
import orpheus.core.commands.executor.LocalExecutor;
import orpheus.core.utils.timer.FrameTimer;
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
        
        var graph = WorldGraphSupplier.fromWorld(world);
        var particles = new Particles();

        // remember: use different FrameTimers to draw and update the world
        // WorldPage handles the drawing part
        // TODO allow this to stop
        new FrameTimer(e -> world.update()).start();
        
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

        WorldPage wp = new WorldPage(
            getContext(), 
            getHost(), 
            new HeadsUpDisplay(graph, player.getId()), 
            graph,
            particles
        );
        wp.setCanvas(renderer);
        getHost().switchToPage(wp);
        renderer.start();
    }
}
