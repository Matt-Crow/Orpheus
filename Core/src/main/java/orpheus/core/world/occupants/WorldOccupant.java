package orpheus.core.world.occupants;

import world.events.termination.*;

import java.util.Optional;

import orpheus.core.world.graph.Graphable;
import util.Coordinates;
import util.Direction;
import world.World;
import world.battle.Team;
import world.events.ActionRegister;

/**
 * The AbstractEntity class is used as the base for anything that moves and
 * exists within a World.
 */
public abstract class WorldOccupant implements Graphable, Terminable {
    
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
     * The x-coordinate of this object in the world
     */
    private int x = 0;

    /**
     * The y-coordinate of this object in the world
     */
    private int y = 0;

    /**
     * The radius of this object, measured in pixels
     */
    private int radius = 50;

    /**
     * The direction this entity is facing
     */
    private Direction facing = Direction.fromDegrees(0);

    /**
     * The unmodifies speed of this object
     */
    private double baseSpeed = 0.0;

    /**
     * How much this entity's movement should be multiplied by this frame
     */
    private double speedMultiplier = 1.0;

    /**
     * Whether this object is moving
     */
    private boolean moving = false;

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
    public WorldOccupant() {
        actReg = new ActionRegister(this);
    }

    /**
     * Creates an entity that may or may not yet exist in a world.
     * @param world the world this entity exists in, or null.
     */
    public WorldOccupant(World world) {
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

    public void setX(int x) {
        this.x = x;
    }

    public int getX() {
        return x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getY() {
        return y;
    }

    public void setRadius(int r) {
        if (r < 0) {
            throw new IllegalArgumentException(String.format("radius must be non-negative, so %d isn't allowed", r));
        }
        radius = r;
    }

    public int getRadius() {
        return radius;
    }

    public void setFacing(int degrees) {
        facing.setDegrees(degrees);
    }

    public Direction getFacing() {
        return facing;
    }

    public void setBaseSpeed(double baseSpeed) {
        if (baseSpeed < 0) {
            throw new IllegalArgumentException("Speed must be non-negative");
        }
        this.baseSpeed = baseSpeed;
    }

    public double getBaseSpeed() {
        return baseSpeed;
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
     * @return the distance this will move on the next call to update
     */
    public double getComputedSpeed() {
        return (moving) 
            ? baseSpeed * speedMultiplier
            : 0.0;
    }

    public void setMoving(boolean moving) {
        this.moving = moving;
    }

    /**
     * @return the game event listeners attached to this entity
     */
    public ActionRegister getActionRegister() {
        return actReg;
    }

    public final void turnTo(int xCoord, int yCoord) {
        facing = Direction.getDegreeByLengths(x, y, xCoord, yCoord);
    }
    
    /**
     * Checks if this entity collides with another entity. Subclasses should
     * overload this with each subclass of AbstractEntity that needs special
     * reactions
     *
     * @param e the AbstractEntity to check for collisions with
     * @return whether or not this collides with the given AbstractEntity
     */
    public final boolean isCollidingWith(WorldOccupant e) {
        return Coordinates.distanceBetween(this, e) <= e.getRadius() + getRadius();
    }

    public final boolean isWithin(int x, int y, int w, int h) {
        return (x < this.x + radius //left
                && x + w > this.x - radius //right
                && y < this.y + radius //top
                && y + h > this.y - radius //bottom
                );
    }

    /**
     * Adds an entity to this one's team & world
     *
     * @param e the AbstractEntity to insert before this one
     */
    public final void spawn(WorldOccupant e) {
        if (e == null) {
            throw new NullPointerException();
        }
        e.setWorld(getWorld());
        getTeam().add(e);
    }

    /**
     * This method is called at the start of a Battle. Subclasses can override
     * this method, but should ensure they call super.init() somewhere within
     * their override
     */
    public void init() {
        terminationListeners.clear();
        actReg.reset();
        setMoving(false);
        speedMultiplier = 1.0;
        terminating = false;
    }

    /**
     * can be overridden, but subclasses should ensure they call super.update()
     * in their implementation.
     */
    public void update() {
        if (!terminating) {
            updateMovement();
            actReg.triggerOnUpdate();
        }
    }

    /**
     * can be overridden, but subclasses should ensure they call
     * super.updateMovement() in their implementation.
     */
    protected void updateMovement() {
        var s = getComputedSpeed();
        x += s * facing.getXMod();
        y += s * facing.getYMod();
        speedMultiplier = 1.0;
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
    
    @Override
    public boolean isTerminating() {
        return terminating;
    }
}
