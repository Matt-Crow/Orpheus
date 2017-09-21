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
	
	public void calcDegrees(){
		degrees = (int) (radiansPI * 180);
		setBounds();
	}
	public void calcRadians(){
		radiansPI = (double) degrees / 180.0;
		setBounds();
	}
	
	public void turnClockwise(int deg){
		degrees -= deg;
		calcRadians();
	}
	public void turnClockwise(double deg){
		radiansPI -= deg;
		calcDegrees();
	}
	public void turnCounterClockwise(int deg){
		degrees += deg;
		calcRadians();
	}
	public void turnCounterClockwise(double deg){
		radiansPI += deg;
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
		return radiansPI;
	}
	public int getDegrees(){
		return degrees;
	}
	
	public double getXMod(){
		return Math.cos(radiansPI * Math.PI);
	}
	public double getYMod(){
		return -Math.sin(radiansPI * Math.PI);
	}
	public double[] getVector(){
		return new double[]{getXMod(), getYMod()};
	}
}
