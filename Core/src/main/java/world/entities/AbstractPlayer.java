package world.entities;

import java.awt.Color;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import orpheus.core.utils.coordinates.Point;
import orpheus.core.utils.coordinates.PointUpdater;
import orpheus.core.utils.coordinates.PolarVector;
import orpheus.core.utils.coordinates.TerminableVectorPointUpdater;
import orpheus.core.utils.coordinates.VectorPointUpdater;
import orpheus.core.world.graph.Player;
import orpheus.core.world.occupants.WorldOccupant;
import util.Direction;
import util.Settings;
import world.Tile;
import world.World;
import world.battle.DamageBacklog;
import world.builds.actives.MeleeActive;
import world.builds.characterClass.CharacterStatName;
import world.statuses.AbstractStatus;

/**
 * The AbstractPlayer class essentially acts as a mobile entity with other,
 * battle related capabilities.
 *
 * @author Matt Crow
 */
public abstract class AbstractPlayer extends WorldOccupant {

    /**
     * A unique identifier for this player
     */
    private final UUID id; // can pull into HumanPlayer once Team is generic

    private final String name;
    private Color color;

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

    private Optional<TerminableVectorPointUpdater> knockback = Optional.empty();

    private int lastHitById; //the useId of the last projectile that hit this player
    
    /**
     * the last player who attacked this
     */
    private Optional<AbstractPlayer> lastAttackedBy = Optional.empty();

    private final MeleeActive slash;
    private final DamageBacklog log;

    /**
     * the statuses this is currently inflicted with
     */
    private final InflictedStatuses statuses;

    public static final int RADIUS = 50;

    public AbstractPlayer(World inWorld, String n, int minLifeSpan, UUID id,
        MeleeActive basicAttack
    ) {
        super(inWorld);
        this.id = id;
        setBaseSpeed(Tile.TILE_SIZE * 5 / Settings.FPS);
        name = n;
        color = Color.black;

        basicAttack.setUser(this);
        slash = basicAttack;
        log = new DamageBacklog(this, minLifeSpan);
        statuses = new InflictedStatuses(this);

        lastHitById = -1;

        setRadius(RADIUS);    
    }

    /**
     * @return this player's unique identifier
     */
    public UUID getId() {
        return id;
    }

    public final String getName() {
        return name;
    }

    public final void setColor(Color c) {
        color = c;
    }

    protected Color getColor() {
        return color;
    }

    public int getMaxHP() {
        return log.getMaxHP();
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

    public void setMoving(boolean moving) {
        this.moving = moving;
    }

    public boolean isMoving() {
        return moving;
    }

    /**
     * @return the distance this will move on the next call to update
     */
    public double getComputedSpeed() {
        return (isMoving()) 
            ? getBaseSpeed() * speedMultiplier
            : 0.0;
    }

    /**
     * @return an immutable list of the statuses this is currently inflicted with
     */
    protected List<AbstractStatus> getInflictedStatuses() {
        return statuses.toList();
    }

    public DamageBacklog getLog() {
        return log;
    }

    /**
     * Since players can only move a set minimum distance, they may not be able
     * to move precisely to a given point. This checks if it would not be
     * possible for them to move any closer to the given point.
     * 
     * @param point the point to check
     * @return whether moving would cause this player to move away from point
     */
    public boolean isAsCloseAsPossibleTo(Point point) {
        return getCoordinates().distanceFrom(point) < getBaseSpeed();
    }

    /**
     * @param mag : the total distance this entity will be knocked back
     * @param d : the direction this entity is knocked back
     * @param dur : the number of frames this will be knocked back for
    */
    public final void knockBack(int mag, Direction d, int dur) {
        var speed = ((double)mag) / dur;
        var kb = new TerminableVectorPointUpdater(new PolarVector(speed, d), mag);
        knockback = Optional.of(kb);
    }

    /**
     * Inflicts this player with the given status, if they are not already 
     * inflicted with a better instance.
     * 
     * @param status the status to inflict
     */
    public void inflict(AbstractStatus status) {
        statuses.add(status);
    }

    public void useMeleeAttack() {
        if (slash.canUse()) {
            slash.trigger();
        }
    }

    /**
     * Notifies this Player that a projectile has hit them.
     * @param player the player who hit this
     * @param projectile the projectile which hit them
     */
    public final void wasHitBy(AbstractPlayer player, Projectile projectile) {
        lastHitById = projectile.getUseId();
        lastAttackedBy = Optional.of(player);
    }

    public void logDamage(int dmg) {
        log.log(dmg);
        getActionRegister().triggerOnTakeDamage(dmg);
    }

    public int getLastHitById() {
        return lastHitById;
    }

    @Override
    public void init() {
        super.init();
        statuses.clear();

        slash.init();
        log.init();

        speedMultiplier = 1.0;
        setMoving(false);

        lastHitById = -1;
        lastAttackedBy = Optional.empty();
        knockback = Optional.empty();
    }

    @Override
    protected void updateMovement() {
        if(knockback.isPresent()) { // cannot move while being knocked back
            var kb = knockback.get();
            kb.update(getCoordinates());
            if (kb.isDone()) {
                knockback = Optional.empty();
            }
        } else {
            move();
        }
    }

    /**
     * can be overridden, but subclasses should ensure they call
     * super.updateMovement() in their implementation.
     */
    protected void move() {
        var velocity = new PolarVector(getComputedSpeed(), getFacing());
        PointUpdater updater = new VectorPointUpdater(velocity);
        updater.update(getCoordinates());
        speedMultiplier = 1.0;
    }

    @Override
    public void update() {
        super.update();
        slash.update();

        getActionRegister().triggerOnUpdate();
        log.update();
    }

    @Override
    public void terminate() {
        super.terminate();
        lastAttackedBy.ifPresent((killer) -> killer.getActionRegister().triggerOnKill(this));
        getTeam().notifyTerminate(this);
    }

    public abstract double getStatValue(CharacterStatName n);

    public orpheus.core.world.graph.Player toGraph() {
        return new Player(
            id,
            getX(), 
            getY(), 
            getRadius(),
            getLog().getHP(),
            statuses.toList().stream().map(AbstractStatus::toString).toList(),
            getTeam().getColor(),
            color
        );
    }
}
