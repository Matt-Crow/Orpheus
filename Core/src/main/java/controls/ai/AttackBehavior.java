package controls.ai;

import util.Coordinates;
import world.entities.AbstractPlayer;

/**
 *
 * @author Matt
 */
public class AttackBehavior extends AbstractBehavior<AbstractPlayer> {
    private final PlayerAI host;
    private final AbstractPlayer attackThisGuy;
    
    public AttackBehavior(PlayerAI host, AbstractPlayer target, AbstractPlayer attackThisGuy) {
        super(target);
        this.host = host;
        this.attackThisGuy = attackThisGuy;
    }

    @Override
    public AbstractBehavior<AbstractPlayer> update() {
        AbstractPlayer target = getTarget();
        AbstractBehavior<AbstractPlayer> newBehavior = this;
        
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
