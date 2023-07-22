package world.entities;

import java.util.HashSet;
import java.util.Optional;

import orpheus.core.utils.coordinates.Point;
import orpheus.core.utils.coordinates.TerminablePointUpdater;
import orpheus.core.world.occupants.WorldOccupant;
import orpheus.core.world.occupants.players.Player;
import util.Direction;

public class Projectile extends WorldOccupant {

    /**
     * the player who spawned this projectile
     */
    private final Player spawnedBy;

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
     * The players who have already been hit by one of the projectiles spawned
     * by the attack which spawned this one. Sibling projectiles share a 
     * reference to the same hashset so if one collides with a player, its
     * siblings will not be able to hit that player. This prevents 
     * double-hitting.
     */
    private final HashSet<Player> hitThusFar; 

    protected Projectile(
            HashSet<Player> attackedPlayers, 
            Point coordinates,
            Direction facing,
            TerminablePointUpdater movement,
            Player user,
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
        this.spawnedBy = user;
        this.movement = movement;
        this.particles = particles;
        this.collideBehavior = collideBehavior;
        this.explodes = explodes;
        this.hitThusFar = attackedPlayers;
    }

    /**
     * Used to prevent double-hitting
     * @return a shared reference to the list of players hit thus far
     */
    public HashSet<Player> getPlayersHitThusFar() {
        return hitThusFar;
    }

    /**
     * Hits the given player if this is colliding with them
     * @param p the player to check if this is colliding with
     */
    public void hitIfColliding(Player p) {
        if (!hitThusFar.contains(p) && isCollidingWith(p)) {
            hit(p);
        }
    }

    private void hit(Player p) {
        collideBehavior.ifPresent(b -> b.collidedWith(this, p));
        p.wasHitBy(spawnedBy, this);
        hitThusFar.add(p);
        getActionRegister().triggerOnHit(p);
        spawnedBy.getActionRegister().triggerOnHit(p);
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
