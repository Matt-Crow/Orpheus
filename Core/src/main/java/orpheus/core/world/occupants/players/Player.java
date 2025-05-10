package orpheus.core.world.occupants.players;

import java.util.Optional;
import java.util.UUID;

import orpheus.core.champions.Playable;
import orpheus.core.utils.coordinates.Point;
import orpheus.core.utils.coordinates.PointUpdater;
import orpheus.core.utils.coordinates.PolarVector;
import orpheus.core.utils.coordinates.TerminableVectorPointUpdater;
import orpheus.core.utils.coordinates.VectorPointUpdater;
import orpheus.core.world.occupants.WorldOccupant;
import util.Direction;
import util.Settings;
import world.Tile;
import world.World;
import world.battle.DamageBacklog;
import world.builds.AssembledBuild;
import world.builds.actives.AbstractActive;
import world.builds.characterClass.CharacterStatName;
import world.builds.characterClass.DroneCharacterClass;
import world.builds.passives.AbstractPassive;
import world.entities.Projectile;
import world.events.DamageEvent;
import world.events.EventListeners;
import world.events.KillEvent;
import world.events.OnHitEvent;
import world.events.OnUseMeleeEvent;
import world.statuses.AbstractStatus;

/**
 * The AbstractPlayer class essentially acts as a mobile entity with other,
 * battle related capabilities.
 *
 * @author Matt Crow
 */
public class Player extends WorldOccupant {

    /**
     * A unique identifier for this player
     */
    private final UUID id;

    /**
     * the playable character this player is playing as
     */
    private final Playable playingAs;

    /**
     * The unmodifies speed of this object
     */
    private final double baseSpeed;

    /**
     * How much this entity's movement should be multiplied by this frame
     */
    private double speedMultiplier = 1.0;

    /**
     * Whether this object is moving - not the same as setting speedMultiplier 
     * to 0
     */
    private boolean moving = false;

    private final DamageBacklog damage;
    
    /**
     * the statuses this is currently inflicted with
     */
    private final InflictedStatuses statuses;
    
    private Optional<TerminableVectorPointUpdater> knockback = Optional.empty();
    
    /**
     * the last player who attacked this
     */
    private Optional<Player> lastAttackedBy = Optional.empty();

    private final EventListeners<OnHitEvent> onBeHitListeners = new EventListeners<>();
	private final EventListeners<OnUseMeleeEvent> onUseMeleeListeners = new EventListeners<>();
    private final EventListeners<KillEvent> onKillListeners = new EventListeners<>();
	private final EventListeners<DamageEvent> onTakeDamageListeners = new EventListeners<>();

    public Player(World inWorld, UUID id, Playable playingAs, int minLifeSpan) {
        super(inWorld);
        setRadius(50);  
        this.id = id;
        this.playingAs = playingAs;
        playingAs.setUser(this);

        var characterClass = playingAs.getCharacterClass();
        baseSpeed = characterClass.getSpeed() * (Tile.TILE_SIZE * 5 / Settings.FPS);

        damage = new DamageBacklog(this, minLifeSpan);
        statuses = new InflictedStatuses(this);
    }

    /**
     * Creates a new player which a human will control - previously known as 
     * HumanPlayer.
     * @param inWorld the world to spawn the player in
     * @param name the player's name
     * @param playingAs what the player will play as
     * @return the new player
     */
    public static Player makeHuman(World inWorld, Playable playingAs) {
        return makeHuman(inWorld, playingAs, UUID.randomUUID());
    }

