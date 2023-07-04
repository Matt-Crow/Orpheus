package world.builds.actives;

import world.entities.ParticleType;

public class MeleeActive extends ElementalActive {

    public MeleeActive(String n, Arc arc, int speed, Range aoe, int dmg) {
        super(n, arc, Range.MELEE, speed, aoe, dmg);
    }

    /**
     * @return the default basic attack which all characters can use
     */
    public static MeleeActive makeBasicAttack() {
        var result = new MeleeActive("Slash", Arc.NONE, 5, Range.NONE, 3);
        result.setParticleType(ParticleType.SHEAR);
        return result;
    }

    @Override
    public MeleeActive copy() {
        return new MeleeActive(getName(), getArc(), getBaseProjectileSpeed(), getAOE(), getBaseDamage());
    }
    
    @Override
    protected void doUse() {
        super.doUse();
        getUser().getActionRegister().triggerOnUseMelee(this);
    }
}
