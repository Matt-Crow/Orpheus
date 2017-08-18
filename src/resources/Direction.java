package resources;

import java.lang.Math;

public class Direction {
	
	private double radianDegreesPI;
	
	public Direction(double deg){
		radianDegreesPI = deg;
	}
	public double getDirNum(){
		return radianDegreesPI;
	}
	public double getXMod(){
		return Math.cos(radianDegreesPI * Math.PI);
	}
	public double getYMod(){
		return -Math.sin(radianDegreesPI * Math.PI);
	}
	public double[] getVector(){
		return new double[]{getXMod(), getYMod()};
	}
}
