package controls.ai;

import gui.graphics.Tile;
import util.Coordinates;
import util.Random;
import world.entities.AbstractPlayer;

/**
 * Get this AbstractPlayer drunk, wander around randomly
 * 
 * @author Matt
 */
public class WanderBehavior extends AbstractBehavior<AbstractPlayer>{
    private static final int DETECTIONRANGE = Tile.TILE_SIZE * 5;
	
    public WanderBehavior(AbstractPlayer target) {
        super(target);
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
                target.setPath(target.getWorld().getMap().findPath(
                    target.getX(), 
                    target.getY(), 
                    x, 
                    y
                ));
            }
        }
    }

    @Override
    public AbstractBehavior update() {
        AbstractBehavior nextBehavior = this;
        AbstractPlayer target = getTarget();
        target.setIsMoving(true);
        if(checkIfPlayerInSightRange()){
            nextBehavior = new PursueBehavior(target, nearestEnemy());
        } else if(target.getPath() == null || target.getPath().noneLeft()){
            // wander some more
            nextBehavior = new WanderBehavior(target);
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
