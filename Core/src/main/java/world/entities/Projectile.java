package world.entities;

import java.util.Optional;

import orpheus.core.utils.coordinates.Point;
import orpheus.core.utils.coordinates.TerminablePointUpdater;
import orpheus.core.world.occupants.WorldOccupant;
import util.Direction;

public class Projectile extends WorldOccupant {

    /**
     * controls this thing's movement in the world - might pass down
     */
    private final TerminablePointUpdater movement;

    /**
     * determines what kind of particles this emits
     */
    private final ParticleGenerator particles;

    /**
     * determine how this will react upon colliding with a player
     */
    private final Optional<ProjectileCollideBehavior> collideBehavior;

    /**
     * determines whether this explodes on termination
     */
    private final Optional<Explodes> explodes;

    /**
     * Projectiles with the same useId cannot hit the same target.
     * This prevents double-hitting.
     */
    private final int useId; 

    protected Projectile(
            int useId, 
            Point coordinates,
            Direction facing,
            TerminablePointUpdater movement,
            AbstractPlayer user,
            ParticleGenerator particles, 
            Optional<ProjectileCollideBehavior> collideBehavior,
            Optional<Explodes> explodes
    ) {
        super(user.getWorld());
        init();
        setCoordinates(coordinates);
        setFacing(facing); // still needed by BoulderToss
        setTeam(user.getTeam());
        setRadius(25);
        this.movement = movement;
        this.particles = particles;
        this.collideBehavior = collideBehavior;
        this.explodes = explodes;
        this.useId = useId;
    }

    /**
     * Used to prevent double-hitting
     * @return a unique identifier for the attack instance that spawned this
     */
    public int getUseId() {
        return useId;
    }

    /**
     * Hits the given player if this is colliding with them
     * @param p the player to check if this is colliding with
     */
    public void hitIfColliding(AbstractPlayer p) {
        if (isCollidingWith(p) && p.getLastHitById() != useId) {
            hit(p);
        }
    }

    private void hit(AbstractPlayer p) {
        collideBehavior.ifPresent(b -> b.collidedWith(this, p));
        p.wasHitBy(this);
        getActionRegister().triggerOnHit(p);
        terminate();
    }

    @Override
    public void update() {
        super.update();
        if (movement.isDone() && !isTerminating()) {
            terminate();
        }
    }

    @Override
    protected void updateMovement() {
        // do not call super method
        movement.update(getCoordinates());
    }

    @Override
    public void terminate() {
        super.terminate();
        explodes.ifPresent(e -> e.explode(this));
    }

    @Override
    public orpheus.core.world.graph.Projectile toGraph() {
        return new orpheus.core.world.graph.Projectile(
            getX(),
            getY(),
            getRadius(),
            getFacing().copy(),
            particles.getColors(),
            particles.getType()
        );
    }
}
