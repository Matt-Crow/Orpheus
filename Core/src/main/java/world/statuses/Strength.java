package world.statuses;

import world.events.EventListener;
import world.events.OnHitEvent;
import util.Settings;
import world.Tile;
import java.util.function.UnaryOperator;

import orpheus.core.utils.UndoableOperation;
import orpheus.core.world.occupants.players.Player;
import util.Direction;
import util.Number;

/**
 * Strength causes the afflicted AbstractEntity to knock back those it hits and do more damage
 */
public class Strength extends AbstractStatus implements EventListener<OnHitEvent> {
    private static final UnaryOperator<Integer> CALC = (i)->{return Number.minMax(1, i, 3) * 2 + 1;};
    /**
     * 
     * @param lv 1-3. The afflicted's melee attack do some knockback 
     * and deal an extra 3.5% of the targets maximum HP for each level of this status.
     * @param uses effect lasts for the next ((uses * 2) + 1) melee attacks the afflicted performs that hit an enemy.
     */
	public Strength(int lv, int uses){
		super("strength", lv, uses, CALC);
		// 3 - 7 uses of 3.5% to 10.5% extra damage logged and knocks back lv units
	}

    @Override
    public UndoableOperation<Player> getInflictor() {
        return makeEventBinder(p -> p.eventOnHit(), this);
    }

    @Override
	public String getDesc(){
		return "Strength, causing the inflicted's next " + getMaxUses() + " attacks to deal an extra " + (3.5 * getIntensityLevel()) + "% of the target's maximum HP and knock them back " + (3.5 * getIntensityLevel()) + " units";
	}

    @Override
    public AbstractStatus copy() {
        return new Strength(getIntensityLevel(), getBaseParam());
    }

    @Override
    public void handle(OnHitEvent e) {
        Player user = (Player)e.getHitter();
        Player target = (Player)e.getWasHit();
        target.getDamage().logPercentageDamage(3.5 * getIntensityLevel());

        Direction angleBetween = Direction.getDegreeByLengths(user.getX(), user.getY(), target.getX(), target.getY());
        int magnitude = Tile.TILE_SIZE * getIntensityLevel();
        target.knockBack(magnitude, angleBetween, Settings.seconds(1));
        
        use();
    }
}
