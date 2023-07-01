package world.entities;

import java.awt.Color;
import java.util.HashMap;
import java.util.UUID;

import controls.ai.Path;
import controls.ai.PathInfo;
import orpheus.core.world.graph.Player;
import orpheus.core.world.occupants.WorldOccupant;
import util.Direction;
import util.Settings;
import world.Tile;
import world.World;
import world.battle.DamageBacklog;
import world.builds.actives.MeleeActive;
import world.builds.actives.Range;
import world.builds.characterClass.CharacterStatName;
import world.events.termination.Terminable;
import world.events.termination.TerminationListener;
import world.statuses.AbstractStatus;
import world.statuses.StatusName;

/**
 * The AbstractPlayer class essentially acts as a mobile entity with other,
 * battle related capabilities.
 *
 * @author Matt Crow
 */                                                         // needs to listen for status termination
public abstract class AbstractPlayer extends WorldOccupant implements TerminationListener {

    /**
     * A unique identifier for this player
     */
    private final UUID id; // can pull into HumanPlayer once Team is generic

    private final String name;
    private Color color;

    /*
     * (focusX, focusY) is a point that the entity is trying to reach
     */
    private int focusX;
    private int focusY;
    private boolean hasFocus;

    private Direction knockbackDir;
    private int knockbackMag;
    private int knockbackDur;

    private int lastHitById; //the useId of the last projectile that hit this player

    private final MeleeActive slash;
    private final DamageBacklog log;

    private final HashMap<StatusName, AbstractStatus> stats = new HashMap<>();
    
    //both players and AI need to find paths, given the current controls
    private Path path;

    public static final int RADIUS = 50;

    public AbstractPlayer(World inWorld, String n, int minLifeSpan) {
        this(inWorld, n, minLifeSpan, UUID.randomUUID());
    }

    public AbstractPlayer(World inWorld, String n, int minLifeSpan, UUID id) {
        super(inWorld);
        this.id = id;
        setBaseSpeed(Tile.TILE_SIZE * 5 / Settings.FPS);
        name = n;
        color = Color.black;

        focusX = 0;
        focusY = 0;
        hasFocus = false;

        knockbackDir = null;
        knockbackMag = 0;
        knockbackDur = 0;

        slash = new MeleeActive("Slash", 1, 5, Range.NONE, 3); 
        slash.setParticleType(ParticleType.SHEAR);
        slash.setUser(this);
        log = new DamageBacklog(this, minLifeSpan);
        path = null;

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

    protected HashMap<StatusName, AbstractStatus> getStatuses() {
        return stats;
    }

    public DamageBacklog getLog() {
        return log;
    }

    //focus related methods
    public final void setFocus(int xCoord, int yCoord) {
        focusX = xCoord;
        focusY = yCoord;
        hasFocus = true;
    }

    public final void setFocus(WorldOccupant e) {
        setFocus(e.getX(), e.getY());
    }

    public final void turnToFocus() {
        turnTo(focusX, focusY);
    }

    public boolean withinFocus() {
        // returns if has reached focal point
        boolean withinX = Math.abs(getX() - focusX) < getBaseSpeed();
        boolean withinY = Math.abs(getY() - focusY) < getBaseSpeed();
        return withinX && withinY;
    }

    public void setPath(int x, int y) {
        setPath(getWorld().getMap().findPath(getX(), getY(), x, y));
    }

    public void setPath(Path p) {
        path = p;
        if (!path.noneLeft()) {
            PathInfo pi = path.get();
            setFocus(pi.getEndX(), pi.getEndY());
        }
    }

    public Path getPath() {
        return path;
    }

    public final void knockBack(int mag, Direction d, int dur) {
        /**
         * @param mag : the total distance this entity will be knocked back
         * @param d : the direction this entity is knocked back
         * @param dur : the number of frames this will be knocked back for
         */
        knockbackMag = mag / dur;
        knockbackDir = d;
        knockbackDur = dur;
    }

    public void inflict(AbstractStatus newStat) {
        boolean found = stats.containsKey(newStat.getStatusName());
        boolean shouldReplace = false;

        if (found) {
            AbstractStatus oldStat = stats.get(newStat.getStatusName());
            if (oldStat.getIntensityLevel() < newStat.getIntensityLevel()) {
                shouldReplace = true;
            } else if (oldStat.getUsesLeft() < newStat.getUsesLeft()) {
                shouldReplace = true;
            }
        }

        if (shouldReplace || !found) {
            stats.put(newStat.getStatusName(), newStat);
            newStat.inflictOn(this);
            newStat.addTerminationListener(this);
        }
    }

    public void useMeleeAttack() {
        if (slash.canUse()) {
            slash.trigger();
        }
    }

    /**
     * Notifies this Player that a projectile has hit them.
     *
     * @param p
     */
    public final void wasHitBy(Projectile p) {
        lastHitById = p.getUseId();
    }

    public void logDamage(int dmg) {
        log.log(dmg);
    }

    public int getLastHitById() {
        return lastHitById;
    }

    @Override
    public void init() {
        super.init();
        stats.clear();

        slash.init();
        log.init();

        path = null;
        lastHitById = -1;

        hasFocus = false;
        knockbackDir = null;
        knockbackMag = 0;
        knockbackDur = 0;

        playerInit();
    }

    @Override
    protected void updateMovement() {
        if (hasFocus) {
            if (withinFocus()) {
                hasFocus = false;
                setMoving(false);
            } else {
                turnToFocus();
                setMoving(true);
            }
        }
        if (knockbackDir != null) {
            //cannot move if being knocked back
            setX((int) (getX() + knockbackMag * knockbackDir.getXMod()));
            setY((int) (getY() + knockbackMag * knockbackDir.getYMod()));
            knockbackDur--;
            if (knockbackDur == 0) {
                knockbackDir = null;
            }
        } else {
            super.updateMovement();
        }
    }

    @Override
    public void update() {
        super.update();
        if (path != null) {
            //follow path
            if (path.noneLeft()) {
                path = null;
            } else {
                while (withinFocus() && !path.noneLeft()) {
                    path.deque();
                    if (!path.noneLeft()) {
                        PathInfo p = path.get();
                        setFocus(p.getEndX(), p.getEndY());
                    }
                }
            }
        }
        slash.update();

        getActionRegister().triggerOnUpdate();
        log.update();

        playerUpdate();
    }

    @Override
    public void terminate() {
        super.terminate();
        getTeam().notifyTerminate(this);
    }

    @Override
    public void objectWasTerminated(Terminable terminable) {
        if (terminable instanceof AbstractStatus) {
            removeStatus((AbstractStatus)terminable);
        }
    }

    private void removeStatus(AbstractStatus status) {
        AbstractStatus old = stats.get(status.getStatusName()); // may be null
        if (old == status) {
            stats.remove(old.getStatusName());
        }
    }

    public abstract double getStatValue(CharacterStatName n);

    public abstract void playerInit();

    public abstract void playerUpdate();

    public orpheus.core.world.graph.Player toGraph() {
        return new Player(
            id,
            getX(), 
            getY(), 
            getRadius(),
            getLog().getHP(),
            stats.values().stream().map(AbstractStatus::toString).toList(),
            getTeam().getColor(),
            color
        );
    }
}
