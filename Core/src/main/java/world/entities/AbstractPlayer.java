package world.entities;

import world.battle.DamageBacklog;
import java.awt.Color;
import java.awt.Graphics;
import controls.ai.Path;
import controls.ai.PathInfo;
import world.statuses.AbstractStatus;
import world.statuses.StatusName;
import util.Settings;
import world.build.actives.ElementalActive;
import world.build.characterClass.CharacterStatName;
import world.entities.particles.ParticleType;
import world.events.termination.Terminable;
import world.events.termination.TerminationListener;
import world.Tile;
import java.util.HashMap;
import util.Direction;
import world.World;

/**
 * The AbstractPlayer class essentially acts as a mobile entity with other,
 * battle related capabilities.
 *
 * @author Matt Crow
 */                                                         // needs to listen for status termination
public abstract class AbstractPlayer extends AbstractEntity implements TerminationListener {

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
    private AbstractPlayer lastHitBy;

    private final ElementalActive slash;
    private final DamageBacklog log;

    private final HashMap<StatusName, AbstractStatus> stats = new HashMap<>();
    
    //both players and AI need to find paths, given the current controls
    private Path path;

    public static final int RADIUS = 50;

    public AbstractPlayer(World inWorld, String n, int minLifeSpan) {
        super(inWorld);
        setMaxSpeed(Tile.TILE_SIZE * 5 / Settings.FPS);
        name = n;
        color = Color.black;

        focusX = 0;
        focusY = 0;
        hasFocus = false;

        knockbackDir = null;
        knockbackMag = 0;
        knockbackDur = 0;

        slash = new ElementalActive("Slash", 1, 1, 5, 0, 3); // todo maybe change
        slash.setParticleType(ParticleType.SHEAR);
        slash.setUser(this);
        log = new DamageBacklog(this, minLifeSpan);
        path = null;

        lastHitById = -1;
        lastHitBy = null;

        setRadius(RADIUS);
    }

    public final String getName() {
        return name;
    }

    public final void setColor(Color c) {
        color = c;
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

    public final void setFocus(AbstractEntity e) {
        setFocus(e.getX(), e.getY());
    }

    public final void turnToFocus() {
        turnTo(focusX, focusY);
    }

    public boolean withinFocus() {
        // returns if has reached focal point
        boolean withinX = Math.abs(getX() - focusX) < getMaxSpeed();
        boolean withinY = Math.abs(getY() - focusY) < getMaxSpeed();
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
        lastHitBy = p.getUser();
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
        lastHitBy = null;

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
                setIsMoving(false);
            } else {
                turnToFocus();
                setIsMoving(true);
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
        clearSpeedFilter();
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
        if (lastHitBy != null) {
            lastHitBy.getLog().healPerc(5);
        }
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

    @Override
    public void draw(Graphics g) {
        int w = 1000;
        int h = 1000;
        int r = getRadius();

        if (path != null) {
            path.draw(g);
        }

        // HP value
        g.setColor(Color.black);
        g.drawString(
                String.format("HP: %d", getLog().getHP()),
                getX() - w / 12,
                getY() - h / 8
        );

        // list statuses
        g.setColor(Color.black);
        int y = getY() + h / 10;
        String iStr;
        int i;
        for (AbstractStatus s : stats.values()) {
            iStr = "";
            i = 0;
            while (i < s.getIntensityLevel()) {
                iStr += "I";
                i++;
            }
            g.drawString(
                    String.format(
                            "%s %s (%d)",
                            s.getName(), iStr, s.getUsesLeft()
                    ),
                    getX() - r,
                    y
            );
            y += h / 30;
        }

        g.setColor(getTeam().getColor());
        g.fillOval(getX() - r, getY() - r, 2 * r, 2 * r);
        g.setColor(color);
        g.fillOval(getX() - (int) (r * 0.8), getY() - (int) (r * 0.8), (int) (r * 1.6), (int) (r * 1.6));
    }

    public abstract double getStatValue(CharacterStatName n);

    public abstract void playerInit();

    public abstract void playerUpdate();
}
