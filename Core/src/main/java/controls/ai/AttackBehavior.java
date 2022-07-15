package controls.ai;

import util.Coordinates;
import world.entities.AbstractPlayer;

/**
 *
 * @author Matt
 */
public class AttackBehavior extends AbstractBehavior<AbstractPlayer> {
    private final AbstractPlayer attackThisGuy;
    
    public AttackBehavior(AbstractPlayer target, AbstractPlayer attackThisGuy) {
        super(target);
        this.attackThisGuy = attackThisGuy;
    }

    @Override
    public AbstractBehavior<AbstractPlayer> update() {
        AbstractPlayer target = getTarget();
        AbstractBehavior<AbstractPlayer> newBehavior = this;
        
        if(attackThisGuy.getShouldTerminate()){
            newBehavior = new WanderBehavior(target);
        } else if(Coordinates.distanceBetween(target, attackThisGuy) >= 100){
            // out of range
            newBehavior = new PursueBehavior(target, attackThisGuy);
        } else {
            target.setFocus(attackThisGuy);
            target.useMeleeAttack();
            target.setIsMoving(false);
        }
        
        return newBehavior;
    }

}
