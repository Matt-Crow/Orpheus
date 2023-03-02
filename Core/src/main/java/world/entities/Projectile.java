package world.entities;

import world.entities.particles.Particle;

import java.util.Arrays;

import gui.graphics.CustomColors;
import util.Settings;
import world.build.actives.ElementalActive;
import util.Random;
import world.World;

public class Projectile extends AbstractEntity {

    private final ElementalActive registeredAttack;
    private int distanceTraveled;
    private int range;

    /**
     * whether this project can explode, spawning more projectiles
     */
    private boolean canExplode;

    private final int useId; //used to prevent double hitting. May not be unique to a single projectile. See AbstractActive for more info

    private Projectile(World inWorld, int useId, int x, int y, int degrees, 
        int momentum, ElementalActive a, 
        boolean canExplode) {
        
        super(inWorld);
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
        
        this(inWorld, useId, x, y, degrees, momentum, a, false);
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
        
        var p = new Projectile(inWorld, useId, x, y, angle, momentum, 
            from, false);
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

    public void spawnParticle(int degrees, int m, CustomColors c) {
        Particle p = new Particle(m, c);
        p.init();
        p.setX(getX());
        p.setY(getY());
        p.setFacing(degrees);
        getWorld().spawn(p);
    }

    /**
     * Needs to be kept separate from update, as update is not invoked by
     * clients. Update automatically calls this method.
     */
    public void spawnParticles() {
        CustomColors[] cs = registeredAttack.getColors();

        if (!Settings.DISABLEPARTICLES && !getShouldTerminate()) {
            switch (registeredAttack.getParticleType()) {
                case BURST:
                    break;
                case SHEAR:
                    CustomColors rs = cs[Random.choose(0, cs.length - 1)];
                    spawnParticle(getFacing().getDegrees() - 45, 5, rs);
                    rs = cs[Random.choose(0, cs.length - 1)];
                    spawnParticle(getFacing().getDegrees() + 45, 5, rs);
                    break;
                case BEAM:
                    CustomColors rbe = cs[Random.choose(0, cs.length - 1)];
                    spawnParticle(getFacing().getDegrees() - 180, 5, rbe);
                    break;
                case BLADE:
                    CustomColors rbl = cs[Random.choose(0, cs.length - 1)];
                    spawnParticle(getFacing().getDegrees(), 0, rbl);
                    break;
                case NONE:
                    break;
                default:
                    System.out.println("The particle type of " + registeredAttack.getParticleType() + " is not found for Projectile.java");
            }
        }
    }

    @Override
    public void init() {
        super.init();
    }

    @Override
    public void update() {
        super.update();
        distanceTraveled += getMomentum();

        // need to change range based on projectile type: attack range for seed, aoe for aoeprojectile
        if (distanceTraveled >= range && !getShouldTerminate()) {
            terminate();
        }
        spawnParticles();
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
