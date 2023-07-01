package world.builds.actives;

public class MeleeActive extends ElementalActive {

    public MeleeActive(String n, int arc, int speed, Range aoe, int dmg) {
        super(n, arc, Range.MELEE, speed, aoe, dmg);
    }
    
    @Override
    protected void doUse() {
        super.doUse();
        getUser().getActionRegister().triggerOnUseMelee(this);
    }
}