    /**
     * Creates a new player which a human will control - previously known as 
     * HumanPlayer.
     * @param inWorld the world to spawn the player in
     * @param id the player's unique ID
     * @param name the player's name
     * @param playingAs what the player will play as
     * @return the new player
     */
    public static Player makeHuman(World inWorld, Playable playingAs, UUID id) {
        var result = new Player(
            inWorld,
            id,
            playingAs, 
            10 // players live for at least 10 seconds
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
    public static Player makeDrone(World inWorld, int level) {
        var playable = new AssembledBuild(
            "Drone", 
            new DroneCharacterClass(level), 
            new AbstractActive[0], 
            new AbstractPassive[0]
        );
        var result = new Player(inWorld, UUID.randomUUID(), playable, level);
        return result;
    }

    /**
     * @return this player's unique identifier
     */
    public UUID getId() {
        return id;
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

    /**
     * Applies a modifier to this entity's speed that will be removed at the end of the frame.
     * @param multiplier the multipier to affect this entity's speed by - additive
     */
    public void addSpeedBoost(double multiplier) {
        speedMultiplier += multiplier;
    }

    public void setMoving(boolean moving) {
        this.moving = moving;
    }

    /**
     * @return the distance this will move on the next call to update
     */
    private double getComputedSpeed() {
        return (moving) 
            ? baseSpeed * speedMultiplier
            : 0.0;
    }

    /**
     * @return the event listeners which fire whenever this is hit
     */
    public EventListeners<OnHitEvent> eventOnBeHit() {
        return onBeHitListeners;
    }

    /**
     * @return the event listeners which fire whenever this uses a melee attack
     */
    public EventListeners<OnUseMeleeEvent> eventOnUseMelee() {
        return onUseMeleeListeners;
    }

    /**
     * @return the event listeners which fire whenever this is killed
     */
    public EventListeners<KillEvent> eventOnKill() {
        return onKillListeners;
    }

    /**
     * @return the event listeners which fire whenever this takes damage
     */
    public EventListeners<DamageEvent> eventOnTakeDamage() {
        return onTakeDamageListeners;
    }

    public DamageBacklog getDamage() {
        return damage;
    }

    public void takeDamage(int dmg) {
        damage.log(dmg);
        var maxHP = damage.getMaxHP();
        var percent = ((double)dmg) / maxHP;
        var e = new DamageEvent(dmg, percent);
		onTakeDamageListeners.handle(e);
    }

    /**
     * Inflicts this player with the given status, if they are not already 
     * inflicted with a better instance.
     * 
     * @param status the status to inflict
     */
    public void inflict(AbstractStatus status) {
        statuses.add(status.copy());
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
     * Notifies this Player that a projectile has hit them.
     * @param player the player who hit this
     * @param projectile the projectile which hit them
     */
    public final void wasHitBy(Player player, Projectile projectile) {
        lastAttackedBy = Optional.of(player);
        OnHitEvent t = new OnHitEvent(player, this);
		onBeHitListeners.handle(t);
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
     * uses this player's melee attack, if it is available
     */
    public void useMeleeAttack() {
        playingAs.getBasicAttack().useIfAble();
    }

    /**
     * Uses the active ability at the given index, if able.
     * @param num the index of the active ability to use
     */
    public void useAttack(int num) {
        var actives = playingAs.getActives();
        if (actives.size() >= num) {
            actives.get(num).useIfAble();
        }        
    }

    @Override
    public void init() {
        super.init();
		
        // must come before adding event listeners
		onBeHitListeners.clear();
		onUseMeleeListeners.clear();
        onTakeDamageListeners.clear();
		onKillListeners.clear();
        
        playingAs.init();
        speedMultiplier = 1.0;
        setMoving(false);
        damage.init();
        statuses.clear();
        knockback = Optional.empty();
        lastAttackedBy = Optional.empty();
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
            var velocity = new PolarVector(getComputedSpeed(), getFacing());
            PointUpdater updater = new VectorPointUpdater(velocity);
            updater.update(getCoordinates());
        }
    }

    @Override
    public void update() {
        super.update();
        playingAs.update();
        damage.update();        
        speedMultiplier = 1.0;
    }

    @Override
    public void terminate() {
        if (!isTerminating()) { // avoid double-terminate bugs
            super.terminate();
            lastAttackedBy.ifPresent((killer) -> killer.onKillListeners.handle(new KillEvent(this)));
            getTeam().notifyTerminate(this);
        }
    }

    public orpheus.core.world.graph.Player toGraph() {
        return new orpheus.core.world.graph.Player(
            id,
            getX(), 
            getY(), 
            getRadius(),
            getDamage().getHP(),
            statuses.toList().stream().map(AbstractStatus::toString).toList(),
            getTeam().getColor(),
            playingAs.getCharacterClass().getColor(),
            playingAs.toGraph(),
            playingAs.getActives().stream().map(AbstractActive::toGraph).toList()
        );
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Player) {
            return ((Player)other).id == id;
        }
        return false;
    }
}
