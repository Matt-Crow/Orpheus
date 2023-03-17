package world.entities;

import java.util.Arrays;

import util.Settings;
import world.World;
import world.builds.actives.ElementalActive;

public class Projectile extends AbstractEntity {

    private final ElementalActive registeredAttack;
    private int distanceTraveled;
    private int range;

    /**
     * whether this project can explode, spawning more projectiles
     */
    private boolean canExplode;

    private final int useId; //used to prevent double hitting. May not be unique to a single projectile. See AbstractActive for more info

    private Projectile(int useId, int x, int y, int degrees, int momentum, 
        ElementalActive a, boolean canExplode) {
        
        super(a.getUser().getWorld());
        setMaxSpeed(momentum);
        init();
        setX(x);
        setY(y);
        setFacing(degrees);
        this.useId = useId;
        distanceTraveled = 0;
        setTeam(a.getUser().getTeam());
        registeredAttack = a;
        range = a.getRange();
        setRadius(25);
        setIsMoving(true);
        this.canExplode = canExplode;
    }

    private Projectile(World inWorld, int useId, int x, int y, int degrees, 
        int momentum, ElementalActive a) {
        
        this(useId, x, y, degrees, momentum, a, false);
    }

    /**
     * Creates a projectile that can explode into more projectiles
     * @param inWorld
     * @param useId
     * @param x
     * @param y
     * @param angle
     * @param momentum
     * @param user
     * @param from
     * @return
     */
    public static Projectile seed(World inWorld, int useId, int x, int y, 
        int angle, int momentum, ElementalActive from) {

        var p = new Projectile(inWorld, useId, x, y, angle, momentum, from);
        p.canExplode = from.getAOE() != 0;
        return p;
    }

    /**
     * Creates a projectile that has exploded from another projectile, and thus
     * can no longer explode.
     * @param inWorld
     * @param useId
     * @param x
     * @param y
     * @param angle
     * @param momentum
     * @param user
     * @param from
     * @return
     */
    public static Projectile explosion(World inWorld, int useId, int x, int y, 
        int angle, int momentum, ElementalActive from) {
        
        var p = new Projectile(useId, x, y, angle, momentum, from, false);
        p.range = (int)from.getAOE();

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
        registeredAttack.hit(this, p);
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
        distanceTraveled += getMomentum();

        if (distanceTraveled >= range && !isTerminating()) {
            terminate();
        }
    }

    @Override
    public void terminate() {
        super.terminate();
        if (canExplode) {
            explode();
        }
    }

    private void explode() {
        World w = getWorld();
        for (int i = 0; i < Settings.TICKSTOROTATE; i++) {
            registeredAttack.getUser().spawn(Projectile.explosion(w, useId, getX(), getY(), 360 * i / Settings.TICKSTOROTATE, 5, registeredAttack));
        }
        canExplode = false;
    }

    @Override
    public orpheus.core.world.graph.Projectile toGraph() {
        return new orpheus.core.world.graph.Projectile(
            getX(),
            getY(),
            getRadius(),
            getFacing().copy(),
            registeredAttack.getUser().getTeam().getColor(),
            Arrays.stream(registeredAttack.getColors()).toList(),
            registeredAttack.getParticleType()
        );
    }
}
