package controls.ai;

import world.Tile;
import orpheus.core.world.occupants.players.Player;
import util.Coordinates;
import util.Random;

/**
 * Get this AbstractPlayer drunk, wander around randomly
 * 
 * @author Matt
 */
public class WanderBehavior extends AbstractBehavior<Player>{
    
    private final PlayerAI host;

    private static final int DETECTIONRANGE = Tile.TILE_SIZE * 5;
	
    public WanderBehavior(PlayerAI host, Player target) {
        super(target);
        this.host = host;
        setToWander();
    }
    
    private void setToWander(){
        int x;
        int y;
        int t = Tile.TILE_SIZE;
        Player target = getTarget();
        boolean pathFound = false;
        for(int attempts = 0; !pathFound && attempts < 10; ++ attempts){
            x = target.getX() + Random.choose(-3, 3) * t;
            y = target.getY() + Random.choose(-3, 3) * t;
            if(target.getWorld().getMap().isOpenTile(x, y)){
                pathFound = true;
                host.setPath(target.getWorld().getMap().findPath(
                    target.getX(), 
                    target.getY(), 
                    x, 
                    y
                ));
            }
        }
    }

    @Override
    public AbstractBehavior<Player> update() {
        AbstractBehavior<Player> nextBehavior = this;
        Player target = getTarget();
        target.setMoving(true);
        if(checkIfPlayerInSightRange()){
            nextBehavior = new PursueBehavior(host, target, nearestEnemy());
        } else {
            var path = host.getCurrentPath();
            if (path.isEmpty() || path.get().noneLeft()) {
                // wander some more
                nextBehavior = new WanderBehavior(host, target);
            }
        }
        return nextBehavior; // todo: make them occasionally transition behaviors
    }
    
    // Maybe move these 2 to World
    
    private boolean checkIfPlayerInSightRange(){
		Player nearest = nearestEnemy();
		return Coordinates.distanceBetween(getTarget(), nearest) <= DETECTIONRANGE;
	}
	
	private Player nearestEnemy(){
        Player target = getTarget();
		return target.getTeam().getEnemy().nearestPlayerTo(target.getX(), target.getY());
	}
}
