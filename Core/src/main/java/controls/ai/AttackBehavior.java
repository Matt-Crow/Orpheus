package controls.ai;

import orpheus.core.world.occupants.players.Player;
import util.Coordinates;

/**
 *
 * @author Matt
 */
public class AttackBehavior extends AbstractBehavior<Player> {
    private final PlayerAI host;
    private final Player attackThisGuy;
    
    public AttackBehavior(PlayerAI host, Player target, Player attackThisGuy) {
        super(target);
        this.host = host;
        this.attackThisGuy = attackThisGuy;
    }

    @Override
    public AbstractBehavior<Player> update() {
        Player target = getTarget();
        AbstractBehavior<Player> newBehavior = this;
        
        if(attackThisGuy.isTerminating()){
            newBehavior = new WanderBehavior(host, target);
        } else if(Coordinates.distanceBetween(target, attackThisGuy) >= 100){
            // out of range
            newBehavior = new PursueBehavior(host, target, attackThisGuy);
        } else {
            host.setFocus(attackThisGuy);
            target.useMeleeAttack();
            target.setMoving(false);
        }
        
        return newBehavior;
    }

}
