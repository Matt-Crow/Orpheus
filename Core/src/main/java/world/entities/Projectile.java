package world.entities;

import java.util.Optional;

import orpheus.core.world.occupants.WorldOccupant;
import world.World;
import world.builds.actives.ElementalActive;
import world.builds.actives.Range;

public class Projectile extends WorldOccupant {

    /**
     * determines what kind of particles this emits
     */
    private final ParticleGenerator particles;

    /**
     * determine how this will react upon colliding with a player
     */
    private final Optional<ProjectileCollideBehavior> collideBehavior;

    /**
     * how far this will travel before terminating
     */
    private final Range range;

    /**
     * determines whether this explodes on termination
     */
    private final Optional<Explodes> explodes;

    private double distanceTraveled;

    private final int useId; //used to prevent double hitting. May not be unique to a single projectile. See AbstractActive for more info


    protected Projectile(
            int useId, 
            int x, 
            int y, 
            int degrees, 
            int momentum, 
            Range range,
            AbstractPlayer user,
            ParticleGenerator particles, 
            Optional<ProjectileCollideBehavior> collideBehavior,
            Optional<Explodes> explodes
    ) {
        
        super(user.getWorld());

        this.particles = particles;

        setBaseSpeed(momentum);
        init();
        setX(x);
        setY(y);
        setFacing(degrees);
        this.useId = useId;
        distanceTraveled = 0;
        setTeam(user.getTeam());
        setRadius(25);
        setMoving(true);

        this.range = range;
        this.collideBehavior = collideBehavior;
        this.explodes = explodes;
    }

    /**
     * Creates a projectile that has exploded from another projectile, and thus
     * can no longer explode.
     */
    public static Projectile explosion(World inWorld, int useId, int x, int y, 
        int angle, int momentum, ElementalActive from) {
        
        var p = new Projectile(
            useId, 
            x, 
            y, 
            angle, 
            momentum,
            from.getAOE(),
            from.getUser(),
            new ParticleGenerator(from.getColors(), from.getParticleType()), 
            Optional.of(new ProjectileAttackBehavior(from)),
            Optional.empty()
        );

        return p;
    }

    /**
     * Used to prevent double-hitting
     * @return a unique identifier for the attack instance that spawned this
     */
    public int getUseId() {
        return useId;
    }

    public void hit(AbstractPlayer p) {
        collideBehavior.ifPresent(b -> b.collidedWith(this, p));
        p.wasHitBy(this);
        getActionRegister().triggerOnHit(p);
        terminate();
    }

    public boolean checkForCollisions(AbstractPlayer p) {
        boolean ret = super.isCollidingWith(p);
        if (ret && p.getLastHitById() != useId) {
            ret = true;
            hit(p);
        }
        return ret;
    }

    @Override
    public void init() {
        super.init();
    }

    @Override
    public void update() {
        super.update();
        distanceTraveled += getComputedSpeed();

        if (distanceTraveled >= range.getInPixels() && !isTerminating()) {
            terminate();
        }
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
