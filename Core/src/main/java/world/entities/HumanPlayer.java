package world.entities;

import util.Settings;
import world.statuses.AbstractStatus;

import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;
import java.util.Map.Entry;
import util.CardinalDirection;
import util.Direction;
import world.World;
import world.builds.AssembledBuild;
import world.builds.actives.AbstractActive;
import world.builds.characterClass.CharacterClass;
import world.builds.characterClass.CharacterStatName;
import world.builds.passives.AbstractPassive;

/**
 *
 * @author Matt
 */
public class HumanPlayer extends AbstractPlayer {

    private CharacterClass c;
    private final AbstractActive[] actives;
    private final AbstractPassive[] passives;
    private final HashMap<CardinalDirection, Boolean> movingInCardinalDir; // used for key controls 

    public static final int MIN_LIFE_SPAN = 10;

    public HumanPlayer(World inWorld, String n, AssembledBuild build) {
        this(inWorld, n, UUID.randomUUID(), build);
    }

    public HumanPlayer(World inWorld, String n, UUID id, AssembledBuild build) {
        super(inWorld, n, MIN_LIFE_SPAN, id, build.getBasicAttack());
        c = null;
        actives = new AbstractActive[3];
        passives = new AbstractPassive[3];
        movingInCardinalDir = new HashMap<>();
        clearMovement();
        applyBuild(build);
    }

    private void clearMovement() {
        for (CardinalDirection dir : CardinalDirection.values()) {
            movingInCardinalDir.put(dir, false);
        }
    }

    private void applyBuild(AssembledBuild b) {
        setClass(b.getCharacterClass().copy());
        setActives(Arrays.stream(b.getActives()).map((act) -> act.copy()).toArray((s) -> new AbstractActive[s]));
        setPassives(Arrays.stream(b.getPassives()).map((pas) -> pas.copy()).toArray((s) -> new AbstractPassive[s]));
        setBaseSpeed((int) (c.getSpeed() * (500.0 / Settings.FPS)));
    }

    public void setClass(CharacterClass characterClass) {
        c = characterClass;
        setColor(c.getColors()[0]);
        c.setUser(this);
    }

    public CharacterClass getCharacterClass() {
        return c;
    }

    public void setActives(AbstractActive[] actives) {
        for (int i = 0; i < 3; i++) {
            this.actives[i] = actives[i];
            this.actives[i].setUser(this);
        }
    }

    public AbstractActive[] getActives() {
        return actives;
    }

    public void setPassives(AbstractPassive[] passives) {
        for (int i = 0; i < 3; i++) {
            this.passives[i] = passives[i];
            this.passives[i].setUser(this);
        }
    }

    public void useAttack(int num) {
        if (actives[num].canUse()) {
            actives[num].trigger();
        }
    }

    public final void setMovingInDir(CardinalDirection dir, boolean isMoving) {
        movingInCardinalDir.put(dir, isMoving);
    }

    @Override
    public void playerInit() {
        c.init();
        for (AbstractActive a : actives) {
            a.init();
        }
        for (AbstractPassive p : passives) {
            p.init();
        }
        clearMovement();
    }

    @Override
    public void playerUpdate() {
        for (AbstractActive a : actives) {
            a.update();
        }
        for (AbstractPassive p : passives) {
            p.update();
        }

        if (this.getPath() == null) { //prevent double movement
            int dx = 0;
            int dy = 0;
            for (Entry<CardinalDirection, Boolean> dir : movingInCardinalDir.entrySet()) {
                if (dir.getValue()) {
                    dx += dir.getKey().getXMod();
                    dy += dir.getKey().getYMod();
                }
            }

            if (dx != 0 || dy != 0) {
                Direction newFacing = Direction.getDegreeByLengths(0, 0, dx, dy);
                setFacing(newFacing.getDegrees());
                setMoving(true);
            } else {
                setMoving(false);
            }
        }
    }

    @Override
    public double getStatValue(CharacterStatName n) {
        double ret = 0.0;
        switch (n) {
            case HP:
                ret = c.getMaxHP();
                break;
            case DMG:
                ret = c.getOffMult();
                break;
            case REDUCTION:
                ret = c.getDefMult();
                break;
            case SPEED:
                ret = c.getSpeed();
                break;
        }
        return ret;
    }

    @Override
    public orpheus.core.world.graph.Player toGraph() {
        return new orpheus.core.world.graph.Player(
            getId(),
            getX(), 
            getY(), 
            getRadius(),
            getLog().getHP(),
            getStatuses().values().stream().map(AbstractStatus::toString).toList(),
            getTeam().getColor(),
            getColor(),
            Arrays.stream(actives).map(AbstractActive::toGraph).toList()
        );
    }
}
