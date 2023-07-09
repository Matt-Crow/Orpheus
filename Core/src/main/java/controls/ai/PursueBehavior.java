package controls.ai;

import util.Coordinates;
import world.entities.AbstractPlayer;

/**
 * target another player, pursuing them until they are terminated
 * @author Matt
 */
public class PursueBehavior extends AbstractBehavior<AbstractPlayer> {
   
    private final PlayerAI host;
    private final AbstractPlayer pursueThisGuy;
    
    public PursueBehavior(PlayerAI host, AbstractPlayer target, AbstractPlayer pursueThisGuy) {
        super(target);
        this.host = host;
        this.pursueThisGuy = pursueThisGuy;
    }

    @Override
    public AbstractBehavior<AbstractPlayer> update() {
        AbstractBehavior<AbstractPlayer> newBehavior = this;
        AbstractPlayer target = getTarget();
        // check if in range
		if(Coordinates.distanceBetween(target, pursueThisGuy) <= 100){
			newBehavior = new AttackBehavior(host, target, pursueThisGuy);
		} else {
            host.setPath(target.getWorld().getMap().findPath(target, pursueThisGuy));
        }
        return newBehavior;
    }

}
