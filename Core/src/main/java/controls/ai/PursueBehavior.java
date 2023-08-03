package controls.ai;

import orpheus.core.world.occupants.players.Player;
import util.Coordinates;

/**
 * target another player, pursuing them until they are terminated
 * @author Matt
 */
public class PursueBehavior extends AbstractBehavior<Player> {
   
    private final PlayerAI host;
    private final Player pursueThisGuy;
    
    public PursueBehavior(PlayerAI host, Player target, Player pursueThisGuy) {
        super(target);
        this.host = host;
        this.pursueThisGuy = pursueThisGuy;
    }

    @Override
    public AbstractBehavior<Player> update() {
        AbstractBehavior<Player> newBehavior = this;
        Player target = getTarget();
        // check if in range
		if(Coordinates.distanceBetween(target, pursueThisGuy) <= 100){
			newBehavior = new AttackBehavior(host, target, pursueThisGuy);
		} else {
            host.setPath(target.getWorld().getMap().findPath(target, pursueThisGuy));
        }
        return newBehavior;
    }

}
