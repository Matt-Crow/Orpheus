package world.entities;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

import orpheus.core.world.occupants.WorldOccupant;
import world.builds.actives.ElementalActive;
import world.builds.actives.Range;

public class ProjectileBuilder {
    
    private Optional<Integer> useId = Optional.empty();
    private Optional<Integer> x = Optional.empty();
    private Optional<Integer> y = Optional.empty();
    private Optional<Integer> degrees = Optional.empty();
    private Optional<Integer> momentum = Optional.empty();
    private Range range = Range.MEDIUM;
    private Optional<AbstractPlayer> user = Optional.empty();
    private ParticleGenerator particles = ParticleGenerator.NONE;
    private Optional<ProjectileCollideBehavior> collideBehavior = Optional.empty();
    private Optional<Explodes> explodes = Optional.empty();

    public ProjectileBuilder withUser(AbstractPlayer user) {
        this.user = Optional.of(user);
        return this;
    }

    /**
     * Designates that the projectile should spawn at the given world occupant's
     * location.
     * 
     * @param occupant the occupant who is located at where this should spawn a
     *  projectile.
     * @return the updated builder
     */
    public ProjectileBuilder at(WorldOccupant occupant) {
        return at(occupant.getX(), occupant.getY())
            .facing(occupant.getFacing().getDegrees());
    }

    public ProjectileBuilder at(int x, int y) {
        this.x = Optional.of(x);
        this.y = Optional.of(y);
        return this;
    }

    public ProjectileBuilder facing(int degrees) {
        this.degrees = Optional.of(degrees);
        return this;
    }

    public ProjectileBuilder withMomentum(int momentum) {
        this.momentum = Optional.of(momentum);
        return this;
    }

    public ProjectileBuilder withRange(Range range) {
        this.range = range;
        return this;
    }

    /**
     * @param useId a unique ID for the use active which generated this
     * @return the updated builder
     */
    public ProjectileBuilder withUseId(int useId) {
        this.useId = Optional.of(useId);
        return this;
    }

    /**
     * @return the builder, updated with a new use ID 
     */
    public ProjectileBuilder withNewUseId() {
        return withUseId(UUID.randomUUID().hashCode());
    }

    public ProjectileBuilder withParticles(ParticleGenerator particles) {
        this.particles = particles;
        return this;
    }

    public ProjectileBuilder onCollide(ProjectileCollideBehavior collideBehavior) {
        this.collideBehavior = Optional.of(collideBehavior);
        return this;
    }

    public ProjectileBuilder from(ElementalActive active) {
        explodes = Optional.of(new Explodes(active));
        return withUser(active.getUser())
            .withRange(active.getRange()) // todo allow AOE
            .withParticles(new ParticleGenerator(Arrays.asList(active.getColors()), active.getParticleType()))
            .onCollide(new ProjectileAttackBehavior(active));
    }

    public Projectile build() {
        var p = new Projectile(
            useId.orElseThrow(required("useId")),
            x.orElseThrow(required("x")),
            y.orElseThrow(required("y")), 
            degrees.orElseThrow(required("facing")), 
            momentum.orElseThrow(required("momentum")), 
            range,
            user.orElseThrow(required("user")),
            particles,
            collideBehavior,
            explodes
        );
        return p;
    }

    private Supplier<NoSuchElementException> required(String field) {
        return () -> new NoSuchElementException(field + " is required");
    }
}
