package world.entities;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
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
import world.builds.actives.Speed;

public class ProjectileBuilder {
    
    private Point coordinates = new Point();
    private Optional<HashSet<Player>> hitThusFar = Optional.empty();
    private Optional<Direction> facing = Optional.empty();
    private Speed momentum = Speed.IMMOBILE;
    private Range range = Range.MEDIUM;
    private Optional<TerminablePointUpdater> movement = Optional.empty();
    private Optional<Player> user = Optional.empty();
    private ParticleGenerator particles = ParticleGenerator.NONE;
    private List<ProjectileCollideBehavior> collideBehaviors = new ArrayList<>();
    private Optional<Explodes> explodes = Optional.empty();


    public ProjectileBuilder() {

    }

    private ProjectileBuilder(
        Point coordinates, 
        Optional<HashSet<Player>> hitThusFar,
        Optional<Direction> facing,
        Speed momentum,
        Range range,
        Optional<TerminablePointUpdater> movement,
        Optional<Player> user,
        ParticleGenerator particles,
        List<ProjectileCollideBehavior> collideBehaviors,
        Optional<Explodes> explodes
    ) {
        this();
        this.coordinates = coordinates;
        this.hitThusFar = hitThusFar;
        this.facing = facing;
        this.momentum = momentum;
        this.range = range;
        this.movement = movement;
        this.user = user;
        this.particles = particles;
        this.collideBehaviors = collideBehaviors;
        this.explodes = explodes;
    }

    public ProjectileBuilder copy() {
        // some of these don't support deep copying, some don't need it
        return new ProjectileBuilder(
            coordinates.copy(), 
            hitThusFar.map(HashSet::new), 
            facing.map(Direction::copy), 
            momentum, 
            range, 
            movement, 
            user, 
            particles, 
            collideBehaviors.stream().toList(), 
            explodes
        );
    }

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

    public ProjectileBuilder withMomentum(Speed momentum) {
        this.momentum = momentum;
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
    public ProjectileBuilder havingHitThusFar(HashSet<Player> useId) {
        this.hitThusFar = Optional.of(useId);
        return this;
    }

    /**
     * @return the builder, updated with an empty list of players hit 
     */
    public ProjectileBuilder havingHitNoPlayersYet() {
        return havingHitThusFar(new HashSet<>());
    }

    public ProjectileBuilder withParticles(ParticleGenerator particles) {
        this.particles = particles;
        return this;
    }

    /**
     * Adds the given behavior to the list of behaviors the built projectile
     * will do upon colliding with a player.
     * @param collideBehavior the behavior to add
     * @return the builder, updated with the added collision behavior
     */
    public ProjectileBuilder onCollide(ProjectileCollideBehavior collideBehavior) {
        collideBehaviors.add(collideBehavior);
        return this;
    }

    /**
     * Specifies that the projectile will explode upon terminating
     * @param explodes the explosion behavior to use upon terminating
     * @return the builder, updated with the added explosion behavior
     */
    public ProjectileBuilder andExploding(Explodes explodes) {
        this.explodes = Optional.of(explodes);
        return this;
    }

    public ProjectileBuilder from(ElementalActive active) {
        return withUser(active.getUser())
            .andExploding(new Explodes(active))
            .withParticles(active.getParticleGenerator())
            .onCollide(new ProjectileAttackBehavior(active));
    }

    public ProjectileBuilder exploded(ElementalActive active) {
        explodes = Optional.empty();
        return withUser(active.getUser())
            .withParticles(active.getParticleGenerator())
            .onCollide(new ProjectileAttackBehavior(active));
    }

    public Projectile build() {
        var p = new Projectile(
            hitThusFar.orElseThrow(required("hitThusFar")),
            coordinates, 
            facing.orElseThrow(required("facing")),
            movement.orElse(new TerminableVectorPointUpdater(new PolarVector(momentum.getInPixelsPerFrame(), facing.get().copy()), range.getInPixels())),
            user.orElseThrow(required("user")),
            particles,
            collideBehaviors,
            explodes
        );
        return p;
    }

    private Supplier<NoSuchElementException> required(String field) {
        return () -> new NoSuchElementException(field + " is required");
    }
}
