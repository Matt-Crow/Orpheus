package world.entities;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

import orpheus.core.utils.coordinates.Point;
import orpheus.core.utils.coordinates.PolarVector;
import orpheus.core.utils.coordinates.TerminablePointUpdater;
import orpheus.core.utils.coordinates.TerminableVectorPointUpdater;
import orpheus.core.world.occupants.WorldOccupant;
import orpheus.core.world.occupants.players.Player;
import util.Direction;
import world.builds.actives.ElementalActive;
import world.builds.actives.Range;

public class ProjectileBuilder {
    
    private Point coordinates = new Point();
    private Optional<Integer> useId = Optional.empty();
    private Optional<Direction> facing = Optional.empty();
    private Optional<Integer> momentum = Optional.empty();
    private Range range = Range.MEDIUM;
    private Optional<TerminablePointUpdater> movement = Optional.empty();
    private Optional<Player> user = Optional.empty();
    private ParticleGenerator particles = ParticleGenerator.NONE;
    private Optional<ProjectileCollideBehavior> collideBehavior = Optional.empty();
    private Optional<Explodes> explodes = Optional.empty();

    public ProjectileBuilder withUser(Player user) {
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
        return at(occupant.getCoordinates().copy()) // need to copy coordinates
            .facing(occupant.getFacing());
    }

    public ProjectileBuilder at(Point coordinates) {
        this.coordinates = coordinates;
        return this;
    }

    public ProjectileBuilder facing(Direction facing) {
        this.facing = Optional.of(facing.copy());
        return this;
    }

    public ProjectileBuilder withMomentum(int momentum) {
        this.momentum = Optional.of(momentum);
        return this;
    }

    public ProjectileBuilder withMovement(TerminablePointUpdater movement) {
        this.movement = Optional.of(movement);
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
            .withRange(active.getRange())
            .withParticles(new ParticleGenerator(active.getColors(), active.getParticleType()))
            .onCollide(new ProjectileAttackBehavior(active));
    }

    public ProjectileBuilder exploded(ElementalActive active) {
        explodes = Optional.empty();
        return withUser(active.getUser())
            .withRange(active.getAOE())
            .withParticles(new ParticleGenerator(active.getColors(), active.getParticleType()))
            .onCollide(new ProjectileAttackBehavior(active));
    }

    public Projectile build() {
        var p = new Projectile(
            useId.orElseThrow(required("useId")),
            coordinates, 
            facing.orElseThrow(required("facing")),
            movement.orElse(new TerminableVectorPointUpdater(new PolarVector(momentum.get(), facing.get().copy()), range.getInPixels())),
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
