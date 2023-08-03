package world.builds.actives;

import world.entities.ParticleGenerator;
import world.entities.ParticleType;
import world.events.EventListener;
import world.events.OnUpdateEvent;
import gui.graphics.CustomColors;
import world.statuses.Rush;

/**
 *
 * @author Matt
 */
public class FlameCharge extends ElementalActive {
    public FlameCharge(){
        super(
            "Flame Charge", 
            Arc.NONE, 
            Range.MELEE, 
            Speed.FAST, 
            Range.NONE, 
            Damage.LOW, 
            new ParticleGenerator(CustomColors.FIRE, ParticleType.SHEAR)
        );
    }
    
    @Override
    public FlameCharge copy(){
        return new FlameCharge();
    }
    
    @Override
    protected final void doUse(){
        var attack = makeAttack();

        Rush status = new Rush(2, 3);
        
        class TermUpdate implements EventListener<OnUpdateEvent> {
            private int timeLeft;

            public TermUpdate(int time){
                timeLeft = time;
            }
            
            @Override
            public void handle(OnUpdateEvent e) {
                spawnProjectile(attack, e.getUpdated().getFacing().rotatedBy(180));
                timeLeft--;
            }

            @Override
            public boolean isDone() {
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
        b.append("The user gains a temporary speed boost, ");
        b.append("dealing damage to enemies caught behind them as they charge.");
        return b.toString();
    }
}
