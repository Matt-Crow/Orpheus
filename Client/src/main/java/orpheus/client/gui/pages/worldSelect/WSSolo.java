package orpheus.client.gui.pages.worldselect;

import world.battle.Team;
import orpheus.client.ClientAppContext;
import orpheus.client.gui.pages.play.WorldGraphSupplier;
import orpheus.client.gui.pages.play.WorldPage;
import orpheus.core.commands.executor.LocalExecutor;
import orpheus.core.utils.timer.FrameTimer;
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
        
        // remember: use different FrameTimers to draw and update the world
        // WorldPage handles the drawing part
        // #65 allow this to stop
        new FrameTimer(e -> world.update()).start();
        
        var selected = getSelectedSpecification().get();
        var assembledBuild = getContext().getSpecificationResolver().resolve(selected);
        var me = Player.makeHuman(world, assembledBuild);
        players.addMember(me);
        world.init();

        WorldPage wp = new WorldPage(
            getContext(), 
            getHost(), 
            WorldGraphSupplier.fromWorld(world),
            me.getId(),
            new LocalExecutor(world)
        );
        getHost().switchToPage(wp);
    }
}
