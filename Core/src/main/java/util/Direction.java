package util;

import java.io.Serializable;

/**
 * The Direction class is used to 
 * get vectors that are used for
 * entity movement. The class
 * is also used to rotate the 
 * canvas to keep the player
 * facing forward.
 */
public class Direction implements Serializable{
	
	private int degrees;
	
	public Direction(int deg){
		degrees = deg;
		setBounds();
	}

	public static Direction fromDegrees(int degrees) {
		return new Direction(degrees);
	}
	
	public static Direction getDegreeByLengths(int x1, int y1, int x2, int y2){
		int x = x2 - x1;
		int y = y2 - y1;
		int absX = Math.abs(x);
		int absY = Math.abs(y);
		
		double alpha = Math.atan((double)absY / absX);
		
		alpha = alpha * (180 / Math.PI);
		
		double theta = 90;
		// use cartesian coords, not reversed y-axis
		if((x > 0) && (y < 0)){
			theta = alpha;
		} else if ((x < 0) && (y < 0)){
			theta = 180 - alpha;
		} else if ((x < 0) && (y > 0)){
			theta = 180 + alpha;
		} else if ((x > 0) && (y > 0)){
			theta = 360 - alpha;
		}
		
		if(x == 0){
			if(y < 0){
				theta = 90;
			} else {
				theta = 270;
			}
		} else if (y == 0){
			if(x > 0){
				theta = 0;
			} else {
				theta = 180;
			}
		}
		
		return new Direction((int)theta);
	}
	
    public final void setDegrees(int deg){
        degrees = deg;
        setBounds();
    }
    
	public void turnClockwise(int deg){
		degrees += deg;
		setBounds();
	}
	public void turnCounterClockwise(int deg){
		degrees -= deg;
		setBounds();
	}
	
	public void setBounds(){
		while(degrees < 0){
			degrees += 360;
		}
		while(degrees >= 360){
			degrees -= 360;
		}
	}
	public double getAsRadians(){
		return degrees * (Math.PI / 180);
	}
	public int getDegrees(){
		return degrees;
	}
	
	public double getXMod(){
		return Math.cos(getAsRadians());
	}
	public double getYMod(){
		return -Math.sin(getAsRadians());
	}
	public double[] getVector(){
		return new double[]{getXMod(), getYMod()};
	}

	/**
	 * @return a deep copy of this Direction
	 */
	public Direction copy() {
		return Direction.fromDegrees(degrees);
	}
}
