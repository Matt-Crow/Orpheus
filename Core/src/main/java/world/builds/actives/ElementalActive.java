package world.builds.actives;

import util.Direction;
import util.Settings;
import world.entities.ParticleType;
import world.entities.Projectile;
import world.entities.ProjectileBuilder;
import world.entities.TerminateOnCollide;

import java.awt.Color;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import gui.graphics.CustomColors;
import orpheus.core.world.occupants.players.Player;
import orpheus.core.world.occupants.players.attributes.requirements.ActivationRequirement;
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
    private final Arc arcLength2;
    private final Range projectileRange;
    private final Range areaOfEffect;
    private final int projectileSpeed;
    private final int damage;

    private final int baseProjectileSpeed;
    private final int baseDamage;

    private ParticleType particleType; // the type of particles this' projectiles emit @see Projectile
    private List<Color> colors = List.of();
    private boolean projectilesTerminateOnHit = false;

    private static HashSet<Player> nextSetOfPlayersHit = new HashSet<>(); // How many actives have been used thus far. Used to prevent double hitting

    /**
     * @param n the name of this active
     * @param arc how wide of an arc of projectiles this will spawn upon trigger
     * @param range how far the projectiles will travel before terminating
     * @param speed 1 to 5. how fast the projectile moves
     * @param aoe 0 to 5. Upon terminating, if this has an aoe (not 0), the
     * terminating projectile will explode into other projectiles, which will
     * travel a distance calculated from this aoe.
     * @param dmg 0 to 5. Upon colliding with a player, this' projectiles will
     * inflict a total of (dmg * (5% of average character's HP)) damage.
     * @param activationRequirements the conditions which must be met in order 
     *  to use this
     */
    public ElementalActive(String n, Arc arc, Range range, int speed, Range aoe, int dmg, ActivationRequirement... activationRequirements) {
        super(n, activationRequirements);
        this.arcLength2 = arc;
        baseProjectileSpeed = util.Number.minMax(1, speed, 5);
        baseDamage = util.Number.minMax(0, dmg, 5);

        this.arcLength = arc.getDegrees();
        projectileRange = range;
        projectileSpeed = Tile.TILE_SIZE * baseProjectileSpeed / Settings.FPS;
        areaOfEffect = aoe;
        damage = baseDamage * CharacterClass.BASE_HP / 20;

        particleType = ParticleType.NONE;
        colors = List.of(Color.BLACK);
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
                arcLength2,
                projectileRange,
                getBaseProjectileSpeed(),
                areaOfEffect,
                getBaseDamage());
        copy.setParticleType(getParticleType());
        copy.setColors(getColors());
        copyInflictTo(copy);
        if (projectilesTerminateOnHit) {
            copy.andProjectilesTerminateOnHit();
        }

        return copy;
    }

    public void setColors(Collection<Color> colors) {
        this.colors = List.copyOf(colors);
    }

    public void setColors(CustomColors[] cs) {
        colors = List.of(cs);
    }

    public List<Color> getColors() {
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

    public Arc getArc() {
        return arcLength2;
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

    public final int getBaseProjectileSpeed() {
        return baseProjectileSpeed;
    }

    public final int getBaseDamage() {
        return baseDamage;
    }

    /**
     * Modifies this such that the projectiles it spawns terminate upon hitting
     * a player.
     * @return this, for chaining
     */
    public ElementalActive andProjectilesTerminateOnHit() {
        projectilesTerminateOnHit = true;
        return this;
    }

    /**
     * Creates a projectile at this' user's coordinates
     *
     * @param facing the direction the new projectile will travel
     */
    protected void spawnProjectile(Direction facing) {
        var p = createProjectile(facing);
        getUser().spawn(p);
    }

    /**
     * Generates an arc of projectiles from this' user
     *
     * @param arcDegrees the number of degrees in the arc
     */
    private void spawnArc() {
        var arcDegrees = arcLength2.getDegrees();
        var start = getUser().getFacing().rotatedBy(-arcDegrees / 2);
        // spawn projectiles every 15 degrees
        for (int angleOffset = 0; angleOffset <= arcDegrees; angleOffset += 15) {
            spawnProjectile(start.rotatedBy(angleOffset));
        }
    }

    /**
     * Calculates the damage this should inflict on the given player upon
     * hitting them.
     *
     * @param p
     * @return
     */
    public int calcDmg(Player p) {
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
    public void hit(Projectile hittingProjectile, Player p) {
        p.takeDamage(calcDmg(p));
        applyEffect(p);
    }

    /**
     * Performs the active ability. Subclasses may want to override this if they
     * want any special custom effects.
     */
    protected void doUse() {
        spawnArc();
    }

    @Override
    protected final void use() {
        doUse();
        nextSetOfPlayersHit = new HashSet<>();
    }

    @Override
    public String getDescription() {
        StringBuilder desc = new StringBuilder();

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

        var statuses = getInflict();
        if (!statuses.isEmpty()) {
            desc.append(statuses.getStatusString());
        }

        return desc.toString();
    }

    // todo make private
    public Projectile createProjectile(Direction facing) {
        var builder = new ProjectileBuilder()
            .at(getUser())
            .from(this)
            .havingHitThusFar(nextSetOfPlayersHit)
            .facing(facing)
            .withMomentum(projectileSpeed);
        if (projectilesTerminateOnHit) {
            builder = builder.onCollide(new TerminateOnCollide());
        }
        var updatedBuilder = withProjectileBuilder(builder);
        var p = updatedBuilder.build();
        return p;
    }

    /**
     * Subclasses should override this method if they wish to modify the 
     * projectile being built as part of an attack.
     * 
     * @param builder currently being used to build a projectile
     * @return the updated builder
     */
    protected ProjectileBuilder withProjectileBuilder(ProjectileBuilder builder) {
        return builder;
    }
}
