package world.builds.actives;

import gui.graphics.CustomColors;
import world.entities.ParticleGenerator;
import world.entities.ParticleType;
import world.events.OnUseMeleeEvent;

public class MeleeActive extends ElementalActive {

    public MeleeActive(String n, Arc arc, Speed speed, Range aoe, Damage dmg, ParticleGenerator particleGenerator) {
        super(n, arc, Range.MELEE, speed, aoe, dmg, particleGenerator);
    }

    /**
     * @return the default basic attack which all characters can use
     */
    public static MeleeActive makeBasicAttack() {
        return new MeleeActive(
            "Slash", 
            Arc.NONE, 
            Speed.FAST, 
            Range.NONE, 
            Damage.MEDIUM, 
            new ParticleGenerator(CustomColors.METAL, ParticleType.SHEAR)
        );
    }

    @Override
    public MeleeActive copy() {
        return new MeleeActive(getName(), getArc(), getSpeed(), getAOE(), getDamage(), getParticleGenerator());
    }
    
    @Override
    protected void doUse() {
        super.doUse();
        var event = new OnUseMeleeEvent(getUser(), this);
        getUser().eventOnUseMelee().handle(event);
    }
}
