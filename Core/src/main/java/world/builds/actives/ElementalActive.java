package world.builds.actives;

import util.Settings;
import world.entities.AbstractPlayer;
import world.entities.ParticleType;
import world.entities.Projectile;
import world.entities.ProjectileBuilder;
import gui.graphics.CustomColors;
import world.Tile;
import world.builds.characterClass.CharacterClass;
import world.builds.characterClass.CharacterStatName;

/**
 * When extending this class, you will want to overide the following methods:
 * <ul>
 * <li>copy</li>
 * <li>doUse</li>
 * </ul>
 *
 * @author Matt Crow
 */
public class ElementalActive extends AbstractActive {

    private final int arcLength;
    private final Range projectileRange;
    private final Range areaOfEffect;
    private final int projectileSpeed;
    private final int damage;

    private final int baseArcLength;
    private final int baseProjectileSpeed;
    private final int baseDamage;

    private ParticleType particleType; // the type of particles this' projectiles emit @see Projectile
    private CustomColors[] colors;

    private static int nextUseId = 0; // How many actives have been used thus far. Used to prevent double hitting

    /**
     * @param n the name of this active
     *
     * @param arc an integer from 1 to 5, denoting how wide of an arc of
     * projectiles this will spawn upon trigger: 1: 45 2: 90 3: 135 4: 180 5:
     * 360
     *
     * @param range how far the projectiles will travel before terminating
     * @param speed 1 to 5. how fast the projectile moves
     * @param aoe 0 to 5. Upon terminating, if this has an aoe (not 0), the
     * terminating projectile will explode into other projectiles, which will
     * travel a distance calculated from this aoe.
     *
     * @param dmg 0 to 5. Upon colliding with a player, this' projectiles will
     * inflict a total of (dmg * (5% of average character's HP)) damage.
     */
    public ElementalActive(String n, int arc, Range range, int speed, Range aoe, int dmg) {
        super(n);

        baseArcLength = util.Number.minMax(0, arc, 5);
        baseProjectileSpeed = util.Number.minMax(1, speed, 5);
        baseDamage = util.Number.minMax(0, dmg, 5);

        this.arcLength = (baseArcLength == 5) ? 360 : baseArcLength * 45;
        projectileRange = range;
        projectileSpeed = Tile.TILE_SIZE * baseProjectileSpeed / Settings.FPS;
        areaOfEffect = aoe;
        damage = baseDamage * CharacterClass.BASE_HP / 20;

        particleType = ParticleType.NONE;
        colors = new CustomColors[]{CustomColors.black};
    }

    /**
     * Returns a copy of this. Note that subclasses should override this method.
     *
     * @return a deep copy of this.
     */
    @Override
    public ElementalActive copy() {
        ElementalActive copy = new ElementalActive(
                getName(),
                getBaseArcLength(),
                projectileRange,
                getBaseProjectileSpeed(),
                areaOfEffect,
                getBaseDamage());
        copy.setParticleType(getParticleType());
        copy.setColors(getColors());
        copyInflictTo(copy);

        return copy;
    }

    public void setColors(CustomColors[] cs) {
        colors = cs.clone();
    }

    public CustomColors[] getColors() {
        return colors;
    }

    // particle methods
    public void setParticleType(ParticleType t) {
        particleType = t;
    }

    public ParticleType getParticleType() {
        return particleType;
    }

    public final int getArcLength() {
        return arcLength;
    }

    public Range getRange() {
        return projectileRange;
    }

    public final int getSpeed() {
        return projectileSpeed;
    }

    public Range getAOE() {
        return areaOfEffect;
    }

    public final int getDamage() {
        return damage;
    }

    public final int getBaseArcLength() {
        return baseArcLength;
    }

    public final int getBaseProjectileSpeed() {
        return baseProjectileSpeed;
    }

    public final int getBaseDamage() {
        return baseDamage;
    }

    /**
     * Creates a projectile at this' user's coordinates
     *
     * @param facingDegrees the direction the new projectile will travel
     */
    protected void spawnProjectile(int facingDegrees) {
        var p = createProjectile();
        p.setFacing(facingDegrees);
        getUser().spawn(p);
    }

    /**
     * Generates an arc of projectiles from this' user
     *
     * @param arcDegrees the number of degrees in the arc
     */
    protected void spawnArc(int arcDegrees) {
        int start = getUser().getFacing().getDegrees() - arcDegrees / 2;
        // spawn projectiles every 15 degrees
        for (int angleOffset = 0; angleOffset < arcDegrees; angleOffset += 15) {
            spawnProjectile(start + angleOffset);
        }
    }

    /**
     * Calculates the damage this should inflict on the given player upon
     * hitting them.
     *
     * @param p
     * @return
     */
    public int calcDmg(AbstractPlayer p) {
        return (int) (damage
                * getUser().getStatValue(CharacterStatName.DMG)
                / p.getStatValue(CharacterStatName.REDUCTION));
    }

    /**
     * Invoked by Projectile upon colliding with a player
     *
     * @param hittingProjectile the Projectile which hit p
     * @param p the AbstractPlayer who one of this Projectiles hit.
     */
    public void hit(Projectile hittingProjectile, AbstractPlayer p) {
        AbstractPlayer user = getUser();
        p.logDamage(calcDmg(p));
        user.getActionRegister().triggerOnHit(p);
        p.getActionRegister().triggerOnHitReceived(user);
        applyEffect(p);
    }

    /**
     * Performs the active ability. Subclasses may want to override this if they
     * want any special custom effects.
     */
    protected void doUse() {
        spawnArc(arcLength);
    }

    @Override
    public final void use() {
        doUse();
        nextUseId++;
    }

    @Override
    public String getDescription() {
        StringBuilder desc = new StringBuilder();

        desc
                .append(getName())
                .append(": \n");
        if (projectileRange == Range.NONE) {
            desc.append(String.format("The user generates an explosion with a %d unit radius", areaOfEffect.getInTiles()));
        } else {
            desc.append("The user launches ");
            if (getArcLength() > 0) {
                desc.append(
                        String.format(
                                "projectiles in a %d degree arc, each traveling ",
                                getArcLength()
                        )
                );
            } else {
                desc.append("a projectile, which travels ");
            }
            desc.append(String.format("for %d units, at %d units per second",
                    projectileRange.getInTiles(),
                    getSpeed() * Settings.FPS / Tile.TILE_SIZE
            )
            );

            if (areaOfEffect != Range.NONE) {
                desc.append(String.format(" before exploding in a %d unit radius", areaOfEffect.getInTiles()));
            }
        }

        desc.append(String.format(" dealing %d damage to enemies it hits. \n", getDamage()));
        desc.append(getInflict().getStatusString());

        return desc.toString();
    }

    public Projectile createProjectile() {
        var p = new ProjectileBuilder()
            .at(getUser())
            .from(this)
            .withUseId(nextUseId)
            .facing(0)
            .withMomentum(projectileSpeed)
            .build();
        return p;
    }
}
