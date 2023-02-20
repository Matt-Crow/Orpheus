package world.entities;

import util.Settings;
import world.build.AssembledBuild;
import world.build.actives.AbstractActive;
import world.build.characterClass.CharacterClass;
import world.build.characterClass.CharacterStatName;
import world.build.passives.AbstractPassive;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map.Entry;
import util.CardinalDirection;
import util.Direction;
import world.World;

/**
 *
 * @author Matt
 */
public class HumanPlayer extends AbstractPlayer {

    private CharacterClass c;
    private final AbstractActive[] actives;
    private final AbstractPassive[] passives;
    private boolean followingMouse;
    private final HashMap<CardinalDirection, Boolean> movingInCardinalDir; // used for key controls 

    public static final int MIN_LIFE_SPAN = 10;

    /**
     * ensures client & server are using compatible version of this class when
     * serializing / deserializing
     */
    private static final long serialVersionUID = 1L;

    public HumanPlayer(World inWorld, String n) {
        super(inWorld, n, MIN_LIFE_SPAN);
        c = null;
        actives = new AbstractActive[3];
        passives = new AbstractPassive[3];
        movingInCardinalDir = new HashMap<>();
        clearMovement();
    }

    private void clearMovement() {
        for (CardinalDirection dir : CardinalDirection.values()) {
            movingInCardinalDir.put(dir, false);
        }
        followingMouse = false;
    }

    public void applyBuild(AssembledBuild b) {
        setClass(b.getCharacterClass().copy());
        setActives(Arrays.stream(b.getActives()).map((act) -> act.copy()).toArray((s) -> new AbstractActive[s]));
        setPassives(Arrays.stream(b.getPassives()).map((pas) -> pas.copy()).toArray((s) -> new AbstractPassive[s]));
        setMaxSpeed((int) (c.getSpeed() * (500.0 / Settings.FPS)));
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

    public final void setFollowingMouse(boolean b) {
        followingMouse = b;
    }

    public final boolean getFollowingMouse() {
        return followingMouse;
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
                setIsMoving(true);
            } else {
                setIsMoving(false);
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
}
