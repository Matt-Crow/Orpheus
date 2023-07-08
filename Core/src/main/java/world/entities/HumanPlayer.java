package world.entities;

import util.Settings;
import world.statuses.AbstractStatus;

import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;
import java.util.Map.Entry;

import orpheus.core.champions.Playable;
import util.CardinalDirection;
import util.Direction;
import world.World;
import world.builds.actives.AbstractActive;
import world.builds.characterClass.CharacterClass;
import world.builds.characterClass.CharacterStatName;
import world.builds.passives.AbstractPassive;

/**
 *
 * @author Matt
 */
public class HumanPlayer extends AbstractPlayer {

    private final Playable playingAs; // can later change to reference this instead of copying data from it
    private CharacterClass characterClass; // cannot be final yet
    private final AbstractActive[] actives;
    private final AbstractPassive[] passives;
    private final HashMap<CardinalDirection, Boolean> movingInCardinalDir; // used for key controls 

    public static final int MIN_LIFE_SPAN = 10;

    public HumanPlayer(World inWorld, String n, Playable playable) {
        this(inWorld, n, UUID.randomUUID(), playable);
    }

    public HumanPlayer(World inWorld, String n, UUID id, Playable playable) {
        super(inWorld, n, MIN_LIFE_SPAN, id, playable.getBasicAttack());
        actives = new AbstractActive[3];
        passives = new AbstractPassive[3];
        movingInCardinalDir = new HashMap<>();
        playingAs = playable;
        clearMovement();
        playAs(playable);
    }

    private void clearMovement() {
        for (CardinalDirection dir : CardinalDirection.values()) {
            movingInCardinalDir.put(dir, false);
        }
    }

    /**
     * Sets the playable thing this is playing as
     * @param playingAs the playable to play as
     */
    private void playAs(Playable playingAs) {
        setClass(playingAs.getCharacterClass());
        setActives(playingAs.getActives().toArray(AbstractActive[]::new));
        setPassives(playingAs.getPassives().toArray(AbstractPassive[]::new));
        setBaseSpeed((int) (characterClass.getSpeed() * (500.0 / Settings.FPS)));
    }

    private void setClass(CharacterClass characterClass) {
        this.characterClass = characterClass;
        setColor(characterClass.getColor());
        characterClass.setUser(this);
    }

    private void setActives(AbstractActive[] actives) {
        for (int i = 0; i < 3; i++) {
            this.actives[i] = actives[i];
            this.actives[i].setUser(this);
        }
    }

    private void setPassives(AbstractPassive[] passives) {
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
        characterClass.init();
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
                ret = characterClass.getMaxHP();
                break;
            case DMG:
                ret = characterClass.getOffMult();
                break;
            case REDUCTION:
                ret = characterClass.getDefMult();
                break;
            case SPEED:
                ret = characterClass.getSpeed();
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
            playingAs.toGraph(),
            Arrays.stream(actives).map(AbstractActive::toGraph).toList()
        );
    }
}
