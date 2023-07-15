package world.entities;

import java.awt.Color;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import orpheus.core.champions.Playable;
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
import world.builds.AssembledBuild;
import world.builds.actives.AbstractActive;
import world.builds.actives.MeleeActive;
import world.builds.characterClass.CharacterStatName;
import world.builds.characterClass.DroneCharacterClass;
import world.builds.passives.AbstractPassive;
import world.statuses.AbstractStatus;

/**
 * The AbstractPlayer class essentially acts as a mobile entity with other,
 * battle related capabilities.
 * 
 * TODO rename to Player class
 *
 * @author Matt Crow
 */
public class AbstractPlayer extends WorldOccupant {

    /**
     * A unique identifier for this player
     */
    private final UUID id; // can pull into HumanPlayer once Team is generic

    /**
     * the playable character this player is playing as
     */
    private final Playable playingAs;

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
    private static final int HUMAN_LIFE_SPAN = 10;

    public AbstractPlayer(World inWorld, Playable playingAs, String n, 
        int minLifeSpan, UUID id, MeleeActive basicAttack
    ) {
        super(inWorld);
        this.id = id;
        this.playingAs = playingAs;
        playingAs.setUser(this);

        var characterClass = playingAs.getCharacterClass();
        color = characterClass.getColor();
        baseSpeed = (int) (characterClass.getSpeed() * (Tile.TILE_SIZE * 5 / Settings.FPS));
        name = n;

        basicAttack.setUser(this);
        slash = basicAttack;
        log = new DamageBacklog(this, minLifeSpan);
        statuses = new InflictedStatuses(this);

        lastHitById = -1;

        setRadius(RADIUS);  
    }

    /**
     * Creates a new player which a human will control - previously known as 
     * HumanPlayer.
     * @param inWorld the world to spawn the player in
     * @param name the player's name
     * @param playingAs what the player will play as
     * @return the new player
     */
    public static AbstractPlayer makeHuman(World inWorld, String name, Playable playingAs) {
        return makeHuman(inWorld, name, playingAs, UUID.randomUUID());
    }

    /**
     * Creates a new player which a human will control - previously known as 
     * HumanPlayer.
     * @param inWorld the world to spawn the player in
     * @param name the player's name
     * @param playingAs what the player will play as
     * @param id the player's unique ID
     * @return the new player
     */
    public static AbstractPlayer makeHuman(World inWorld, String name, Playable playingAs, UUID id) {
        var result = new AbstractPlayer(
            inWorld, 
            playingAs, 
            name, 
            HUMAN_LIFE_SPAN, 
            id, 
            playingAs.getBasicAttack()
        );
        return result;
    }

    /**
     * Creates a new drone player - previously known as AIPlayer
     * @param inWorld the world to spawn the drone in
     * @param name the drone's name
     * @param level how powerful the drone is - ranges from 1 to 5
     * @return the new drone
     */
    public static AbstractPlayer makeDrone(World inWorld, String name, int level) {
        var playable = new AssembledBuild(
            "Drone", 
            new DroneCharacterClass(level), 
            new AbstractActive[0], 
            new AbstractPassive[0]
        );
        var result = new AbstractPlayer(inWorld, playable, name, level, UUID.randomUUID(), MeleeActive.makeBasicAttack());
        return result;
    }

    /**
     * @return this player's unique identifier
     */
    public UUID getId() {
        return id;
    }

    public int getMaxHP() {
        return log.getMaxHP();
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

    /**
     * @return the distance this will move on the next call to update
     */
    public double getComputedSpeed() {
        return (moving) 
            ? baseSpeed * speedMultiplier
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
        return getCoordinates().distanceFrom(point) < baseSpeed;
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
     * Uses the active ability at the given index, if able.
     * @param num the index of the active ability to use
     */
    public void useAttack(int num) {
        var actives = playingAs.getActives();
        if (actives.size() >= num) {
            var attack = actives.get(num);
            if (attack.canUse()) {
                attack.trigger();
            }
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
        playingAs.init();
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
        playingAs.update();
        //slash.update(); avoid double-updating slash

        getActionRegister().triggerOnUpdate();
        log.update();
    }

    @Override
    public void terminate() {
        super.terminate();
        lastAttackedBy.ifPresent((killer) -> killer.getActionRegister().triggerOnKill(this));
        getTeam().notifyTerminate(this);
    }

    public double getStatValue(CharacterStatName n) {
        var characterClass = playingAs.getCharacterClass();
        var result = 0.0;
        switch (n) {
            case HP:
                result = characterClass.getMaxHP();
                break;
            case DMG:
                result = characterClass.getOffMult();
                break;
            case REDUCTION:
                result = characterClass.getDefMult();
                break;
            case SPEED:
                result = characterClass.getSpeed();
                break;
        }
        return result;
    }

    public orpheus.core.world.graph.Player toGraph() {
        return new Player(
            id,
            getX(), 
            getY(), 
            getRadius(),
            getLog().getHP(),
            statuses.toList().stream().map(AbstractStatus::toString).toList(),
            getTeam().getColor(),
            color,
            playingAs.toGraph(),
            playingAs.getActives().stream().map(AbstractActive::toGraph).toList()
        );
    }
}
