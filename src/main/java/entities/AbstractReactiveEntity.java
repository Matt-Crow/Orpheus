package entities;

import util.Direction;

/**
 * The AbstractReactiveEntity takes the basic movement features from
 * AbstractEntity, and adds in more complexity, so that instead of blindly
 * following a vector, it can react to stimulus.
 * The primary purpose of this class is to remove functionality from Particles,
 * as having them run through every check in this class can slow the game down.
 * 
 * @author Matt Crow
 */
public abstract class AbstractReactiveEntity extends AbstractEntity{
    /*
	 * (focusX, focusY) is a point that the entity is trying to reach
	 */
	private int focusX;
	private int focusY;
	private boolean hasFocus;
    
    private Direction knockbackDir;
    private int knockbackMag;
    private int knockbackDur;
    
    public AbstractReactiveEntity(){
        super();
        focusX = 0;
        focusY = 0;
        hasFocus = false;
        
        knockbackDir = null;
        knockbackMag = 0;
        knockbackDur = 0;
    }
    
    //focus related methods
	public final void setFocus(int xCoord, int yCoord){
		focusX = xCoord;
		focusY = yCoord;
		hasFocus = true;
	}
	public final void setFocus(AbstractEntity e){
		setFocus(e.getX(), e.getY());
	}
	public final void turnToFocus(){
		turnTo(focusX, focusY);
	}
	public boolean withinFocus(){
		// returns if has reached focal point
		boolean withinX = Math.abs(getX() - focusX) < getSpeed();
		boolean withinY = Math.abs(getY() - focusY) < getSpeed();
		return withinX && withinY;
	}
    
    
    
    public final void knockBack(int mag, Direction d, int dur){
        /**
         * @param mag : the total distance this entity will be knocked back
         * @param d : the direction this entity is knocked back
         * @param dur : the number of frames this will be knocked back for
         */
        knockbackMag = mag / dur;
        knockbackDir = d;
        knockbackDur = dur;
    }
    
    
    
    @Override
    public void updateMovement(){
        if(hasFocus){
			if(withinFocus()){
				hasFocus = false;
				setMoving(false);
			}else{
				turnToFocus();
				setMoving(true);
			}
		}
        if(knockbackDir != null){
            //cannot move if being knocked back
            setX((int) (getX() + knockbackMag * knockbackDir.getXMod()));
            setY((int) (getY() + knockbackMag * knockbackDir.getYMod()));
            knockbackDur--;
            if(knockbackDur == 0){
                knockbackDir = null;
            }
        } else {
            super.updateMovement();
        }   
        clearSpeedFilter();
    }
    
    @Override
    public void init(){
        hasFocus = false;
        knockbackDir = null;
        knockbackMag = 0;
        knockbackDur = 0;
    }
}
