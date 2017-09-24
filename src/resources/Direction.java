package resources;

import java.lang.Math;

public class Direction {
	
	private double radiansPI;
	private int degrees;
	
	public Direction(double radDeg){
		radiansPI = radDeg;
		calcDegrees();
	}
	public Direction(int deg){
		degrees = deg;
		calcRadians();
	}
	
	public static Direction getDegreeByLengths(int x1, int y1, int x2, int y2){
		double x = x2 - x1;
		double y = y2 - y1;
		double absX = Math.abs(x);
		double absY = Math.abs(y);
		
		double alpha = Math.atan(absY / absX);
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
	
	public void calcDegrees(){
		degrees = (int) (radiansPI * 180);
		setBounds();
	}
	public void calcRadians(){
		radiansPI = (double) degrees / 180.0;
		setBounds();
	}
	
	public void turnClockwise(int deg){
		degrees += deg;
		calcRadians();
	}
	public void turnClockwise(double deg){
		radiansPI += deg;
		calcDegrees();
	}
	public void turnCounterClockwise(int deg){
		degrees -= deg;
		calcRadians();
	}
	
	public void turnCounterClockwise(double deg){
		radiansPI -= deg;
		calcDegrees();
	}
	
	public void setBounds(){
		while(radiansPI < 0.0){
			radiansPI += 2.0;
		}
		while(radiansPI >= 2.0){
			radiansPI -= 2.0;
		}
		while(degrees < 0){
			degrees += 360;
		}
		while(degrees >= 360){
			degrees -= 360;
		}
	}
	
	public double getRadians(){
		return radiansPI * Math.PI;
	}
	public int getDegrees(){
		return degrees;
	}
	
	public double getXMod(){
		return Math.cos(radiansPI * Math.PI);
	}
	public double getYMod(){
		return Math.sin(radiansPI * Math.PI);
	}
	public double[] getVector(){
		return new double[]{getXMod(), getYMod()};
	}
}
