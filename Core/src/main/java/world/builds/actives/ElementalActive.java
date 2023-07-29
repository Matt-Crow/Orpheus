package world.builds.actives;

import util.Direction;
import world.entities.Explodes;
import world.entities.ParticleGenerator;
import world.entities.Projectile;
import world.entities.ProjectileAttackBehavior;
import world.entities.ProjectileBuilder;
import world.entities.TerminateOnCollide;

import java.util.HashSet;
import java.util.List;

import orpheus.core.world.occupants.players.Player;
import orpheus.core.world.occupants.players.attributes.requirements.ActivationRequirement;
import world.builds.characterClass.CharacterClass;
import world.builds.characterClass.CharacterStatName;

/**
 * @author Matt Crow
 */
public class ElementalActive extends AbstractActive {

    private final Arc arc;
    private final Range projectileRange;
    private final Range areaOfEffect;
    private final Speed projectileSpeed;
    private final Damage damage;

    /**
     * used by this active's projectiles to generate particles
     */
    private ParticleGenerator particleGenerator;
    
    private boolean projectilesTerminateOnHit = false;

    private static HashSet<Player> nextSetOfPlayersHit = new HashSet<>(); // How many actives have been used thus far. Used to prevent double hitting

    /**
     * @param name the name of this active
     * @param arc how wide of an arc of projectiles this will spawn upon trigger
     * @param range how far the projectiles will travel before terminating
     * @param speed how fast the projectile moves
     * @param aoe upon terminating, if this has an aoe, the terminating 
     * projectile will explode into other projectiles, which will travel a 
     * distance calculated from this aoe.
     * @param dmg how much damage this active's projectiles will inflict
     * @param particleGenerator used by projectiles to generate particles
     * @param activationRequirements the conditions which must be met in order 
     *  to use this
     */
    public ElementalActive(
            String name, 
            Arc arc, 
            Range range, 
            Speed speed, 
            Range aoe, 
            Damage dmg, 
            ParticleGenerator particleGenerator,
            ActivationRequirement... activationRequirements
    ) {
        super(name, activationRequirements);
        this.arc = arc;
        projectileRange = range;
        projectileSpeed = speed;
        areaOfEffect = aoe;
        damage = dmg;
        this.particleGenerator = particleGenerator;
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
            arc,
            projectileRange,
            projectileSpeed,
            areaOfEffect,
            damage,
            particleGenerator,
            getActivationRequirements().toArray(ActivationRequirement[]::new)
        );
        copyInflictTo(copy);
        if (projectilesTerminateOnHit) {
            copy.andProjectilesTerminateOnHit();
        }

        return copy;
    }

    protected Arc getArc() {
        return arc;
    }

    protected Speed getSpeed() {
        return projectileSpeed;
    }

    public Range getRange() {
        return projectileRange;
    }

    public Range getAOE() {
        return areaOfEffect;
    }

    protected Damage getDamage() {
        return damage;
    }

    /**
     * @return the generator used to spawn particles for this
     */
    public ParticleGenerator getParticleGenerator() {
        return particleGenerator;
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

    @Override
    protected final void use() {
        doUse();
        nextSetOfPlayersHit = new HashSet<>();
    }

    /**
     * Performs the active ability. Subclasses may want to override this if they
     * want any special custom effects.
     */
    protected void doUse() {
        var arcDegrees = arc.getDegrees();
        var start = getUser().getFacing().rotatedBy(-arcDegrees / 2);
        // spawn projectiles every 15 degrees
        for (int angleOffset = 0; angleOffset <= arcDegrees; angleOffset += 15) {
            spawnProjectile(start.rotatedBy(angleOffset));
        }
    }

    /**
     * Creates a projectile at this' user's coordinates.
     * Subclasses can use withProjectileBuilder to modify this method's behavior
     *
     * @param facing the direction the new projectile will travel
     */
    protected final void spawnProjectile(Direction facing) {
        var attack = makeAttack(); // todo maybe pass this down
        var onCollide = new ProjectileAttackBehavior(attack);
        var builder = new ProjectileBuilder()
            .at(getUser())
            .from(this)
            .onCollide(onCollide)
            .andExploding(new Explodes(areaOfEffect, projectileSpeed, onCollide))
            .havingHitThusFar(nextSetOfPlayersHit)
            .facing(facing)
            .withMomentum(projectileSpeed);
        if (projectilesTerminateOnHit) {
            builder = builder.onCollide(new TerminateOnCollide());
        }
        var updatedBuilder = withProjectileBuilder(builder);
        var p = updatedBuilder.build();
        modifyProjectile(p);
        getUser().spawn(p);
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

    /**
     * Subclasses should override this method if they wish to modify the
     * projectile after it has been built
     */
    protected void modifyProjectile(Projectile projectile) {

    }

    private Attack makeAttack() {
        return new Attack(
            getComputedDamage() * getUser().getStatValue(CharacterStatName.DMG), 
            List.of(getInflict().getStatuses())
        );
    }

    /**
     * @return the amount of damage this inflicts on hit
     */
    private double getComputedDamage() {
        return damage.getPercentage() * CharacterClass.BASE_HP;
    }

    @Override
    public String getDescription() {
        StringBuilder desc = new StringBuilder();

        if (projectileRange == Range.NONE) {
            desc.append(String.format("The user generates an explosion with a %d unit radius", areaOfEffect.getInTiles()));
        } else {
            desc.append("The user launches ");
            if (arc != Arc.NONE) {
                desc.append(String.format(
                    "projectiles in a %d degree arc, each traveling ",
                    arc.getDegrees()
                ));
            } else {
                desc.append("a projectile, which travels ");
            }
            desc.append(String.format("for %d units, at %d tiles per second",
                projectileRange.getInTiles(),
                projectileSpeed.getInTilesPerSecond()
            ));

            if (areaOfEffect != Range.NONE) {
                desc.append(String.format(" before exploding in a %d unit radius", areaOfEffect.getInTiles()));
            }
        }
        desc.append(String.format(" dealing %3.2f damage to enemies it hits. \n", getComputedDamage()));

        var statuses = getInflict();
        if (!statuses.isEmpty()) {
            desc.append(statuses.getStatusString());
        }

        return desc.toString();
    }
}
