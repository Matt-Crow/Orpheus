package controls.ai;

import world.events.OnHitEvent;

import java.util.Optional;

import orpheus.core.utils.coordinates.Point;
import orpheus.core.world.occupants.WorldOccupant;
import orpheus.core.world.occupants.players.Player;
import orpheus.core.world.occupants.players.PlayerController;
import util.Settings;

/**
 * The PlayerAI class is used to control non-human-controlled
 * Players. It supports extendable behaviors, as seen in AbstractBehavior.
 * 
 * In previous versions of Orpheus, all Entities would have AI,
 * but would be turned off for everything but fake players and tracking projectiles.
 * Since tracking projectiles don't feel like they fit with the game anymore,
 * I've removed the base AI class.
 * 
 * @author Matt Crow
 */
public class PlayerAI implements PlayerController {

    /**
     * the player this controls
     */
	private final Player appliedTo;

    /**
     * strategy design pattern - the current behavior applied to the player
     */
    private AbstractBehavior<Player> currentBehavior = null;

    /**
     * the path this is currently directing the player to move through
     */
    private Optional<Path> currentPath = Optional.empty();

    /**
     * the coordinates this is trying to navigate the player to
     */
    private Optional<Point> focus = Optional.empty();
	

    /**
     * Creates a new AI which controls the given player
     * @param p the player to control
     */
	public PlayerAI(Player p){
		appliedTo = p;
        
        currentBehavior = null;
        currentPath = Optional.empty();
        focus = Optional.empty();
        if(!Settings.DISABLEALLAI){
            currentBehavior = new WanderBehavior(this, appliedTo);
            appliedTo.eventOnBeHit().add((OnHitEvent e) -> {
                currentBehavior = new PursueBehavior(this, appliedTo, (Player)e.getHitter());
            });
        }
    }

    public void setPath(Path p) {
        currentPath = Optional.of(p);
        if (!p.noneLeft()) {
            PathInfo pi = p.get();
            setFocus(pi.getEndX(), pi.getEndY());
        }
    }

    public Optional<Path> getCurrentPath() {
        return currentPath;
    }

    /**
     * makes the player focus on the given WorldOccupant, following their 
     * movements
     * 
     * @param e the WorldOccupant to focus on
     */
    public void setFocus(WorldOccupant e) {
        /*
         * getCoordinates returns a reference, so the focus will change as the
         * WorldOccupant moves, which is what I want
         */
        focus = Optional.of(e.getCoordinates());
    }

    private void setFocus(int xCoord, int yCoord) {
        focus = Optional.of(new Point(xCoord, yCoord));
    }
    
	public void update(){
        if(
            !Settings.DISABLEALLAI &&
            !appliedTo.getTeam().getEnemy().isDefeated() &&
            currentBehavior != null
        ){
            doUpdate();
        }
	}

    private void doUpdate() {
        currentPath.ifPresent(p -> {
            if (p.noneLeft()) {
                currentPath = Optional.empty();
            } else {
                while (focus.isPresent() && appliedTo.isAsCloseAsPossibleTo(focus.get()) && !p.noneLeft()) {
                    p.deque();
                    if (!p.noneLeft()) {
                        var step = p.get();
                        setFocus(step.getEndX(), step.getEndY());
                    }
                }
            }
        });
        focus.ifPresent(f -> {
            if (appliedTo.isAsCloseAsPossibleTo(f)) {
                focus = Optional.empty();
                appliedTo.setMoving(false);
            } else {
                appliedTo.turnTo(f);
                appliedTo.setMoving(true);
            }
        });
        currentBehavior = currentBehavior.update();
    }

    @Override
    public Player getControlled() {
        return appliedTo;
    }

    @Override
    public void tick() {
        update();
    }
}
