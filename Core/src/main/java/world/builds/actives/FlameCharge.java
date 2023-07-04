package world.builds.actives;

import world.entities.ParticleType;
import world.events.EventListener;
import world.events.OnUpdateEvent;
import world.events.termination.*;
import gui.graphics.CustomColors;
import world.statuses.Rush;

/**
 *
 * @author Matt
 */
public class FlameCharge extends ElementalActive {
    public FlameCharge(){
        super("Flame Charge", Arc.NONE, Range.MELEE, 5, Range.NONE, 1);
        setColors(CustomColors.fireColors);
        setParticleType(ParticleType.SHEAR);
    }
    
    @Override
    public FlameCharge copy(){
        return new FlameCharge();
    }
    
    @Override
    protected final void doUse(){
        Rush status = new Rush(2, 3);
        
        // Need this to dual-implement these two interfaces. Probably a better way.
        class TermUpdate implements EventListener<OnUpdateEvent>, Terminable {
            private final TerminationListeners terminationListeners = new TerminationListeners();
            private int timeLeft;

            public TermUpdate(int time){
                timeLeft = time;
            }
            
            @Override
            public void handle(OnUpdateEvent e) {
                spawnProjectile(e.getUpdated().getFacing().rotatedBy(180));
                timeLeft--;
                if(timeLeft <= 0){
                    terminate();
                }
            }

            @Override
            public void addTerminationListener(TerminationListener listen) {
                terminationListeners.add(listen);
            }

            @Override
            public void terminate() {
                terminationListeners.objectWasTerminated(this);
            }

            @Override
            public boolean isTerminating() {
                return timeLeft <= 0;
            }
            
        }
        EventListener<OnUpdateEvent> listen = new TermUpdate(status.getUsesLeft());
        getUser().getActionRegister().addOnUpdate(listen);
        getUser().inflict(status);
    }
    
    @Override
    public String getDescription(){
        StringBuilder b = new StringBuilder();
        b.append(getName()).append(": \n");
        b.append("The user gains a temporary speed boost, ");
        b.append("Dealing damage to enemies caught behind them as they charge.");
        return b.toString();
    }
}
