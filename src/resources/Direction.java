package resources;

import java.lang.Math;

/**
 * The Direction class is used to 
 * get vectors that are used for
 * entity movement. The class
 * is also used to rotate the 
 * canvas to keep the player
 * facing forward.
 */
public class Direction {
	
	private int degrees;
	
	public Direction(int deg){
		degrees = deg;
	}
	
	public static Direction getDegreeByLengths(int x1, int y1, int x2, int y2){
		int x = x2 - x1;
		int y = y2 - y1;
		int absX = Math.abs(x);
		int absY = Math.abs(y);
		
		double alpha = Math.atan((double)absY / absX);
		alpha = alpha * (180 / Math.PI);
		Op.add(alpha);
		
		double theta = 90;
		
		if((x > 0) && (y > 0)){
			Op.add("Q1");
			theta = alpha;
		} else if ((x < 0) && (y > 0)){
			Op.add("Q2");
			theta = 180 - alpha;
		} else if ((x < 0) && (y < 0)){
			Op.add("Q3");
			theta = 180 + alpha;
		} else if ((x > 0) && (y < 0)){
			Op.add("Q4");
			theta = 360 - alpha;
		} else {
			Op.add("Coterminal: " + alpha);
		}
		Op.dp();
		return new Direction((int)theta);
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
		return degrees * Math.PI / 180;
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
}
