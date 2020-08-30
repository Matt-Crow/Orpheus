package customizables.actives;

import entities.ParticleType;
import events.OnUpdateEvent;
import events.OnUpdateListener;
import events.Terminable;
import events.TerminateListener;
import gui.graphics.CustomColors;
import statuses.Rush;
import util.SafeList;

/**
 *
 * @author Matt
 */
public class FlameCharge extends ElementalActive {
    public FlameCharge(){
        super("Flame Charge", 1, 1, 5, 0, 1);
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
        class TermUpdate implements OnUpdateListener, Terminable {
            private final SafeList<TerminateListener> termListens;
            private int timeLeft;
            public TermUpdate(int time){
                termListens = new SafeList<>();
                timeLeft = time;
            }
            
            @Override
            public void trigger(OnUpdateEvent e) {
                spawnProjectile(e.getUpdated().getDir().getDegrees() + 180);
                timeLeft--;
                if(timeLeft <= 0){
                    terminate();
                }
            }

            @Override
            public void addTerminationListener(TerminateListener listen) {
                termListens.add(listen);
            }

            @Override
            public boolean removeTerminationListener(TerminateListener listen) {
                return termListens.remove(listen);
            }

            @Override
            public void terminate() {
                termListens.forEach((l)->l.objectWasTerminated(this));
            }
            
        }
        OnUpdateListener listen = new TermUpdate(status.getUsesLeft());
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
