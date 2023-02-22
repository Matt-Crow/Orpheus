package world.entities;

import java.awt.Graphics;
import world.events.termination.*;
import util.Coordinates;
import world.World;
import world.battle.Team;
import world.events.ActionRegister;

/**
 * The AbstractEntity class is used as the base for anything that moves and
 * exists within a World.
 */
public abstract class AbstractEntity extends AbstractPrimitiveEntity implements Terminable {
    // make sure to set this after deserializing!
    private transient World world; 

    private double speedMultiplier;

    private final ActionRegister actReg;
    private Team team;

    private boolean shouldTerminate;
    private final TerminationListeners terminationListeners = new TerminationListeners();

    public final String id;
    private static int nextId = 0;

    public AbstractEntity(World world) {
        speedMultiplier = 1.0;
        id = "#" + nextId;
        this.world = world; // null world needs to be allowed due to circular dependencies 
        actReg = new ActionRegister(this);
        nextId++;
    }

    @Override
    public final boolean equals(Object o) {
        return o != null && o instanceof AbstractEntity && ((AbstractEntity) o).id.equals(id);
    }

    @Override
    public final int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "Entity #" + id;
    }
    
    /**
     * required because world is transient
     * @param world the world this exists in
     */
    public void setWorld(World world){
        this.world = world;
    }
    
    public final World getWorld() {
        return world;
    }

    public final void multiplySpeedBy(double f) {
        speedMultiplier *= f;
    }

    // remove this later
    public final void clearSpeedFilter() {
        speedMultiplier = 1.0;
    }

    /**
     * Checks if this entity collides with another entity. Subclasses should
     * overload this with each subclass of AbstractEntity that needs special
     * reactions
     *
     * @param e the AbstractEntity to check for collisions with
     * @return whether or not this collides with the given AbstractEntity
     */
    public final boolean isCollidingWith(AbstractEntity e) {
        return Coordinates.distanceBetween(this, e) <= e.getRadius() + getRadius();
    }

    /**
     *
     * @return how much this entity will move this frame
     */
    @Override
    public final int getMomentum() {
        return (int) (getMaxSpeed() * speedMultiplier);
    }

    public final ActionRegister getActionRegister() {
        return actReg;
    }

    public final void setTeam(Team t) {
        team = t;
    }

    public final Team getTeam() {
        return team;
    }

    @Override
    public void addTerminationListener(TerminationListener listen) {
        terminationListeners.add(listen);
    }

    @Override
    public void terminate() {
        shouldTerminate = true;
        terminationListeners.objectWasTerminated(this);
    }

    public final boolean getShouldTerminate() {
        return shouldTerminate;
    }

    /**
     * Inserts an AbstractEntity into this' EntityNode chain. Since the
     * AbstractEntity is inserted before this one, it will not be updated during
     * this iteration of EntityManager.update
     *
     * @param e the AbstractEntity to insert before this one
     */
    public final void spawn(AbstractEntity e) {
        if (e == null) {
            throw new NullPointerException();
        }
        team.add(e);
    }

    /**
     * This method is called at the start of a Battle. Subclasses can override
     * this method, but should ensure they call super.init() somewhere within
     * their override
     */
    @Override
    public void init() {
        terminationListeners.clear();
        actReg.reset();
        setIsMoving(false);
        speedMultiplier = 1.0;
        shouldTerminate = false;
    }

    /**
     * can be overridden, but subclasses should ensure they call super.update()
     * in their implementation.
     */
    @Override
    public void update() {
        if (!shouldTerminate) {
            super.update();
            actReg.triggerOnUpdate();
        }
    }

    @Override
    protected void updateMovement() {
        super.updateMovement();
        clearSpeedFilter();
    }

    @Override
    public boolean isTerminating() {
        return shouldTerminate;
    }

    @Override
    public abstract void draw(Graphics g);
}