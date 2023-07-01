package world.builds.actives;

import world.entities.AbstractPlayer;
import world.entities.ParticleType;

public class MeleeActive extends ElementalActive {

    public MeleeActive(String n, Arc arc, int speed, Range aoe, int dmg) {
        super(n, arc, Range.MELEE, speed, aoe, dmg);
    }

    public static MeleeActive makeBasicAttack() {
        var result = new MeleeActive("Slash", Arc.NONE, 5, Range.NONE, 3);
        result.setParticleType(ParticleType.SHEAR);
        return result;
    }

    public static MeleeActive makeBasicAttack(AbstractPlayer user) {
        var result = makeBasicAttack();
        result.setUser(user);
        return result;
    }
    
    @Override
    protected void doUse() {
        super.doUse();
        getUser().getActionRegister().triggerOnUseMelee(this);
    }
}
