package entities;

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
    
    public AbstractReactiveEntity(){
        super();
        focusX = 0;
        focusY = 0;
        hasFocus = false;
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
        super.updateMovement();
    }
    
    @Override
    public void init(){
        hasFocus = false;
    }
}
