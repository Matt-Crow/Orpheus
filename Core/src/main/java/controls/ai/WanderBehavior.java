package controls.ai;

import world.Tile;
import util.Coordinates;
import util.Random;
import world.entities.AbstractPlayer;

/**
 * Get this AbstractPlayer drunk, wander around randomly
 * 
 * @author Matt
 */
public class WanderBehavior extends AbstractBehavior<AbstractPlayer>{
    
    private final PlayerAI host;

    private static final int DETECTIONRANGE = Tile.TILE_SIZE * 5;
	
    public WanderBehavior(PlayerAI host, AbstractPlayer target) {
        super(target);
        this.host = host;
        setToWander();
    }
    
    private void setToWander(){
        int x;
        int y;
        int t = Tile.TILE_SIZE;
        AbstractPlayer target = getTarget();
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
    public AbstractBehavior<AbstractPlayer> update() {
        AbstractBehavior<AbstractPlayer> nextBehavior = this;
        AbstractPlayer target = getTarget();
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
		AbstractPlayer nearest = nearestEnemy();
		return Coordinates.distanceBetween(getTarget(), nearest) <= DETECTIONRANGE;
	}
	
	private AbstractPlayer nearestEnemy(){
        AbstractPlayer target = getTarget();
		return target.getTeam().getEnemy().nearestPlayerTo(target.getX(), target.getY());
	}
}
