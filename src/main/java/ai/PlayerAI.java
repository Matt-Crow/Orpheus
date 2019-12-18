package ai;

import entities.AbstractPlayer;
import actions.*;
import controllers.Master;
import graphics.Tile;
import java.io.Serializable;
import util.Coordinates;
import util.Random;

/**
 * The PlayerAI class is used to control non-human-controlled
 * Players. It supports basic behaviours, as seen in AiMode.
 * 
 * In previous versions of Orpheus, all Entities would have AI,
 * but would be turned off for everything but fake players and tracking projectiles.
 * Since tracking projectiles don't feel like they fit with the game anymore,
 * I've removed the base AI class.
 * @author Matt
 */
public class PlayerAI implements Serializable{
	private final AbstractPlayer appliedTo;
    private boolean enabled;
    private AiMode mode;
    private AbstractPlayer latched;
	
	public PlayerAI(AbstractPlayer p){
		appliedTo = p;
		enabled = false;
        mode = AiMode.NONE;
        latched = null;
	}
    
    public void setEnabled(boolean b){
        enabled = b;
        if(b){
            setToWander();
            appliedTo.getActionRegister().addOnBeHit((OnHitEvent e) -> {
                latch((AbstractPlayer)e.getHitter());
            });
        }
    }
    
    /**
     * Get this AbstractPlayer drunk, wander around randomly
     */
    public void setToWander(){
        mode = AiMode.WANDER;
        int attempts = 0;
        int x;
        int y;
        boolean pathFound = false;
        while(attempts < 10 && !pathFound){
            attempts++;
            x = appliedTo.getX() + Random.choose(-3, 3) * Tile.TILE_SIZE;
            y = appliedTo.getY() + Random.choose(-3, 3) * Tile.TILE_SIZE;
            if(appliedTo.getWorld().getMap().isValidIndex(x / Tile.TILE_SIZE, y / Tile.TILE_SIZE)){
                pathFound = true;
                appliedTo.setPath(appliedTo.getWorld().getMap().findPath(appliedTo.getX(), appliedTo.getY(), x, y));
            }
        }
    }
    
    /**
     * Make this AbstractPlayer target another player,
 pursuing them until they are terminated
     * @param p the player to target
     */
    public void latch(AbstractPlayer p){
        latched = p;
        mode = AiMode.PURSUE;
    }
    
	public void setToAttack(){
		mode = AiMode.ATTACK;
    }
    
    
    private void wander(){
        appliedTo.setMoving(true);
        if(checkIfPlayerInSightRange()){
            latchOntoNearest();
        }else if(appliedTo.getPath() == null || appliedTo.getPath().noneLeft()){
            setToWander();
        }
    }
    private void pursue(){
		// check if in range
		if(Coordinates.distanceBetween(appliedTo, latched) <= 100){
			setToAttack();
		} else {
            appliedTo.setPath(
                appliedTo.getWorld().getMap().findPath(
                    appliedTo.getX(), 
                    appliedTo.getY(), 
                    latched.getX(), 
                    latched.getY()
                )
            );
        }
	}
	public void attack(){
		if(latched.getShouldTerminate()){
			setToWander();
			return;
		}
		
		// check if out of range
		if(Coordinates.distanceBetween(appliedTo, latched) >= 100){
			mode = AiMode.PURSUE;
		} else {
			appliedTo.setFocus(latched);
			appliedTo.useMeleeAttack();
			appliedTo.setMoving(false);
		}
	}
    
	public void update(){
		//don't update if AI is disabled,
        //or the enemy team is defeated
        if(
            !enabled 
            || Master.DISABLEALLAI 
            || appliedTo.getTeam().getEnemy().isDefeated()
        ){
            return;
        }
        
		switch(mode){
            case WANDER:
                wander();
                break;
            case PURSUE:
                pursue();
                break;
            case ATTACK:
                attack();
                break;
            case NONE:
                //do nothing
                break;
            default:
                System.out.println("Uncaught AI Mode in PlayerAI.update: " + mode);
                break;
        }
	}
    
    
    
    //move these to world
    public boolean checkIfPlayerInSightRange(){
		AbstractPlayer nearest = nearestEnemy();
		return Coordinates.distanceBetween(appliedTo, nearest) <= Master.DETECTIONRANGE;
	}
	
	public AbstractPlayer nearestEnemy(){
		return appliedTo.getTeam().getEnemy().nearestPlayerTo(appliedTo.getX(), appliedTo.getY());
	}
	public void latchOntoNearest(){
		latch(nearestEnemy());
	}
}
