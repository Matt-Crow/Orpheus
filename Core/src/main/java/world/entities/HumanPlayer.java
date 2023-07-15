package world.entities;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.UUID;

import orpheus.core.champions.Playable;
import util.CardinalDirection;
import util.Direction;
import util.Settings;
import world.World;
import world.builds.actives.AbstractActive;
import world.statuses.AbstractStatus;

/**
 *
 * @author Matt
 */
public class HumanPlayer extends AbstractPlayer {

    private final Playable playingAs2;

    private final HashMap<CardinalDirection, Boolean> movingInCardinalDir; // used for key controls 

    public static final int MIN_LIFE_SPAN = 10;

    public HumanPlayer(World inWorld, String n, Playable playable) {
        this(inWorld, n, UUID.randomUUID(), playable);
    }

    public HumanPlayer(World inWorld, String n, UUID id, Playable playable) {
        super(inWorld, playable, n, MIN_LIFE_SPAN, id, playable.getBasicAttack());
        movingInCardinalDir = new HashMap<>();
        playingAs2 = playable;
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
        playingAs.setUser(this);

        var characterClass = playingAs.getCharacterClass();
        setColor(characterClass.getColor());
        setBaseSpeed((int) (characterClass.getSpeed() * (500.0 / Settings.FPS)));
    }

    public void useAttack(int num) {
        var attack = playingAs2.getActives().get(num);
        if (attack.canUse()) {
            attack.trigger();
        }
    }

    public final void setMovingInDir(CardinalDirection dir, boolean isMoving) {
        movingInCardinalDir.put(dir, isMoving);
    }

    @Override
    public void init() {
        super.init();
        playingAs2.init();
        clearMovement();
    }

    @Override
    public void update() {
        super.update();
        playingAs2.update();

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

    @Override
    public orpheus.core.world.graph.Player toGraph() {
        return new orpheus.core.world.graph.Player(
            getId(),
            getX(), 
            getY(), 
            getRadius(),
            getLog().getHP(),
            getInflictedStatuses().stream().map(AbstractStatus::toString).toList(),
            getTeam().getColor(),
            getColor(),
            playingAs2.toGraph(),
            playingAs2.getActives().stream().map(AbstractActive::toGraph).toList()
        );
    }
}
