package world.entities;

import world.events.termination.*;

import java.util.Optional;

import util.Coordinates;
import world.World;
import world.battle.Team;
import world.events.ActionRegister;

/**
 * The AbstractEntity class is used as the base for anything that moves and
 * exists within a World.
 */
public abstract class AbstractEntity extends AbstractPrimitiveEntity implements Terminable {
    
    /**
     * The game world this entity occupies. Allows empty due to circular 
     * dependencies between entities & worlds.
     */
    private Optional<World> world = Optional.empty();

    /**
     * The team this entity is a member of. Allows empty due to circular 
     * dependencies between entities & teams.
     */
    private Optional<Team> team = Optional.empty();

    /**
     * How much this entity's movement should be multiplied by this frame
     */
    private double speedMultiplier = 1.0;

    /**
     * Registers various game event listeners
     */
    private final ActionRegister actReg;

    /**
     * Whether this entity is in its terminating phase
     */
    private boolean terminating = false;

    /**
     * The objects to notify when this entity terminates
     */
    private final TerminationListeners terminationListeners = new TerminationListeners();

    /**
     * Creates an entity that does not yet exist in a world. You must call 
     * setWorld before using this entity.
     */
    public AbstractEntity() {
        actReg = new ActionRegister(this);
    }

    /**
     * Creates an entity that may or may not yet exist in a world.
     * @param world the world this entity exists in, or null.
     */
    public AbstractEntity(World world) {
        this();
        if (world != null) {
            this.world = Optional.of(world);
        }
    }

    /**
     * Allows this to be instantiated before the given world
     * @param world the world this exists in
     */
    public void setWorld(World world){
        this.world = Optional.of(world);
    }
    
    /**
     * Throws an exception if this entity's world has not been set
     * @return the world this entity occupies
     */
    public World getWorld() {
        return world.get();
    }

    /**
     * Allows this to be instantiated before the given team
     * @param team the team this entity belongs to
     */
    public void setTeam(Team team) {
        this.team = Optional.of(team);
    }

    /**
     * Throws an exception if this entity's team has not been set
     * @return the team this entity belongs to
     */
    public Team getTeam() {
        return team.get();
    }

    /**
     * Applies a modifier to this entity's speed that will be removed at the end
     * of the frame.
     * @param multiplier the multipier to affect this entity's speed by - multiplicative
     */
    public void multiplySpeedBy(double multiplier) {
        speedMultiplier *= multiplier;
    }

    /**
     * @return the game event listeners attached to this entity
     */
    public ActionRegister getActionRegister() {
        return actReg;
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
     * Adds an entity to this one's team & world
     *
     * @param e the AbstractEntity to insert before this one
     */
    public final void spawn(AbstractEntity e) {
        if (e == null) {
            throw new NullPointerException();
        }
        e.setWorld(getWorld());
        getTeam().add(e);
    }

    /**
     *
     * @return how much this entity will move this frame
     */
    @Override
    public final int getMomentum() {
        return (int) (getMaxSpeed() * speedMultiplier);
    }

    @Override
    public void addTerminationListener(TerminationListener listen) {
        terminationListeners.add(listen);
    }

    @Override
    public void terminate() {
        terminating = true;
        terminationListeners.objectWasTerminated(this);
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
        terminating = false;
    }

    /**
     * can be overridden, but subclasses should ensure they call super.update()
     * in their implementation.
     */
    @Override
    public void update() {
        if (!terminating) {
            super.update();
            actReg.triggerOnUpdate();
        }
    }

    @Override
    protected void updateMovement() {
        super.updateMovement();
        speedMultiplier = 1.0;
    }

    @Override
    public boolean isTerminating() {
        return terminating;
    }
}
