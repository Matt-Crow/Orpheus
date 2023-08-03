package orpheus.core.world.occupants.players;

import java.util.HashMap;
import java.util.Map.Entry;

import util.CardinalDirection;
import util.Direction;

/**
 * controls a player by moving them in cardinal directions
 */
public class CardinalDirectionPlayerController implements PlayerController {

    private final Player controlled;
    private final HashMap<CardinalDirection, Boolean> movingInCardinalDir = new HashMap<>();

    public CardinalDirectionPlayerController(Player managing) {
        this.controlled = managing;
        for (var direction : CardinalDirection.values()) {
            movingInCardinalDir.put(direction, false);
        }
    }

    public void setMovingInDir(CardinalDirection dir, boolean isMoving) {
        movingInCardinalDir.put(dir, isMoving);
    }

    @Override
    public Player getControlled() {
        return controlled;
    }

    @Override
    public void tick() {
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
            controlled.setFacing(newFacing.getDegrees());
            controlled.setMoving(true);
        } else {
            controlled.setMoving(false);
        }
    }
}
